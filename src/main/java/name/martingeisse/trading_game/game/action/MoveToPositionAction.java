package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.game.Player;

/**
 *
 */
public final class MoveToPositionAction extends PlayerAction {

	private final long x;
	private final long y;

	/**
	 *
	 */
	public MoveToPositionAction(Player player, long x, long y) {
		super(player, 1000);
		this.x = x;
		this.y = y;
	}

	@Override
	public void onFinish() {

	}

	@Override
	public String toString() {
		return "move to " + x + ", " + y;
	}

}
