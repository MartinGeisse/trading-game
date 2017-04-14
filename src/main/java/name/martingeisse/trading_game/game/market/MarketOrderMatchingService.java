package name.martingeisse.trading_game.game.market;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.QueryException;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import name.martingeisse.trading_game.common.database.DatabaseUtil;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.QMarketOrderRow;

/**
 * Finds matching market orders and resolves them against each other.
 */
public class MarketOrderMatchingService {

	private final PostgresContextService postgresContextService;
	private final EntityProvider entityProvider;
	private final EscrowService escrowService;

	public MarketOrderMatchingService(PostgresContextService postgresContextService, EntityProvider entityProvider, EscrowService escrowService) {
		this.postgresContextService = postgresContextService;
		this.entityProvider = entityProvider;
		this.escrowService = escrowService;
	}

	/**
	 * Looks for matching orders for the specified order.
	 */
	public void match(MarketOrder marketOrder) {
		ItemType itemType = marketOrder.getItemType();

		QMarketOrderRow qmo = QMarketOrderRow.MarketOrder;
		BooleanExpression predicate = qmo.principalPlayerId.ne(marketOrder.getPrincipalPlayerId())
			.and(qmo.locationSpaceObjectBaseDataId.eq(marketOrder.getLocationSpaceObjectBaseDataId()))
			.and(qmo.type.eq(marketOrder.getType().getOpposite()))
			.and(qmo.itemType.eq(marketOrder.getSerializedItemType()));
		if (marketOrder.getType() == MarketOrderType.BUY) {
			predicate = predicate.and(qmo.unitPrice.loe(marketOrder.getUnitPrice()));
		} else {
			predicate = predicate.and(qmo.unitPrice.goe(marketOrder.getUnitPrice()));
		}
		try (CloseableIterator<Tuple> iterator = postgresContextService.select(qmo.id, qmo.quantity, qmo.unitPrice).from(qmo).where(predicate).iterate()) {
			while (iterator.hasNext()) {
				int quantity = marketOrder.getQuantity();
				if (quantity < 1) {
					break;
				}
				Tuple tuple = iterator.next();
				int matchQuantity = tuple.get(qmo.quantity);
				if (matchQuantity > quantity) {
					matchQuantity = quantity;
				}

				if (matchQuantity > 0) {
					// TODO transaction

					// determine matched quantity
					long matchUnitPrice = (marketOrder.getUnitPrice() + tuple.get(qmo.unitPrice)) / 2;
					long matchPrice = matchQuantity * matchUnitPrice;

					// reduce orders TODO rollback if reduction fails, especially for the second one
					matchQuantity = reduceOrderQuantity(tuple.get(qmo.id), matchQuantity);
					reduceOrderQuantity(marketOrder.getId(), matchQuantity);

					// determine buyer/seller
					Player buyer, seller;
					if (marketOrder.getType() == MarketOrderType.BUY) {
						buyer = entityProvider.getPlayer(marketOrder.getPrincipalPlayerId());
						seller = entityProvider.getPlayer(tuple.get(qmo.principalPlayerId));
					} else {
						buyer = entityProvider.getPlayer(tuple.get(qmo.principalPlayerId));
						seller = entityProvider.getPlayer(marketOrder.getPrincipalPlayerId());
					}

					// give items from escrow to the buyer
					SpaceObject location = entityProvider.getSpaceObject(marketOrder.getLocationSpaceObjectBaseDataId());
					escrowService.removeItemsFromEscrow(location, buyer, itemType, matchQuantity);

					// give money from escrow to the seller
					escrowService.removeMoneyFromEscrow(seller, matchPrice);
				}
			}
		}

		// remove fully matched orders in a race condition free way
		postgresContextService.delete(qmo).where(qmo.quantity.eq(0)).execute();

	}

	private int reduceOrderQuantity(long id, int reduction) {
		QMarketOrderRow qmo = QMarketOrderRow.MarketOrder;
		int retries = 100;
		while (retries > 0) {
			try {
				if (reduction == 0) {
					return 0;
				}
				if (postgresContextService.update(qmo).set(qmo.quantity, qmo.quantity.subtract(reduction)).execute() == 0) {
					return 0;
				} else {
					return reduction;
				}
			} catch (QueryException e) {
				if (DatabaseUtil.isCheckConstraintViolation(e, "MarketOrder_quantity_check")) {
					reduction = postgresContextService.select(qmo.quantity).from(qmo).where(qmo.id.eq(id)).fetchFirst();
				} else {
					throw e;
				}
			}
			retries--;
		}
		throw new RuntimeException("could not reduce market order quantity for ID " + id + " by " + reduction);
	}

}