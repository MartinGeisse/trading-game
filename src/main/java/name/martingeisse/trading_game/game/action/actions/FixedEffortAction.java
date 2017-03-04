package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ProgressSnapshot;

/**
 * Base class for an action that has a fixed effort (required progress points) to complete.
 */
public abstract class FixedEffortAction implements Action {

	private int currentProgressPoints;

	/**
	 * @return the total number of progress points needed for this action.
	 */
	public abstract int getTotalRequiredProgressPoints();

	/**
	 * Getter method.
	 *
	 * @return the currentProgressPoints
	 */
	public int getCurrentProgressPoints() {
		return currentProgressPoints;
	}

	/**
	 * @return the number of progress points obtained per second (default is 1; may be modified by upgrades)
	 */
	public int getProgressPointsPerSecond() {
		return 1;
	}

	@Override
	public final Integer getTotalTime() {
		return getTotalRequiredProgressPoints() / getProgressPointsPerSecond();
	}

	@Override
	public final Status start() {
		return onStart() ? Status.RUNNING : Status.FAILED;
	}

	@Override
	public final ProgressSnapshot getProgress() {
		return new ProgressSnapshot(getTotalRequiredProgressPoints(), currentProgressPoints);
	}

	public final Integer getRemainingProgressPoints() {
		return getTotalRequiredProgressPoints() - currentProgressPoints;
	}

	@Override
	public final Integer getRemainingTime() {
		return getRemainingProgressPoints() / getProgressPointsPerSecond();
	}

	@Override
	public final void cancel() {
		currentProgressPoints = 0;
		onCancel();
	}

	@Override
	public Status tick() {
		currentProgressPoints += getProgressPointsPerSecond();
		if (currentProgressPoints >= getTotalRequiredProgressPoints()) {
			return onFinish() ? Status.FINISHED : Status.FAILED;
		} else {
			return Status.RUNNING;
		}
	}

	/**
	 * Called when startingthe action.
	 *
	 * @return whether starting the action was successful
	 */
	protected abstract boolean onStart();

	/**
	 * Called when cancelling the action.
	 */
	protected abstract void onCancel();

	/**
	 * Called when finishing the action successfully.
	 *
	 * @return whether finishing the action was successful
	 */
	protected abstract boolean onFinish();

}
