package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionExecution;
import name.martingeisse.trading_game.game.action.ProgressSnapshot;

/**
 * Base class for an action that has a continuous effect while it is being performed. This implies:
 * - there is no useful definition of "progress". Even if there is a well-defined point in the future when the action
 * cannot be continued, the lack of a well-defined starting point in the past makes progress a non-concept.
 * - there is typically no state to be kept during execution and no special action to be taken on starting, completing
 * or cancelling the action. These events just define the time period during which the actual effects occur.
 * - continuous, useful effects occur while the action is being performed. The tick() method is thus the most important
 * method of this class.
 * <p>
 * Based on these assumptions, a base implementation of most methods as well as an {@link ActionExecution}
 * implementation are provided by this class.
 */
public abstract class ContinuousAction implements Action {

	/**
	 * This method has been moved from {@link ActionExecution} to the enclosing class, making subclassing of the
	 * enclosing class easier, since the former is typically stateless.
	 */
	protected abstract Integer getRemainingTime();

	@Override
	public final Integer getTotalTime() {
		return getRemainingTime();
	}

	@Override
	public ActionExecution startExecution() {
		return new Execution();
	}

	/**
	 * This method has been moved from {@link ActionExecution} to the enclosing class, making subclassing of the
	 * enclosing class easier, since the former is typically stateless.
	 */
	public abstract boolean isFinishable();

	/**
	 * This method has been moved from {@link ActionExecution} to the enclosing class, making subclassing of the
	 * enclosing class easier, since the former is typically stateless.
	 */
	public abstract void tick();

	/**
	 * Execution base class. This class itself is stateless and delegates methods to the enclosing class since most
	 * subclasses (if any) are stateless as well.
	 */
	public class Execution implements ActionExecution {

		@Override
		public String getName() {
			return ContinuousAction.this.toString();
		}

		@Override
		public ProgressSnapshot getProgress() {
			return null;
		}

		@Override
		public Integer getRemainingTime() {
			return ContinuousAction.this.getRemainingTime();
		}

		@Override
		public void cancel() {
		}

		@Override
		public boolean isFinishable() {
			return ContinuousAction.this.isFinishable();
		}

		@Override
		public void finish() {
		}

		@Override
		public void tick() {
			ContinuousAction.this.tick();
		}
	}

}
