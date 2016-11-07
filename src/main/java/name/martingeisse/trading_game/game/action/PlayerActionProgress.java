package name.martingeisse.trading_game.game.action;

/**
 * Progress is measured in "progress points". The player's equipment, buffs etc. defines how fast these are accumulated;
 * the action defines how many points are needed to complete it.
 */
public final class PlayerActionProgress {

	private final PlayerAction action;
	private int progressPoints;

	public PlayerActionProgress(PlayerAction action) {
		this.action = action;
		this.progressPoints = 0;
	}

	/**
	 * Getter method.
	 *
	 * @return the action
	 */
	public PlayerAction getAction() {
		return action;
	}

	/**
	 * Getter method.
	 *
	 * @return the progressPoints
	 */
	public int getProgressPoints() {
		return progressPoints;
	}

	public void advance(int progressPoints) {
		this.progressPoints += progressPoints;
	}

	public boolean isFinishable() {
		return progressPoints >= action.getRequiredProgressPoints();
	}

	public void finish() {
		if (!isFinishable()) {
			throw new IllegalStateException("this action cannot be finished");
		}
		action.finish();
	}

}
