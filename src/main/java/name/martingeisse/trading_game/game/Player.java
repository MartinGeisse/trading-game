package name.martingeisse.trading_game.game;

import name.martingeisse.trading_game.game.action.PlayerAction;
import name.martingeisse.trading_game.game.action.PlayerActionProgress;
import name.martingeisse.trading_game.game.item.Inventory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 */
public final class Player {

	private final Inventory inventory = new Inventory();
	private final List<PlayerAction> pendingActions = new ArrayList<>();
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
	 * Getter method.
	 *
	 * @return the pendingActions
	 */
	public List<PlayerAction> getPendingActions() {
		return pendingActions;
	}

	/**
	 * Getter method.
	 *
	 * @return the actionProgress
	 */
	public PlayerActionProgress getActionProgress() {
		return actionProgress;
	}

	/**
	 * Schedules an action to be performed after all currently pending actions.
	 *
	 * @param action the action to schedule
	 */
	public void scheduleAction(PlayerAction action) {
		pendingActions.add(action);
	}

	/**
	 * Advances game logic.
	 */
	void tick() {
		if (actionProgress == null && !pendingActions.isEmpty()) {
			actionProgress = new PlayerActionProgress(pendingActions.remove(0));
		}
		if (actionProgress != null) {
			actionProgress.advance(1);
			if (actionProgress.isFinishable()) {
				actionProgress.finish();
				actionProgress = null;
			}
		}
	}

}
