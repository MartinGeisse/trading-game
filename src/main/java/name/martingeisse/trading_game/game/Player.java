package name.martingeisse.trading_game.game;

import name.martingeisse.trading_game.game.action.PlayerAction;
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
	 * Starts a new action, interrupting whatever action was in progress.
	 *
	 * @param action the action to start
	 */
	public void startAction(PlayerAction action) {
		actionProgress = new PlayerActionProgress(action);
	}

	/**
	 * Advances game logic.
	 */
	void tick() {
		if (actionProgress != null) {
			actionProgress.advance(1);
			if (actionProgress.isFinishable()) {
				actionProgress.finish();
				actionProgress = null;
			}
		}
	}

}
