package name.martingeisse.trading_game.game.space;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.item.InventoryFactory;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.SpaceObjectBaseDataRow;
import org.postgresql.geometric.PGpoint;

/**
 *
 */
@Singleton
public class SpaceObjectFactory {

	private final PostgresService postgresService;
	private final InventoryFactory inventoryFactory;

	@Inject
	public SpaceObjectFactory(PostgresService postgresService, InventoryFactory inventoryFactory) {
		this.postgresService = postgresService;
		this.inventoryFactory = inventoryFactory;
	}

	/**
	 * Creates a new player ship and returns its ID.
	 */
	public long createPlayerShip(String name, long x, long y) {
		long inventoryId = inventoryFactory.createInventory();
		try (PostgresConnection connection = postgresService.newConnection()) {
			SpaceObjectBaseDataRow baseData = new SpaceObjectBaseDataRow();
			baseData.setType(SpaceObjectType.PLAYER_SHIP);
			baseData.setName(name);
			baseData.setPosition(new PGpoint(x, y));
			baseData.setInventoryId(inventoryId);
			baseData.insert(connection);
			return baseData.getId();
		}
	}

}
