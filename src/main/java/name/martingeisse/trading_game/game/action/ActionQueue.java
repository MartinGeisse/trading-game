package name.martingeisse.trading_game.game.action;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.ActionQueueSlotRow;
import name.martingeisse.trading_game.postgres_entities.QActionQueueRow;
import name.martingeisse.trading_game.postgres_entities.QActionQueueSlotRow;
import name.martingeisse.trading_game.tools.codegen.PostgresJsonb;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO move all the database handling to a separate class (maybe a per-run class that takes the connection and
 * implements closeable), so this class contains visible logic steps
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

	/**
	 * Schedules an action to be performed after all currently pending actions.
	 *
	 * @param action the action to schedule
	 */
	public void scheduleAction(Action action) {
		ActionQueueSlotRow row = new ActionQueueSlotRow();
		row.setActionQueueId(id);
		row.setAction(new PostgresJsonb(actionSerializer.serializeAction(action)));
		row.setPrerequisite(false);
		row.setStarted(false);
		try (PostgresConnection connection = postgresService.newConnection()) {
			row.insert(connection);
		}
	}

	/**
	 * Gets all actions in this queue.
	 * <p>
	 * TODO currently cannot return the 'prerequisite' flag
	 *
	 * @return all actions
	 */
	public ImmutableList<Action> getAll() {
		List<ActionQueueSlotRow> rows;
		try (PostgresConnection connection = postgresService.newConnection()) {
			QActionQueueSlotRow qaqs = QActionQueueSlotRow.ActionQueueSlot;
			rows = connection.query().select(qaqs).from(qaqs).where(qaqs.actionQueueId.eq(id)).orderBy(qaqs.prerequisite.desc(), qaqs.id.asc()).fetch();
		}
		List<Action> actions = new ArrayList<>();
		for (ActionQueueSlotRow row : rows) {
			actions.add(actionSerializer.deserializeAction(row.getAction().getValue()));
		}
		return ImmutableList.copyOf(actions);
	}






	// TODO old TODO tick / start next / tick action / finish
	public void tick(PostgresConnection connection) {
		// TODO
		/*
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
		*/
	}

	// TODO old
	/*
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
	*/







	/**
	 * Cancels the currently executed action (if any).
	 */
	public void cancelCurrentAction() {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QActionQueueSlotRow qaqs = QActionQueueSlotRow.ActionQueueSlot;
			ActionQueueSlotRow row = connection.query().select(qaqs).from(qaqs).where(qaqs.actionQueueId.eq(id), qaqs.started.isTrue()).fetchFirst();
			Action action = actionSerializer.deserializeAction(row.getAction().getValue());
			action.cancel();
			connection.delete(qaqs).where(qaqs.id.eq(row.getId())).execute();
		}
	}

	/**
	 * Cancels the pending action at the specified index.
	 *
	 * @param index the index of the action to cancel
	 */
	public void cancelPendingAction(int index) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QActionQueueSlotRow qaqs = QActionQueueSlotRow.ActionQueueSlot;
			Long slotId = connection.query().select(qaqs.id).from(qaqs).where(qaqs.actionQueueId.eq(id), qaqs.started.isFalse()).orderBy(qaqs.prerequisite.desc(), qaqs.id.asc()).limit(1).offset(index).fetchFirst();
			if (slotId != null) {
				connection.delete(qaqs).where(qaqs.id.eq(slotId)).execute();
			}
		}
	}

	/**
	 * Cancels all pending actions (but not the current action).
	 */
	public void cancelAllPendingActions() {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QActionQueueSlotRow qaqs = QActionQueueSlotRow.ActionQueueSlot;
			connection.delete(qaqs).where(qaqs.actionQueueId.eq(id), qaqs.started.isFalse()).execute();
		}
	}

}
