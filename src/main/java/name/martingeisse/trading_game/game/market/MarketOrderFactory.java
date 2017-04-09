package name.martingeisse.trading_game.game.market;

import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.MarketOrderRow;

/**
 *
 */
public class MarketOrderFactory {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;

	public MarketOrderFactory(PostgresContextService postgresContextService, JacksonService jacksonService) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
	}

	public void createMarketOrder(Player principal, SpaceObject location, MarketOrderType marketOrderType, ItemType itemType, int quantity, long unitPrice) {
		MarketOrderRow data = new MarketOrderRow();
		data.setPrincipalPlayerId(principal.getId());
		data.setLocationSpaceObjectBaseDataId(location.getId());
		data.setType(marketOrderType);
		data.setItemType(jacksonService.serialize(itemType));
		data.setQuantity(quantity);
		data.setUnitPrice(unitPrice);
		data.insert(postgresContextService.getConnection());
	}

}
