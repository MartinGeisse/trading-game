package name.martingeisse.trading_game.game.action;

/**
 * Represents the current progress of an action. This object is immutable, so it won't change when progress advances.
 */
public final class ProgressSnapshot {

	private final int requiredProgressPoints;
	private final int currentProgressPoints;

	public ProgressSnapshot(int requiredProgressPoints, int currentProgressPoints) {
		this.requiredProgressPoints = requiredProgressPoints;
		this.currentProgressPoints = currentProgressPoints;
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
	 * Getter method.
	 *
	 * @return the currentProgressPoints
	 */
	public int getCurrentProgressPoints() {
		return currentProgressPoints;
	}

	@Override
	public String toString() {
		return currentProgressPoints + " / " + requiredProgressPoints;
	}

}
