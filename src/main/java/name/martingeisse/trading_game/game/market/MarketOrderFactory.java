package name.martingeisse.trading_game.game.market;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.MarketOrderRow;
import name.martingeisse.trading_game.postgres_entities.QMarketOrderRow;

/**
 * TODO install a job to remove market orders with quantity = 0
 */
public class MarketOrderFactory {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;

	public MarketOrderFactory(PostgresContextService postgresContextService, JacksonService jacksonService) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
	}

	public void createMarketOrder(Player principal, SpaceObject location, MarketOrderType marketOrderType, ItemType itemType, int quantity, long unitPrice) {
		QMarketOrderRow qmo = QMarketOrderRow.MarketOrder;
		String serializedItemType = jacksonService.serialize(itemType);

		// first check for matching orders of opposite type
		{
			BooleanExpression predicate = qmo.principalPlayerId.ne(principal.getId())
					.and(qmo.locationSpaceObjectBaseDataId.eq(location.getId()))
					.and(qmo.type.eq(marketOrderType.getOpposite()))
					.and(qmo.itemType.eq(serializedItemType));
			if (marketOrderType == MarketOrderType.BUY) {
				predicate = predicate.and(qmo.unitPrice.loe(unitPrice));
			} else {
				predicate = predicate.and(qmo.unitPrice.goe(unitPrice));
			}
			try (CloseableIterator<Tuple> iterator = postgresContextService.select(qmo.id, qmo.quantity, qmo.unitPrice).from(qmo).where(predicate).iterate()) {
				while (iterator.hasNext() && quantity > 0) {
					Tuple tuple = iterator.next();
					int matchQuantity = tuple.get(qmo.quantity);
					if (matchQuantity > quantity) {
						matchQuantity = quantity;
					}
					matchQuantity = reduceOrderQuantity(tuple.get(qmo.id), matchQuantity);
					if (matchQuantity > 0) {
						long matchUnitPrice = (unitPrice + tuple.get(qmo.unitPrice)) / 2;
						long matchPrice = matchQuantity * matchUnitPrice;
						// TODO transfer money
						quantity -= matchQuantity;
					}
				}
			}
		}

		// then create an order for the remaining quantity, if any
		if (quantity > 0) {
			MarketOrderRow data = new MarketOrderRow();
			data.setPrincipalPlayerId(principal.getId());
			data.setLocationSpaceObjectBaseDataId(location.getId());
			data.setType(marketOrderType);
			data.setItemType(serializedItemType);
			data.setQuantity(quantity);
			data.setUnitPrice(unitPrice);
			data.insert(postgresContextService.getConnection());
		}

	}

	private int reduceOrderQuantity(long id, int reduction) {
		QMarketOrderRow qmo = QMarketOrderRow.MarketOrder;
		int retries = 100;
		while (retries > 0) {
			try {
				// TODO check the "updated rows" count to avoid race conditions
				postgresContextService.update(qmo).set(qmo.quantity, qmo.quantity.subtract(reduction)).execute();
				return reduction;
			} catch (Exception e) { // TODO check for the correct exception type and fields
				reduction = postgresContextService.select(qmo.quantity).from(qmo).where(qmo.id.eq(id)).fetchFirst();
			}
			retries--;
		}
		throw new RuntimeException("could not reduce market order quantity for ID " + id + " by " + reduction);
	}

}
