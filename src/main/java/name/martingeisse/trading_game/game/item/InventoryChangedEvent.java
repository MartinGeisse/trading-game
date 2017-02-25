package name.martingeisse.trading_game.game.item;

import name.martingeisse.trading_game.game.event.DatabaseRowEvent;
import name.martingeisse.trading_game.game.event.GameEvent;
import name.martingeisse.trading_game.postgres_entities.InventoryRow;
import name.martingeisse.trading_game.postgres_entities.InventorySlotRow;

/**
 * Indicates that an inventory in the database has changed. In contrast to a {@link DatabaseRowEvent}, this event
 * affects both the {@link InventoryRow} and {@link InventorySlotRow} tables.
 *
 * TODO should this be some kind of sub-event system, such that this event returns DB row events for those two tables?
 */
public final class InventoryChangedEvent extends GameEvent {

	private final long inventoryId;

	/**
	 * Constructor.
	 *
	 * @param inventoryId the ID of the inventory that has changed
	 */
	public InventoryChangedEvent(long inventoryId) {
		this.inventoryId = inventoryId;
	}

	/**
	 * Getter method.
	 *
	 * @return the inventoryId
	 */
	public long getInventoryId() {
		return inventoryId;
	}

}
