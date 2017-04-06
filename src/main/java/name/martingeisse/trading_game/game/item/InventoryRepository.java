package name.martingeisse.trading_game.game.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

/**
 * Helper class to handle mixed injection / id parameters in class {@link Inventory}.
 */
@Singleton
public class InventoryRepository {

	private final PostgresService postgresService;
	private final JacksonService jacksonService;
	private final GameEventEmitter gameEventEmitter;

	@Inject
	public InventoryRepository(PostgresService postgresService, JacksonService jacksonService, GameEventEmitter gameEventEmitter) {
		this.postgresService = postgresService;
		this.jacksonService = jacksonService;
		this.gameEventEmitter = gameEventEmitter;
	}

	/**
	 * Creates a new instance based by the specified database ID. This constructor does not ensure that the ID
	 * actually exists in the database.
	 *
	 * @param id the inventory ID
	 */
	public Inventory getInventory(long id) {
		return new Inventory(postgresService, jacksonService, gameEventEmitter, id);
	}

}
