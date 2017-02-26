package name.martingeisse.trading_game.game.action;

import com.querydsl.sql.postgresql.PostgreSQLQuery;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.postgres_entities.ActionQueueSlotRow;
import name.martingeisse.trading_game.postgres_entities.QActionQueueSlotRow;
import name.martingeisse.trading_game.tools.codegen.PostgresJsonb;

import java.util.List;

/**
 * Contains simple helper methods for {@link ActionQueue} to keep that class more readable.
 */
final class ActionQueueHelper {

	private static final QActionQueueSlotRow qaqs = QActionQueueSlotRow.ActionQueueSlot;

	private final ActionSerializer actionSerializer;
	private final long id;

	ActionQueueHelper(ActionSerializer actionSerializer, long id) {
		this.actionSerializer = actionSerializer;
		this.id = id;
	}

	Action extractAction(ActionQueueSlotRow row) {
		return actionSerializer.deserializeAction(row.getAction().getValue());
	}

	ActionQueueSlotRow insertSlot(PostgresConnection connection, Action action, boolean prerequisite, boolean started) {
		ActionQueueSlotRow row = new ActionQueueSlotRow();
		row.setActionQueueId(id);
		row.setAction(new PostgresJsonb(actionSerializer.serializeAction(action)));
		row.setPrerequisite(prerequisite);
		row.setStarted(started);
		row.insert(connection);
		return row;
	}

	private PostgreSQLQuery<ActionQueueSlotRow> slotsQuery(PostgresConnection connection) {
		return connection.query().select(qaqs).from(qaqs).where(qaqs.actionQueueId.eq(id)).orderBy(qaqs.prerequisite.desc(), qaqs.id.asc());
	}

	List<ActionQueueSlotRow> fetchAllSlots(PostgresConnection connection) {
		return slotsQuery(connection).fetch();
	}

	ActionQueueSlotRow fetchPendingSlot(PostgresConnection connection, int index) {
		return slotsQuery(connection).where(qaqs.started.isFalse()).limit(1).offset(index).fetchFirst();
	}

	ActionQueueSlotRow fetchStartedSlot(PostgresConnection connection) {
		return connection.query().select(qaqs).from(qaqs).where(qaqs.actionQueueId.eq(id)).where(qaqs.started.isTrue()).fetchFirst();
	}

	void deleteSlot(PostgresConnection connection, ActionQueueSlotRow row) {
		deleteSlot(connection, row.getId());
	}

	void deleteSlot(PostgresConnection connection, long id) {
		connection.delete(qaqs).where(qaqs.id.eq(id)).execute();
	}

	void deleteAllPendingSlots(PostgresConnection connection) {
		connection.delete(qaqs).where(qaqs.actionQueueId.eq(id), qaqs.started.isFalse()).execute();
	}

}
