package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionExecution;
import name.martingeisse.trading_game.game.action.ProgressSnapshot;

/**
 * Base class for an action that has a fixed effort (required progress points) to complete.
 */
public abstract class FixedEffortAction implements Action {

	/**
	 * @return the total number of progress points needed for this action.
	 */
	public abstract int getTotalRequiredProgressPoints();

	/**
	 * @return the number of progress points obtained per game step (default is 1)
	 */
	public int getProgressPointsPerStep() {
		return 1;
	}

	@Override
	public final Integer getTotalTime() {
		return getTotalRequiredProgressPoints() / getProgressPointsPerStep();
	}

	// narrow down return type
	@Override
	public abstract AbstractExecution startExecution();

	/**
	 * Base execution implementation.
	 */
	public abstract class AbstractExecution implements ActionExecution {

		private int currentProgressPoints;

		@Override
		public final ProgressSnapshot getProgress() {
			return new ProgressSnapshot(getTotalRequiredProgressPoints(), currentProgressPoints);
		}

		public final Integer getRemainingProgressPoints() {
			return getTotalRequiredProgressPoints() - currentProgressPoints;
		}

		@Override
		public final Integer getRemainingTime() {
			return getRemainingProgressPoints() / getProgressPointsPerStep();
		}

		@Override
		public void tick() {
			currentProgressPoints += getProgressPointsPerStep();
		}

		@Override
		public final boolean isFinishable() {
			return currentProgressPoints >= getTotalRequiredProgressPoints();
		}

		@Override
		public final void finish() {
			if (!isFinishable()) {
				throw new IllegalStateException("this action cannot be finished");
			}
			onFinish();
		}

		/**
		 * Called when finishing the action successfully.
		 */
		protected abstract void onFinish();

		@Override
		public String toString() {
			return getName() + " (" + currentProgressPoints + " / " + getTotalRequiredProgressPoints() + ")";
		}

	}

}
