package name.martingeisse.trading_game.game.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.jackson.JacksonService;
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
	private final JacksonService jacksonService;
	private final GameEventEmitter gameEventEmitter;

	@Inject
	public InventoryRepository(PostgresService postgresService, JacksonService jacksonService, GameEventEmitter gameEventEmitter) {
		this.postgresService = postgresService;
		this.jacksonService = jacksonService;
		this.gameEventEmitter = gameEventEmitter;
	}

	/**
	 * Creates a new instance based by the specified database ID. This constructor does not ensure that the ID
	 * actually exists in the database.
	 *
	 * @param id the inventory ID
	 */
	public Inventory getInventory(long id) {
		return new Inventory(postgresService, jacksonService, gameEventEmitter, id);
	}

	/**
	 * Creates a new instance based by the specified database ID, filtered by player ID. This constructor does not
	 * ensure that the ID actually exists in the database.
	 *
	 * @param id             the inventory ID
	 * @param playerIdFilter the player ID ot filter by
	 */
	public Inventory getInventory(long id, long playerIdFilter) {
		return new Inventory(postgresService, jacksonService, gameEventEmitter, id, playerIdFilter);
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
			gameEventEmitter.emit(new InventoryChangedEvent(inventory.getId()));
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
				slot.setItemType(jacksonService.serialize(stack.getItemType()));
				slot.setQuantity(stack.getSize());
				slot.insert(connection);
			}
			gameEventEmitter.emit(new InventoryChangedEvent(inventory.getId()));
			return inventory.getId();
		}
	}

}
