package name.martingeisse.trading_game.game.space;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.item.InventoryFactory;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseDataRow;
import name.martingeisse.trading_game.postgres_entities.SpaceObjectBaseDataRow;
import org.postgresql.geometric.PGpoint;

/**
 * Creates the initial space objects.
 */
@Singleton
public final class InitialSpaceObjectsFactory {

	private static final QSpaceObjectBaseDataRow qbd = QSpaceObjectBaseDataRow.SpaceObjectBaseData;

	private final PostgresContextService postgresContextService;
	private final GameDefinition gameDefinition;
	private final InventoryFactory inventoryFactory;

	@Inject
	public InitialSpaceObjectsFactory(PostgresContextService postgresContextService, GameDefinition gameDefinition, InventoryFactory inventoryFactory) {
		this.postgresContextService = postgresContextService;
		this.gameDefinition = gameDefinition;
		this.inventoryFactory = inventoryFactory;
	}

	public void createStar(String name, long x, long y) {
		insertBaseData(SpaceObjectType.STAR, name, x, y, null, null);
	}

	public void createPlanet(String name, long x, long y) {
		insertBaseData(SpaceObjectType.PLANET, name, x, y, null, null);
	}

	public void createAsteroid(String name, long x, long y, ImmutableItemStacks miningYield, long miningCapacity) {
		insertBaseData(SpaceObjectType.ASTEROID, name, x, y, inventoryFactory.createInventory(miningYield), miningCapacity);
	}

	public void createSpaceStation(String name, long x, long y) {
		// TODO these are just sample items for testing
		// ImmutableItemStacks inventory = ImmutableItemStacks.from(gameDefinition.getRedPixelItemType(), 1000, gameDefinition.getRedPixelAssemblyItemType(), 1000);
		ImmutableItemStacks inventory = ImmutableItemStacks.EMPTY;
		insertBaseData(SpaceObjectType.SPACE_STATION, name, x, y, inventoryFactory.createInventory(inventory), null);
	}

	private long insertBaseData(SpaceObjectType type, String name, long x, long y, Long inventoryId, Long longField1) {
		SpaceObjectBaseDataRow baseData = new SpaceObjectBaseDataRow();
		baseData.setType(type);
		baseData.setName(name);
		baseData.setPosition(new PGpoint(x, y));
		baseData.setInventoryId(inventoryId);
		baseData.setLongField1(longField1);
		baseData.insert(postgresContextService.getConnection());
		return baseData.getId();
	}

}
