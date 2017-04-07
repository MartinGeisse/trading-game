package name.martingeisse.trading_game.game.action;

import com.querydsl.sql.postgresql.PostgreSQLQuery;
import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.ActionQueueSlotRow;
import name.martingeisse.trading_game.postgres_entities.QActionQueueSlotRow;
import name.martingeisse.trading_game.tools.codegen.PostgresJsonb;

import java.util.List;

/**
 * Contains simple helper methods for {@link ActionQueue} to keep that class more readable.
 */
final class ActionQueueHelper {

	private static final QActionQueueSlotRow qaqs = QActionQueueSlotRow.ActionQueueSlot;

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;
	private final long id;

	ActionQueueHelper(PostgresContextService postgresContextService, JacksonService jacksonService, long id) {
		this.postgresContextService = ParameterUtil.ensureNotNull(postgresContextService, "postgresContextService");
		this.jacksonService = ParameterUtil.ensureNotNull(jacksonService, "jacksonService");
		this.id = ParameterUtil.ensurePositive(id, "id");
	}

	Action extractAction(ActionQueueSlotRow row) {
		ParameterUtil.ensureNotNull(row, "row");
		return jacksonService.deserialize(row.getAction().getValue(), Action.class);
	}

	ActionQueueSlotRow insertSlot(Action action, boolean prerequisite, boolean started) {
		ParameterUtil.ensureNotNull(action, "action");
		ActionQueueSlotRow row = new ActionQueueSlotRow();
		row.setActionQueueId(id);
		row.setAction(new PostgresJsonb(jacksonService.serialize(action)));
		row.setPrerequisite(prerequisite);
		row.setStarted(started);
		row.insert(postgresContextService.getConnection());
		return row;
	}

	private PostgreSQLQuery<ActionQueueSlotRow> slotsQuery() {
		return postgresContextService.select(qaqs).from(qaqs).where(qaqs.actionQueueId.eq(id)).orderBy(qaqs.prerequisite.desc(), qaqs.id.asc());
	}

	List<ActionQueueSlotRow> fetchAllSlots() {
		return slotsQuery().fetch();
	}

	ActionQueueSlotRow fetchPendingSlot(int index) {
		ParameterUtil.ensureNotNegative(index, "index");
		return slotsQuery().where(qaqs.started.isFalse()).limit(1).offset(index).fetchFirst();
	}

	ActionQueueSlotRow fetchStartedSlot() {
		return postgresContextService.select(qaqs).from(qaqs).where(qaqs.actionQueueId.eq(id)).where(qaqs.started.isTrue()).fetchFirst();
	}

	void deleteSlot(ActionQueueSlotRow row) {
		ParameterUtil.ensureNotNull(row, "row");
		deleteSlot(row.getId());
	}

	void deleteSlot(long id) {
		ParameterUtil.ensurePositive(id, "id");
		postgresContextService.delete(qaqs).where(qaqs.id.eq(id)).execute();
	}

	void deleteAllPendingSlots() {
		postgresContextService.delete(qaqs).where(qaqs.actionQueueId.eq(id), qaqs.started.isFalse()).execute();
	}

}
