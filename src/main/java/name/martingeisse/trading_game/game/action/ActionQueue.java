package name.martingeisse.trading_game.game.action;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.ActionQueueSlotRow;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class ActionQueue {

	private final PostgresService postgresService;
	private final ActionSerializer actionSerializer;
	private final long id;
	private final ActionQueueHelper helper;

	// use ActionQueueRepository to get an instance of this class
	ActionQueue(PostgresService postgresService, ActionSerializer actionSerializer, long id) {
		this.postgresService = ParameterUtil.ensureNotNull(postgresService, "postgresService");;
		this.actionSerializer = ParameterUtil.ensureNotNull(actionSerializer, "actionSerializer");;
		this.id = ParameterUtil.ensurePositive(id, "id");;
		this.helper = new ActionQueueHelper(actionSerializer, id);
	}

	/**
	 * Schedules an action to be performed after all currently pending actions.
	 *
	 * @param action the action to schedule
	 */
	public void scheduleAction(Action action) {
		ParameterUtil.ensureNotNull(action, "action");
		try (PostgresConnection connection = postgresService.newConnection()) {
			helper.insertSlot(connection, action, false, false);
		}
	}

	/**
	 * Gets all entries in this queue.
	 *
	 * @return all actions
	 */
	public ImmutableList<ActionQueueEntry> getEntries() {
		List<ActionQueueSlotRow> rows;
		try (PostgresConnection connection = postgresService.newConnection()) {
			rows = helper.fetchAllSlots(connection);
		}
		List<ActionQueueEntry> entries = new ArrayList<>();
		for (ActionQueueSlotRow row : rows) {
			entries.add(new ActionQueueEntry(helper.extractAction(row), row.getPrerequisite()));
		}
		return ImmutableList.copyOf(entries);
	}

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick(PostgresConnection connection) {
		ParameterUtil.ensureNotNull(connection, "connection");
		ActionQueueSlotRow runningSlot = helper.fetchStartedSlot(connection);
		Action runningAction;
		if (runningSlot == null) {
			ActionStarter starter = new ActionStarter(connection, helper);
			starter.startAction();
			runningSlot = starter.getRunningSlot();
			runningAction = starter.getRunningAction();
		} else {
			runningAction = helper.extractAction(runningSlot);
		}
		if (runningSlot != null) {
			Action.Status status = runningAction.tick();
			if (status != Action.Status.RUNNING) {
				helper.deleteSlot(connection, runningSlot);
			}
		}
	}

	/**
	 * Cancels the currently executed action (if any).
	 */
	public void cancelCurrentAction() {
		try (PostgresConnection connection = postgresService.newConnection()) {
			ActionQueueSlotRow row = helper.fetchStartedSlot(connection);
			if (row != null) {
				Action action = helper.extractAction(row);
				action.cancel();
				helper.deleteSlot(connection, row);
			}
		}
	}

	/**
	 * Cancels the pending action at the specified index.
	 *
	 * @param index the index of the action to cancel
	 */
	public void cancelPendingAction(int index) {
		ParameterUtil.ensureNotNegative(index, "index");
		try (PostgresConnection connection = postgresService.newConnection()) {
			ActionQueueSlotRow row = helper.fetchPendingSlot(connection, index);
			if (row != null) {
				helper.deleteSlot(connection, row);
			}
		}
	}

	/**
	 * Cancels all pending actions (but not the current action).
	 */
	public void cancelAllPendingActions() {
		try (PostgresConnection connection = postgresService.newConnection()) {
			helper.deleteAllPendingSlots(connection);
		}
	}

}
