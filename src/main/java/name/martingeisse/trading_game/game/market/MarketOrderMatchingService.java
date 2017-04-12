package name.martingeisse.trading_game.game.market;

import com.querydsl.core.QueryException;
import name.martingeisse.trading_game.common.database.DatabaseUtil;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.QMarketOrderRow;

/**
 * Finds matching market orders and resolves them against each other.
 */
public class MarketOrderMatchingService {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;
	private final EscrowService escrowService;

	public MarketOrderMatchingService(PostgresContextService postgresContextService, JacksonService jacksonService, EscrowService escrowService) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
		this.escrowService = escrowService;
	}

	/**
	 * Looks for matching orders for the specified order.
	 */
	public void match(MarketOrder marketOrder) {

		/*

		TODO

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


		// remove fully matched orders in a race condition free way
		postgresContextService.delete(qmo).where(qmo.quantity.eq(0)).execute();

		 */
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
