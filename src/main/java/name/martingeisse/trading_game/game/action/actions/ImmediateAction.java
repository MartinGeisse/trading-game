package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ProgressSnapshot;

/**
 * Base class for an action that does not take any time but happens immediately, such as loading/unloading items.
 */
public abstract class ImmediateAction implements Action {

	@Override
	public final Integer getTotalTime() {
		return 0;
	}

	@Override
	public final Status start() {
		return onExecute() ? Status.FINISHED : Status.FAILED;
	}

	/**
	 * Executes this action. This method must not be called if the action has any prerequisite actions as returned by
	 * {@link #getPrerequisite()}.
	 *
	 * @return whether the action was successful
	 */
	protected abstract boolean onExecute();

	@Override
	public final ProgressSnapshot getProgress() {
		return null;
	}

	@Override
	public final Integer getRemainingTime() {
		return 0;
	}

	@Override
	public final void cancel() {
	}

	@Override
	public Status tick() {
		return Status.FINISHED;
	}

}
