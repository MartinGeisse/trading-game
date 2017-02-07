package name.martingeisse.trading_game.game.space;

import name.martingeisse.trading_game.game.item.Inventory;

/**
 *
 */
public final class SpaceStation extends StaticSpaceObject {

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
