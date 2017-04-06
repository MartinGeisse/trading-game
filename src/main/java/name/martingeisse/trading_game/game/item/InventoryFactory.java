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
 *
 */
@Singleton
public class InventoryFactory {

	private final PostgresService postgresService;
	private final JacksonService jacksonService;
	private final GameEventEmitter gameEventEmitter;

	@Inject
	public InventoryFactory(PostgresService postgresService, JacksonService jacksonService, GameEventEmitter gameEventEmitter) {
		this.postgresService = postgresService;
		this.jacksonService = jacksonService;
		this.gameEventEmitter = gameEventEmitter;
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
