package name.martingeisse.trading_game.game.market;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.GameLogicException;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.MarketOrderRow;
import name.martingeisse.trading_game.postgres_entities.QMarketOrderRow;

/**
 * Note: Since market orders are matched whenever a new order is created, this factory also does all the matching.
 */
@Singleton
public class MarketOrderFactory {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;
	private final EscrowService escrowService;
	private final MarketOrderMatchingService marketOrderMatchingService;

	@Inject
	public MarketOrderFactory(PostgresContextService postgresContextService, JacksonService jacksonService, EscrowService escrowService, MarketOrderMatchingService marketOrderMatchingService) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
		this.escrowService = escrowService;
		this.marketOrderMatchingService = marketOrderMatchingService;
	}

	public void createMarketOrder(Player principal, SpaceObject location, MarketOrderType marketOrderType, ItemType itemType, int quantity, long unitPrice) throws GameLogicException {
		if (principal == null) {
			throw new IllegalArgumentException("principal cannot be null");
		}
		if (location == null) {
			throw new IllegalArgumentException("location cannot be null");
		}
		if (marketOrderType == null) {
			throw new IllegalArgumentException("marketOrderType cannot be null");
		}
		if (itemType == null) {
			throw new IllegalArgumentException("itemType cannot be null");
		}
		if (quantity < 1) {
			throw new IllegalArgumentException("quantity must be positive");
		}
		if (unitPrice < 0) {
			throw new IllegalArgumentException("unitPrice must be non-negative");
		}

		// Put money / items in escrow. This also ensures that the principal has those items or money in the first place.
		if (marketOrderType == MarketOrderType.BUY) {
			escrowService.putMoneyInEscrow(principal, quantity * unitPrice);
		} else {
			escrowService.putItemsInEscrow(location, principal, itemType, quantity);
		}

		// create the market order
		String serializedItemType = jacksonService.serialize(itemType);
		MarketOrderRow data = new MarketOrderRow();
		data.setPrincipalPlayerId(principal.getId());
		data.setLocationSpaceObjectBaseDataId(location.getId());
		data.setType(marketOrderType);
		data.setItemType(serializedItemType);
		data.setQuantity(quantity);
		data.setUnitPrice(unitPrice);
		data.insert(postgresContextService.getConnection());

		// match the new market order against existing ones
		MarketOrder marketOrder = new MarketOrder(postgresContextService, jacksonService, data.getId());
		marketOrderMatchingService.match(marketOrder);

	}

}
