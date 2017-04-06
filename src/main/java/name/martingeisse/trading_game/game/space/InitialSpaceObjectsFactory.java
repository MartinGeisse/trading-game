package name.martingeisse.trading_game.game.space;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.item.InventoryFactory;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseDataRow;
import name.martingeisse.trading_game.postgres_entities.SpaceObjectBaseDataRow;
import org.postgresql.geometric.PGpoint;

/**
 * Creates the initial space objects.
 */
@Singleton
public final class InitialSpaceObjectsFactory {

	private static final QSpaceObjectBaseDataRow qbd = QSpaceObjectBaseDataRow.SpaceObjectBaseData;

	private final PostgresService postgresService;
	private final GameDefinition gameDefinition;
	private final InventoryFactory inventoryFactory;

	@Inject
	public InitialSpaceObjectsFactory(PostgresService postgresService, GameDefinition gameDefinition, InventoryFactory inventoryFactory) {
		this.postgresService = postgresService;
		this.gameDefinition = gameDefinition;
		this.inventoryFactory = inventoryFactory;
	}

	public void createAsteroid(String name, long x, long y, ImmutableItemStacks miningYield, long miningCapacity) {
		insertBaseData(SpaceObjectType.ASTEROID, name, x, y, inventoryFactory.createInventory(miningYield), miningCapacity);
	}

	public void createSpaceStation(String name, long x, long y) {
		// TODO these are just sample items for testing
		ImmutableItemStacks inventory = ImmutableItemStacks.from(gameDefinition.getRedPixelItemType(), 1000, gameDefinition.getRedPixelAssemblyItemType(), 1000);
		insertBaseData(SpaceObjectType.SPACE_STATION, name, x, y, inventoryFactory.createInventory(inventory), null);
	}

	private long insertBaseData(SpaceObjectType type, String name, long x, long y, Long inventoryId, Long longField1) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			SpaceObjectBaseDataRow baseData = new SpaceObjectBaseDataRow();
			baseData.setType(type);
			baseData.setName(name);
			baseData.setPosition(new PGpoint(x, y));
			baseData.setInventoryId(inventoryId);
			baseData.setLongField1(longField1);
			baseData.insert(connection);
			return baseData.getId();
		}
	}

}
