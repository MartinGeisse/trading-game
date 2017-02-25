package name.martingeisse.trading_game.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO remove this class!!!
 */
@Singleton
public final class Game {

	// debugging switch to speed up the game
	private static final int TICK_MULTIPLIER = 1;

	private final Space space;
	private final PlayerRepository playerRepository;
	private final GameListenerSet listeners = new GameListenerSet();

	@Inject
	public Game(PostgresService postgresService, Space space, PlayerRepository playerRepository) {
		this.space = space;
		this.playerRepository = playerRepository;
		new Timer(true).schedule(new TimerTask() {
			@Override
			public void run() {
				for (int i = 0; i < TICK_MULTIPLIER; i++) {
					// TODO may drop ticks on high load depending on how the Timer class handles it
					GlobalGameLock.lock();
					try (PostgresConnection connection = postgresService.newConnection()) {
						tick(connection);
					} finally {
						GlobalGameLock.unlock();
					}
				}
			}
		}, 0, 1000);
	}

	/**
	 * Called once every second to advance the game logic.
	 */
	private void tick(PostgresConnection connection) {
		playerRepository.tick(connection);
		space.tick(connection);
		listeners.onDynamicSpaceObjectsChanged();
	}

	/**
	 * Getter method.
	 *
	 * @return the listeners
	 */
	public GameListenerSet getListeners() {
		return listeners;
	}

}
