package name.martingeisse.trading_game.game.space;

import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.item.InventoryRepository;

/**
 *
 */
public final class PlayerShip extends DynamicSpaceObject {

	private InventoryRepository inventoryRepository;
	private long inventoryId;

	public PlayerShip(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
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
		return inventoryRepository.getInventory(inventoryId);
	}

}
