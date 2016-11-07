package name.martingeisse.trading_game.game.action;

/**
 *
 */
public final class WalkAction implements PlayerAction {

	private final WalkDirection walkDirection;

	public WalkAction(WalkDirection walkDirection) {
		this.walkDirection = walkDirection;
	}

	@Override
	public int getRequiredProgressPoints() {
		return 5;
	}

	@Override
	public void finish() {
		// TODO
	}

}
