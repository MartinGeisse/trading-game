package name.martingeisse.trading_game.game.space;

import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.item.InventoryRepository;
import name.martingeisse.trading_game.game.item.ObjectWithInventory;

/**
 *
 */
public final class PlayerShip extends DynamicSpaceObject implements ObjectWithInventory {

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
	@Override
	public long getInventoryId() {
		if (inventoryId < 1) {
			throw new IllegalStateException("inventoryId not initialized");
		}
		return inventoryId;
	}

	/**
	 * Getter method.
	 *
	 * @return the inventory
	 */
	@Override
	public Inventory getInventory() {
		return inventoryRepository.getInventory(getInventoryId());
	}

}
