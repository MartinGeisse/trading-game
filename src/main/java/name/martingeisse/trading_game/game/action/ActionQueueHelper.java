package name.martingeisse.trading_game.game.action;

import com.querydsl.sql.postgresql.PostgreSQLQuery;
import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.game.jackson.JacksonService;
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

	private final JacksonService jacksonService;
	private final long id;

	ActionQueueHelper(JacksonService jacksonService, long id) {
		this.jacksonService = ParameterUtil.ensureNotNull(jacksonService, "jacksonService");
		this.id = ParameterUtil.ensurePositive(id, "id");
	}

	Action extractAction(ActionQueueSlotRow row) {
		ParameterUtil.ensureNotNull(row, "row");
		return jacksonService.deserialize(row.getAction().getValue(), Action.class);
	}

	ActionQueueSlotRow insertSlot(PostgresConnection connection, Action action, boolean prerequisite, boolean started) {
		ParameterUtil.ensureNotNull(connection, "connection");
		ParameterUtil.ensureNotNull(action, "action");
		ActionQueueSlotRow row = new ActionQueueSlotRow();
		row.setActionQueueId(id);
		row.setAction(new PostgresJsonb(jacksonService.serialize(action)));
		row.setPrerequisite(prerequisite);
		row.setStarted(started);
		row.insert(connection);
		return row;
	}

	private PostgreSQLQuery<ActionQueueSlotRow> slotsQuery(PostgresConnection connection) {
		ParameterUtil.ensureNotNull(connection, "connection");
		return connection.query().select(qaqs).from(qaqs).where(qaqs.actionQueueId.eq(id)).orderBy(qaqs.prerequisite.desc(), qaqs.id.asc());
	}

	List<ActionQueueSlotRow> fetchAllSlots(PostgresConnection connection) {
		ParameterUtil.ensureNotNull(connection, "connection");
		return slotsQuery(connection).fetch();
	}

	ActionQueueSlotRow fetchPendingSlot(PostgresConnection connection, int index) {
		ParameterUtil.ensureNotNull(connection, "connection");
		ParameterUtil.ensureNotNegative(index, "index");
		return slotsQuery(connection).where(qaqs.started.isFalse()).limit(1).offset(index).fetchFirst();
	}

	ActionQueueSlotRow fetchStartedSlot(PostgresConnection connection) {
		ParameterUtil.ensureNotNull(connection, "connection");
		return connection.query().select(qaqs).from(qaqs).where(qaqs.actionQueueId.eq(id)).where(qaqs.started.isTrue()).fetchFirst();
	}

	void deleteSlot(PostgresConnection connection, ActionQueueSlotRow row) {
		ParameterUtil.ensureNotNull(connection, "connection");
		ParameterUtil.ensureNotNull(row, "row");
		deleteSlot(connection, row.getId());
	}

	void deleteSlot(PostgresConnection connection, long id) {
		ParameterUtil.ensureNotNull(connection, "connection");
		ParameterUtil.ensurePositive(id, "id");
		connection.delete(qaqs).where(qaqs.id.eq(id)).execute();
	}

	void deleteAllPendingSlots(PostgresConnection connection) {
		ParameterUtil.ensureNotNull(connection, "connection");
		connection.delete(qaqs).where(qaqs.actionQueueId.eq(id), qaqs.started.isFalse()).execute();
	}

}
