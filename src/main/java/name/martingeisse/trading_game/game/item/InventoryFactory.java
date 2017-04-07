package name.martingeisse.trading_game.game.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.InventoryRow;
import name.martingeisse.trading_game.postgres_entities.InventorySlotRow;

/**
 *
 */
@Singleton
public class InventoryFactory {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;
	private final GameEventEmitter gameEventEmitter;

	@Inject
	public InventoryFactory(PostgresContextService postgresContextService, JacksonService jacksonService, GameEventEmitter gameEventEmitter) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
		this.gameEventEmitter = gameEventEmitter;
	}

	/**
	 * Creates a new, empty inventory
	 *
	 * @return the ID of the new inventory
	 */
	public long createInventory() {
		InventoryRow inventory = new InventoryRow();
		inventory.insert(postgresContextService.getConnection());
		gameEventEmitter.emit(new InventoryChangedEvent(inventory.getId()));
		return inventory.getId();
	}

	/**
	 * Creates a new inventory with the specified contents
	 *
	 * @return the ID of the new inventory
	 */
	public long createInventory(ImmutableItemStacks itemStacks) {
		InventoryRow inventory = new InventoryRow();
		inventory.insert(postgresContextService.getConnection());
		for (ImmutableItemStack stack : itemStacks.getStacks()) {
			InventorySlotRow slot = new InventorySlotRow();
			slot.setInventoryId(inventory.getId());
			slot.setItemType(jacksonService.serialize(stack.getItemType()));
			slot.setQuantity(stack.getSize());
			slot.insert(postgresContextService.getConnection());
		}
		gameEventEmitter.emit(new InventoryChangedEvent(inventory.getId()));
		return inventory.getId();
	}

}
