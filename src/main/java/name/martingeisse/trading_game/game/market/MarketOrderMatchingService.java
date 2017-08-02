package name.martingeisse.trading_game.game.market;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.QueryException;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
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
@Singleton
public class MarketOrderMatchingService {

	private final PostgresContextService postgresContextService;
	private final EntityProvider entityProvider;
	private final EscrowService escrowService;

	@Inject
	public MarketOrderMatchingService(PostgresContextService postgresContextService, EntityProvider entityProvider, EscrowService escrowService) {
		this.postgresContextService = postgresContextService;
		this.entityProvider = entityProvider;
		this.escrowService = escrowService;
	}

	/**
	 * Looks for matching orders for the specified order.
	 */
	public void match(MarketOrder marketOrder) {
		if (marketOrder == null) {
			throw new IllegalArgumentException("marketOrder cannot be null");
		}
		if (marketOrder.getLocationSpaceObjectBaseDataId() == null) {
			throw new IllegalArgumentException("marketOrder must have a location");
		}
		if (marketOrder.getQuantity() == null) {
			throw new IllegalArgumentException("marketOrder must have a quantity");
		}

		ItemType itemType = marketOrder.getItemType();
		QMarketOrderRow qmo = QMarketOrderRow.MarketOrder;
		BooleanExpression predicate = qmo.principalPlayerId.ne(marketOrder.getPrincipalPlayerId())
				.and(qmo.locationSpaceObjectBaseDataId.isNull().or(qmo.locationSpaceObjectBaseDataId.eq(marketOrder.getLocationSpaceObjectBaseDataId())))
				.and(qmo.type.eq(marketOrder.getType().getOpposite()))
				.and(qmo.itemType.eq(marketOrder.getSerializedItemType()));
		OrderSpecifier<?> order;
		if (marketOrder.getType() == MarketOrderType.BUY) {
			predicate = predicate.and(qmo.unitPrice.loe(marketOrder.getUnitPrice()));
			order = qmo.unitPrice.asc();
		} else {
			predicate = predicate.and(qmo.unitPrice.goe(marketOrder.getUnitPrice()));
			order = qmo.unitPrice.desc();
		}
		try (CloseableIterator<Tuple> iterator = postgresContextService.select(qmo.id, qmo.quantity, qmo.unitPrice).from(qmo).where(predicate).orderBy(order).iterate()) {
			while (iterator.hasNext()) {
				int quantity = marketOrder.getQuantity();
				if (quantity < 1) {
					break;
				}
				Tuple tuple = iterator.next();
				int matchQuantity;
				if (tuple.get(qmo.quantity) == null) {
					matchQuantity = quantity;
				} else {
					matchQuantity = Math.min(quantity, tuple.get(qmo.quantity));
				}
				if (matchQuantity > 0) {
					// TODO transaction

					// The price is that of the existing order. This is totally arbitrary, but simplifies the
					// common case that a newly created order should match multiple existing orders. By using the
					// existing order's price, we spare the creating player the necessity of creating multiple
					// orders with different prices for maximum profit.
					long matchUnitPrice = tuple.get(qmo.unitPrice);
					long matchPrice = matchQuantity * matchUnitPrice;

					// reduce order quantities (the matched one in variable 'tuple' may be unlimited)
					if (tuple.get(qmo.quantity) != null) {
						// TODO rollback if reduction fails, especially for the second one
						matchQuantity = reduceOrderQuantity(tuple.get(qmo.id), matchQuantity);
					}
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
		postgresContextService.delete(qmo).where(qmo.quantity.isNotNull(), qmo.quantity.eq(0)).execute();

	}

	private int reduceOrderQuantity(long id, int reduction) {
		QMarketOrderRow qmo = QMarketOrderRow.MarketOrder;
		int retries = 100;
		while (retries > 0) {
			try {
				if (reduction == 0) {
					return 0;
				}
				if (postgresContextService.update(qmo).set(qmo.quantity, qmo.quantity.subtract(reduction)).where(qmo.id.eq(id)).execute() == 0) {
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
