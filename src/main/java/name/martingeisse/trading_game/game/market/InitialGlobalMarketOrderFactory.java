package name.martingeisse.trading_game.game.market;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.GameLogicException;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.MarketOrderRow;
import name.martingeisse.trading_game.postgres_entities.QMarketOrderRow;

/**
 * This factory is used to create the game's global (location-less) market orders on initialization.
 *
 * No matching is done at this time.
 */
@Singleton
public class InitialGlobalMarketOrderFactory {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;

	@Inject
	public InitialGlobalMarketOrderFactory(PostgresContextService postgresContextService, JacksonService jacksonService) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
	}

	public void createMarketOrder(Player principal, MarketOrderType marketOrderType, ItemType itemType, long unitPrice) throws GameLogicException {
		if (principal == null) {
			throw new IllegalArgumentException("principal cannot be null");
		}
		if (marketOrderType == null) {
			throw new IllegalArgumentException("marketOrderType cannot be null");
		}
		if (itemType == null) {
			throw new IllegalArgumentException("itemType cannot be null");
		}
		if (unitPrice < 0) {
			throw new IllegalArgumentException("unitPrice must be non-negative");
		}


	String serializedItemType = jacksonService.serialize(itemType);
		MarketOrderRow data = new MarketOrderRow();
		data.setPrincipalPlayerId(principal.getId());
		data.setLocationSpaceObjectBaseDataId(null);
		data.setType(marketOrderType);
		data.setItemType(serializedItemType);
		data.setQuantity(null);
		data.setUnitPrice(unitPrice);
		data.insert(postgresContextService.getConnection());
	}

}
