package name.martingeisse.trading_game.game.space;

import name.martingeisse.trading_game.game.item.Inventory;

/**
 *
 */
public final class PlayerShip extends SpaceObject {

	private final Inventory inventory = new Inventory();

	/**
	 * Getter method.
	 *
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

}
