package name.martingeisse.trading_game.game;

import name.martingeisse.trading_game.game.action.PlayerActionProgress;
import name.martingeisse.trading_game.game.item.Inventory;

/**
 *
 */
public final class Player {

	private final Inventory inventory = new Inventory();
	private PlayerActionProgress actionProgress;

	/**
	 * Getter method.
	 *
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 *
	 */
	void tick() {
		if (actionProgress != null) {
			actionProgress.advance(1);
		}
	}

}
