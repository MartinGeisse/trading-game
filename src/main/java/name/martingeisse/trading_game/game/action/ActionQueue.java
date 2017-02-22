package name.martingeisse.trading_game.game.action;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

/**
 *
 */
public final class ActionQueue {

	private final PostgresService postgresService;
	private final ActionSerializer actionSerializer;
	private final long id;

	// use ActionQueueRepository to get an instance of this class
	ActionQueue(PostgresService postgresService, ActionSerializer actionSerializer, long id) {
		this.postgresService = postgresService;
		this.actionSerializer = actionSerializer;
		this.id = id;
	}

	// TODO insert
	// TODO tick / start next / tick action / finish
	// TODO get all actions (for displaying)
	// TODO cancel current
	// TODO cancel pending




	/**
	 * Schedules an action to be performed after all currently pending actions.
	 *
	 * @param action the action to schedule
	 */
	public void scheduleAction(Action action) {
		pendingActions.add(action);
	}


	public void tick(PostgresConnection connection) {
		while (actionExecution == null && !pendingActions.isEmpty()) {
			actionExecution = pendingActions.startNext();
		}
		if (actionExecution != null) {
			actionExecution.tick();
			if (actionExecution.isFinishable()) {
				actionExecution.finish();
				actionExecution = null;
			}
		}

	}

	// TODO old
	private ActionExecution startNext() {
		if (isEmpty()) {
			return null;
		}
		Action action = get(0);
		Action prerequisite = action.getPrerequisite();
		if (prerequisite != null) {
			return new PrerequisiteActionExecutionDecorator(prerequisite.startExecution());
		}
		remove(0);
		return action.startExecution();
	}

	public ImmutableList<Action> getAll() {
		// TODO
	}

	/**
	 * Cancels the currently executed action (if any).
	 */
	public void cancelCurrentAction() {

		if (actionExecution != null) {
			actionExecution.cancel();
			actionExecution = null;
		}
	}

	/**
	 * Cancels the pending action at the specified index.
	 *
	 * @param index the index of the action to cancel
	 */
	public void cancelPendingAction(int index) {
		if (index >= 0 && index < pendingActions.size()) {
			pendingActions.remove(index);
		}
	}

	/**
	 * Cancels all pending actions (but not the current action).
	 */
	public void cancelAllPendingActions() {
		pendingActions.clear();
	}

}
