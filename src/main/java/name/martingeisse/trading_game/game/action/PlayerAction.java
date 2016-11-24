package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.game.Player;

/**
 *
 */
public abstract class PlayerAction {

	private final Player player;
	private final int requiredProgressPoints;

	public PlayerAction(Player player, int requiredProgressPoints) {
		this.player = player;
		this.requiredProgressPoints = requiredProgressPoints;
	}

	/**
	 * Getter method.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Getter method.
	 *
	 * @return the requiredProgressPoints
	 */
	public int getRequiredProgressPoints() {
		return requiredProgressPoints;
	}

	/**
	 * Called when the player starts performing the action.
	 *
	 * @return true to proceed, false to fail the action immediately
	 */
	public boolean onStart() {
		return true;
	}

	/**
	 * Called when the player cancels the action.
	 */
	public void onCancel() {
	}

	/**
	 * Called when the player finishes the action.
	 */
	public abstract void onFinish();

}
