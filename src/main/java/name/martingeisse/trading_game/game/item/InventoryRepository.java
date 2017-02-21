package name.martingeisse.trading_game.game.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.InventoryRow;
import name.martingeisse.trading_game.postgres_entities.InventorySlotRow;

/**
 * Helper class to handle mixed injection / id parameters in class {@link Inventory}.
 */
@Singleton
public class InventoryRepository {

	private final PostgresService postgresService;
	private final ItemTypeSerializer itemTypeSerializer;

	@Inject
	public InventoryRepository(PostgresService postgresService, ItemTypeSerializer itemTypeSerializer) {
		this.postgresService = postgresService;
		this.itemTypeSerializer = itemTypeSerializer;
	}

	/**
	 * Creates a new instance based by the specified database ID. This constructor does not ensure that the ID
	 * actually exists in the database.
	 *
	 * @param id the inventory ID
	 */
	public Inventory getInventory(long id) {
		return new Inventory(postgresService, itemTypeSerializer, id);
	}

	/**
	 * Creates a new, empty inventory
	 *
	 * @return the ID of the new inventory
	 */
	public long createInventory() {
		try (PostgresConnection connection = postgresService.newConnection()) {
			InventoryRow inventory = new InventoryRow();
			inventory.insert(connection);
			return inventory.getId();
		}
	}

	/**
	 * Creates a new inventory with the specified contents
	 *
	 * @return the ID of the new inventory
	 */
	public long createInventory(ImmutableItemStacks itemStacks) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			InventoryRow inventory = new InventoryRow();
			inventory.insert(connection);
			for (ImmutableItemStack stack : itemStacks.getStacks()) {
				InventorySlotRow slot = new InventorySlotRow();
				slot.setInventoryId(inventory.getId());
				slot.setItemType(itemTypeSerializer.serializeItemType(stack.getItemType()));
				slot.setQuantity(stack.getSize());
				slot.insert(connection);
			}
			return inventory.getId();
		}
	}

}
