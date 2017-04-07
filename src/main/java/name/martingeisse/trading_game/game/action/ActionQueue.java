package name.martingeisse.trading_game.game.action;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.ActionQueueSlotRow;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class ActionQueue {

	private final ActionQueueHelper helper;

	// use ActionQueueRepository to get an instance of this class
	public ActionQueue(PostgresContextService postgresContextService, JacksonService jacksonService, long id) {
		this.helper = new ActionQueueHelper(postgresContextService, jacksonService, id);
	}

	/**
	 * Schedules an action to be performed after all currently pending actions.
	 *
	 * @param action the action to schedule
	 */
	public void scheduleAction(Action action) {
		ParameterUtil.ensureNotNull(action, "action");
		helper.insertSlot(action, false, false);
	}

	/**
	 * Gets all entries in this queue.
	 *
	 * @return all actions
	 */
	public ImmutableList<ActionQueueEntry> getEntries() {
		List<ActionQueueSlotRow> rows;
		rows = helper.fetchAllSlots();
		List<ActionQueueEntry> entries = new ArrayList<>();
		for (ActionQueueSlotRow row : rows) {
			entries.add(new ActionQueueEntry(helper.extractAction(row), row.getPrerequisite()));
		}
		return ImmutableList.copyOf(entries);
	}

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick() {
		ActionQueueSlotRow runningSlot = helper.fetchStartedSlot();
		Action runningAction;
		if (runningSlot == null) {
			ActionStarter starter = new ActionStarter(helper);
			starter.startAction();
			runningSlot = starter.getRunningSlot();
			runningAction = starter.getRunningAction();
		} else {
			runningAction = helper.extractAction(runningSlot);
		}
		if (runningSlot != null) {
			Action.Status status = runningAction.tick();
			if (status != Action.Status.RUNNING) {
				helper.deleteSlot(runningSlot);
			}
		}
	}

	/**
	 * Cancels the currently executed action (if any).
	 */
	public void cancelCurrentAction() {
		ActionQueueSlotRow row = helper.fetchStartedSlot();
		if (row != null) {
			Action action = helper.extractAction(row);
			action.cancel();
			helper.deleteSlot(row);
		}
	}

	/**
	 * Cancels the pending action at the specified index.
	 *
	 * @param index the index of the action to cancel
	 */
	public void cancelPendingAction(int index) {
		ParameterUtil.ensureNotNegative(index, "index");
		ActionQueueSlotRow row = helper.fetchPendingSlot(index);
		if (row != null) {
			helper.deleteSlot(row);
		}
	}

	/**
	 * Cancels all pending actions (but not the current action).
	 */
	public void cancelAllPendingActions() {
		helper.deleteAllPendingSlots();
	}

}
