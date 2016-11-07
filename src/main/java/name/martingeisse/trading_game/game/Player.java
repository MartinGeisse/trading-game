package name.martingeisse.trading_game.game;

import name.martingeisse.trading_game.game.action.PlayerActionProgress;

/**
 *
 */
public final class Player {

	private int x;
	private int y;
	private PlayerActionProgress actionProgress;

	/**
	 * Getter method.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter method.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
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
