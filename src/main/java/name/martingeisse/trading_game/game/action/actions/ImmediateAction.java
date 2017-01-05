package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionExecution;
import name.martingeisse.trading_game.game.action.ProgressSnapshot;

/**
 * Base class for an action that does not take any time but happens immediately, such as loading/unloading items.
 */
public abstract class ImmediateAction implements Action {

	@Override
	public final Integer getTotalTime() {
		return 0;
	}

	// narrow down return type
	@Override
	public Execution startExecution() {
		return new Execution();
	}

	/**
	 * Performs the action.
	 *
	 * @param execution the execution object, in case data needs to be transferred from startExecution().
	 */
	protected abstract void onExecute(Execution execution);

	/**
	 * Cancels the action. The default implementation does nothing.
	 *
	 * @param execution the execution object, in case data needs to be transferred from startExecution().
	 */
	protected void onCancel(Execution execution) {
	}

	/**
	 * Execution implementation. The only need to create a subclass from this class is to transfer data from
	 * startExecution() to onExecute().
	 */
	public class Execution implements ActionExecution {

		@Override
		public final String getName() {
			return ImmediateAction.this.toString();
		}

		@Override
		public final ProgressSnapshot getProgress() {
			return null;
		}

		@Override
		public final Integer getRemainingTime() {
			return 0;
		}

		@Override
		public final void tick() {
		}

		@Override
		public final void cancel() {
			onCancel(this);
		}

		@Override
		public final boolean isFinishable() {
			return true;
		}

		@Override
		public final void finish() {
			onExecute(this);
		}

		@Override
		public String toString() {
			return getName();
		}

	}

}
