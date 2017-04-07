package name.martingeisse.trading_game.game.space;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.item.InventoryFactory;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.SpaceObjectBaseDataRow;
import org.postgresql.geometric.PGpoint;

/**
 *
 */
@Singleton
public class SpaceObjectFactory {

	private final PostgresContextService postgresContextService;
	private final InventoryFactory inventoryFactory;

	@Inject
	public SpaceObjectFactory(PostgresContextService postgresContextService, InventoryFactory inventoryFactory) {
		this.postgresContextService = postgresContextService;
		this.inventoryFactory = inventoryFactory;
	}

	/**
	 * Creates a new player ship and returns its ID.
	 */
	public long createPlayerShip(String name, long x, long y) {
		long inventoryId = inventoryFactory.createInventory();
		SpaceObjectBaseDataRow baseData = new SpaceObjectBaseDataRow();
		baseData.setType(SpaceObjectType.PLAYER_SHIP);
		baseData.setName(name);
		baseData.setPosition(new PGpoint(x, y));
		baseData.setInventoryId(inventoryId);
		baseData.insert(postgresContextService.getConnection());
		return baseData.getId();
	}

}
