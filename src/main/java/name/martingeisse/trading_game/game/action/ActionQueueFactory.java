package name.martingeisse.trading_game.game.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.ActionQueueRow;

/**
 *
 */
@Singleton
public class ActionQueueFactory {

	private final PostgresService postgresService;

	@Inject
	public ActionQueueFactory(PostgresService postgresService) {
		this.postgresService = postgresService;
	}

	/**
	 * Creates a new, empty action queue
	 *
	 * @return the ID of the new action queue
	 */
	public long createActionQueue() {
		try (PostgresConnection connection = postgresService.newConnection()) {
			ActionQueueRow actionQueueRow = new ActionQueueRow();
			actionQueueRow.insert(connection);
			return actionQueueRow.getId();
		}
	}

}
