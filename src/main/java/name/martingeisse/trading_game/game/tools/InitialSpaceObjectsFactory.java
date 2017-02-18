package name.martingeisse.trading_game.game.tools;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.item.ImmutableItemStack;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.space.SpaceObjectType;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.InventoryRow;
import name.martingeisse.trading_game.postgres_entities.InventorySlotRow;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseDataRow;
import name.martingeisse.trading_game.postgres_entities.SpaceObjectBaseDataRow;

/**
 * Creates the initial space objects.
 */
@Singleton
public final class InitialSpaceObjectsFactory {

	private static final QSpaceObjectBaseDataRow qbd = QSpaceObjectBaseDataRow.SpaceObjectBaseData;

	private final PostgresService postgresService;
	private final GameDefinition gameDefinition;

	@Inject
	public InitialSpaceObjectsFactory(PostgresService postgresService, GameDefinition gameDefinition) {
		this.postgresService = postgresService;
		this.gameDefinition = gameDefinition;
	}

	public void createAsteroid(String name, long x, long y, ImmutableItemStacks miningYield, long miningCapacity) {
		insertBaseData(SpaceObjectType.ASTEROID, name, x, y, insertInventory(miningYield), miningCapacity);
	}

	public void createSpaceStation(String name, long x, long y) {
//		// TODO for testing
		ImmutableItemStacks inventory = ImmutableItemStacks.from(gameDefinition.getRedPixelItemType(), 1000, gameDefinition.getRedPixelAssemblyItemType(), 1000);
		insertBaseData(SpaceObjectType.SPACE_STATION, name, x, y, insertInventory(inventory), null);
	}

	private long insertInventory(ImmutableItemStacks contents) {
		try (PostgresConnection connection = postgresService.newConnection()) {

			InventoryRow inventory = new InventoryRow();
			inventory.insert(connection);

			for (ImmutableItemStack stack : contents.getStacks()) {
				InventorySlotRow slot = new InventorySlotRow();
				slot.setInventoryId(inventory.getId());
				slot.setQuantity(stack.getSize());
				// TODO set item type
				slot.insert(connection);
			}

			return inventory.getId();
		}
	}

	private long insertBaseData(SpaceObjectType type, String name, long x, long y, Long inventoryId, Long longField1) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			SpaceObjectBaseDataRow baseData = new SpaceObjectBaseDataRow();
			baseData.setType(type);
			baseData.setName(name);
			baseData.setX(x);
			baseData.setY(y);
			baseData.setInventoryId(inventoryId);
			baseData.setLongField1(longField1);
			baseData.insert(connection);
			return baseData.getId();
		}
	}

}
