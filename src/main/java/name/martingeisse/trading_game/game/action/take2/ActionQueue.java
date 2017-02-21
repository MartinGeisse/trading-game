package name.martingeisse.trading_game.game.action.take2;

import name.martingeisse.trading_game.game.item.ItemTypeSerializer;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

/**
 *
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

	// TODO insert
	// TODO tick / start next / tick action / finish
	// TODO get all actions (for displaying)
	// TODO cancel current
	// TODO cancel pending

}
