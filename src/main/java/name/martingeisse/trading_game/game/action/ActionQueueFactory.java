package name.martingeisse.trading_game.game.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.ActionQueueRow;

/**
 *
 */
@Singleton
public class ActionQueueFactory {

	private final PostgresContextService postgresContextService;

	@Inject
	public ActionQueueFactory(PostgresContextService postgresContextService) {
		this.postgresContextService = postgresContextService;
	}

	/**
	 * Creates a new, empty action queue
	 *
	 * @return the ID of the new action queue
	 */
	public long createActionQueue() {
		ActionQueueRow actionQueueRow = new ActionQueueRow();
		actionQueueRow.insert(postgresContextService.getConnection());
		return actionQueueRow.getId();
	}

}
