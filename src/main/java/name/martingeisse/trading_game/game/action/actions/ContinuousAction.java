package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ProgressSnapshot;

/**
 * Base class for an action that has a continuous effect while it is being performed. This implies:
 * - there is no useful definition of "progress". Even if there is a well-defined point in the future when the action
 * cannot be continued, the lack of a well-defined starting point in the past makes progress a non-concept.
 * - there is typically no state to be kept during execution and no special action to be taken on starting, completing
 * or cancelling the action. These events just define the time period during which the actual effects occur.
 * - continuous, useful effects occur while the action is being performed. The tick() method is thus the most important
 * method of this class.
 */
public abstract class ContinuousAction implements Action {

	@Override
	public Integer getTotalTime() {
		return getRemainingTime();
	}

	@Override
	public Status start() {
		return Status.RUNNING;
	}

	@Override
	public ProgressSnapshot getProgress() {
		return null;
	}

	@Override
	public void cancel() {
	}

}
