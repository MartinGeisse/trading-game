package name.martingeisse.trading_game.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Calls the tick() method of all game objects regularly.
 */
@Singleton
public final class GameTicker {

	// debugging switch to speed up the game
	private static final int TICK_MULTIPLIER = 1;

	private final PostgresContextService postgresContextService;
	private final Space space;
	private final PlayerRepository playerRepository;

	@Inject
	public GameTicker(PostgresContextService postgresContextService, Space space, PlayerRepository playerRepository) {
		this.postgresContextService = postgresContextService;
		this.space = space;
		this.playerRepository = playerRepository;
	}

	/**
	 * Starts game logic ticking.
	 */
	public void start() {
		new Timer(true).schedule(new TimerTask() {
			@Override
			public void run() {
				for (int i = 0; i < TICK_MULTIPLIER; i++) {
					try {
						tick(postgresContextService.getConnection());
					} finally {
						postgresContextService.reset();
					}
				}
			}
		}, 0, 1000);
	}

	/**
	 * Called once every second to advance the game logic.
	 */
	private void tick(PostgresConnection connection) {
		try {
			playerRepository.tick(connection);
			space.tick(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
