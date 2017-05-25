package name.martingeisse.trading_game.peripherals.feedback;

import com.google.inject.Inject;
import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.QGameFeedbackRow;
import name.martingeisse.trading_game.tools.codegen.PostgresJsonb;

/**
 *
 */
public class SubmitFeedbackService {

	private final JacksonService jacksonService;
	private final PostgresContextService postgresContextService;

	@Inject
	public SubmitFeedbackService(JacksonService jacksonService, PostgresContextService postgresContextService) {
		this.jacksonService = jacksonService;
		this.postgresContextService = postgresContextService;
	}

	public void submitFeedback(String sessionId, Long playerId, FeedbackContext context, String text) {
		QGameFeedbackRow qf = QGameFeedbackRow.GameFeedback;

		String serializedContext = jacksonService.serialize(context);
		SQLInsertClause insert = postgresContextService.insert(qf);
		insert.set(qf.sessionId, sessionId);
		insert.set(qf.playerId, playerId);
		insert.set(qf.context, new PostgresJsonb(serializedContext));
		insert.set(qf.text, text);
		insert.execute();
	}

}
