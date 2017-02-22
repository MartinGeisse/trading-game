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
public class ActionQueueRepository {

	private final PostgresService postgresService;
	private final ActionSerializer actionSerializer;

	@Inject
	public ActionQueueRepository(PostgresService postgresService, ActionSerializer actionSerializer) {
		this.postgresService = postgresService;
		this.actionSerializer = actionSerializer;
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

	/**
	 * Gets an action queue from the database as a new {@link ActionQueue} instance. This constructor does not ensure
	 * that the ID actually exists in the database.
	 *
	 * @param id the action queue ID
	 */
	public ActionQueue getActionQueue(long id) {
		return new ActionQueue(postgresService, actionSerializer, id);
	}

}
