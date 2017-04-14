package name.martingeisse.trading_game.game.item;

import name.martingeisse.trading_game.game.event.GameEvent;

/**
 * Indicates that an inventory in the database has changed.
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
