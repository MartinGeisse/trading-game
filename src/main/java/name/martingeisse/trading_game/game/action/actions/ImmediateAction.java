package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.CannotStartActionException;
import name.martingeisse.trading_game.game.action.ProgressSnapshot;
import name.martingeisse.trading_game.game.action.Action;

/**
 * Base class for an action that does not take any time but happens immediately, such as loading/unloading items.
 */
public abstract class ImmediateAction implements Action {

	@Override
	public final Integer getTotalTime() {
		return 0;
	}

	@Override
	public final void start() throws CannotStartActionException {
		onExecute();
	}

	/**
	 * Executes this action. This method must not be called if the action has any prerequisite actions as returned by
	 * {@link #getPrerequisite()}.
	 *
	 * @throws CannotStartActionException if this action cannot be started
	 */
	protected abstract void onExecute() throws CannotStartActionException;

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
