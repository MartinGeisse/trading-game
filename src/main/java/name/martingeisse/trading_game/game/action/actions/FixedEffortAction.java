package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.CannotStartActionException;
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
	public final void start() throws CannotStartActionException {
		onStart();
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
			onFinish();
			return Status.FINISHED;
		} else {
			return Status.RUNNING;
		}
	}

	/**
	 * Called when startingthe action.
	 *
	 * @throws CannotStartActionException if this action cannot be started
	 */
	protected abstract void onStart() throws CannotStartActionException;

	/**
	 * Called when cancelling the action.
	 */
	protected abstract void onCancel();

	/**
	 * Called when finishing the action successfully.
	 */
	protected abstract void onFinish();

}
