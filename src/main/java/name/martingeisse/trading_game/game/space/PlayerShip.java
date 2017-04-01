package name.martingeisse.trading_game.game.space;

import com.google.inject.Provider;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.item.InventoryRepository;
import name.martingeisse.trading_game.game.item.ObjectWithInventory;
import name.martingeisse.trading_game.game.player.PlayerRepository;

/**
 *
 */
public final class PlayerShip extends DynamicSpaceObject implements ObjectWithInventory {

	private final InventoryRepository inventoryRepository;
	private final Provider<PlayerRepository> playerRepositoryProvider;
	private long inventoryId;

	public PlayerShip(InventoryRepository inventoryRepository, Provider<PlayerRepository> playerRepositoryProvider) {
		this.inventoryRepository = inventoryRepository;
		this.playerRepositoryProvider = playerRepositoryProvider;
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
		// TODO see InventoryNameService for a long discussion; here's another example of how we can't seem to abstract from the database
		long playerId = playerRepositoryProvider.get().getPlayerByShipId(getId()).getId();
		return inventoryRepository.getInventory(getInventoryId(), playerId);
	}

	@Override
	public long getRadius() {
		return 500;
	}

}
