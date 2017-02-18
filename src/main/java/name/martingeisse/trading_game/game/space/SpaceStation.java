package name.martingeisse.trading_game.game.space;

import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.item.InventoryProvider;

/**
 *
 */
public final class SpaceStation extends StaticSpaceObject {

	private InventoryProvider inventoryProvider;
	private long inventoryId;

	public SpaceStation(InventoryProvider inventoryProvider) {
		this.inventoryProvider = inventoryProvider;
	}

	/**
	 * Setter method.
	 *
	 * @param inventoryId the inventoryId
	 */
	void internalSetInventoryId(long inventoryId) {
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

	/**
	 * Getter method.
	 *
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventoryProvider.getInventory(inventoryId);
	}

}
