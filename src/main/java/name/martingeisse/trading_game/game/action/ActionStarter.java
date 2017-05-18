package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.common.util.SwitchCaseWtfException;
import name.martingeisse.trading_game.postgres_entities.ActionQueueSlotRow;

/**
 * Helper class to start an action and/or its prerequisite actions.
 */
final class ActionStarter {

	private final ActionQueueHelper helper;
	private boolean prerequisiteSuccess;
	private boolean prerequisiteFailure;
	private ActionQueueSlotRow runningSlot;
	private Action runningAction;

	ActionStarter(ActionQueueHelper helper) {
		this.helper = helper;
	}

	void startAction() {
		runningSlot = null;
		runningAction = null;
		while (true) {
			ActionQueueSlotRow slot = helper.fetchPendingSlot(0);
			if (slot == null) {
				// queue is empty
				return;
			}
			Action action = helper.extractAction(slot);
			prerequisiteSuccess = prerequisiteFailure = false;
			startPrerequisiteFor(action);
			if (prerequisiteSuccess) {
				// a prerequisite was started and is running now
				break;
			} else if (prerequisiteFailure) {
				// a prerequisite failed, so we cannot execute the action itself -- remove it and skip to the next one
				helper.deleteSlot(slot);
			} else {
				// no prerequisite found, so start the action itself
				Action.Status status = action.start();
				if (status == Action.Status.RUNNING) {
					// the action was successfully started
					runningSlot = slot;
					runningAction = action;
					return;
				} else {
					// failed or finished, so continue with the next action
					helper.deleteSlot(slot);
				}
			}
		}
	}

	/**
	 * Starts the prerequisite for the specified action, if any such prerequisite exists. Sets the prerequisiteSuccess flag if
	 * a prerequisite was started and is running. Sets the prerequisiteFailure flag if starting a prerequisite failed. Doesn't
	 * set any flags if no prerequisite was found, or if all prerequisites finished instantly. In the latter case,
	 * the caller can continue starting actions.
	 * <p>
	 * Starting a prerequisite is different from starting a scheduled action since prerequisites are not yet backed
	 * by a queue slot when this method gets called.
	 */
	private void startPrerequisiteFor(Action action) {
		Action prerequisite = action.getPrerequisite();
		if (prerequisite == null) {
			// no prerequisite
			return;
		}
		startPrerequisiteFor(prerequisite);
		if (prerequisiteSuccess || prerequisiteFailure) {
			// A nested prerequisite was started and is running, or starting it failed. Either way, we cannot start the
			// current prerequisite now.
			return;
		}
		// Any nested prerequisites are finished, so we can start the current prerequisite
		Action.Status status = prerequisite.start();
		switch (status) {

			case RUNNING:
				// the current prerequisite was started successfully and is running now, so add it to the queue
				prerequisiteSuccess = true;
				runningSlot = helper.insertSlot(prerequisite, true, true);
				runningAction = prerequisite;
				break;

			case FINISHED:
				// the current prerequisite was started and finished instantly, so let the caller continue starting other actions
				break;

			case FAILED:
				// prerequisite failed, so the actual action cannot be started
				prerequisiteFailure = true;
				break;

			default:
				throw new SwitchCaseWtfException();

		}
	}

	/**
	 * Getter method.
	 *
	 * @return the runningSlot
	 */
	ActionQueueSlotRow getRunningSlot() {
		return runningSlot;
	}

	/**
	 * Getter method.
	 *
	 * @return the runningAction
	 */
	Action getRunningAction() {
		return runningAction;
	}

}
