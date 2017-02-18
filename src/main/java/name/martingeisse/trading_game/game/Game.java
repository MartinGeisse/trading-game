package name.martingeisse.trading_game.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

/**
 *
 */
@Singleton
public final class Game {

	// debugging switch to speed up the game
	private static final int TICK_MULTIPLIER = 1;

	private final PostgresService postgresService;
	private final GameDefinition gameDefinition;
	private final Space space;
	private final Map<String, Player> players;
	private final GameListenerSet listeners = new GameListenerSet();

	@Inject
	public Game(PostgresService postgresService, GameDefinition gameDefinition, Space space) {
		this.postgresService = postgresService;
		this.gameDefinition = gameDefinition;
		this.space = space;
		this.players = new HashMap<>();
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

	public Collection<Player> getPlayers() {
		return players.values();
	}

	/**
	 * Getter method.
	 *
	 * @return the space
	 */
	public Space getSpace() {
		return space;
	}

	public Player getPlayer(String playerId) {
		return players.get(playerId);
	}

	public Player getOrCreatePlayer(String playerId) {
		Player player = players.get(playerId);
		if (player == null) {
			String id;
			do {
				id = generatePlayerId();
			} while (players.get(id) != null);
			long playerShipId = space.createPlayerShip("unnamed player ship", 0, 0);
			// TODO this will only save the ID in the future
			PlayerShip ship = (PlayerShip)space.get(playerShipId);
			player = new Player(this, id, ship);
			players.put(id, player);
		}
		return player;
	}

	private static String generatePlayerId() {
		return RandomStringUtils.random(16, true, true);
	}

	/**
	 * Called once every second to advance the game logic.
	 */
	private void tick(PostgresConnection connection) {
		for (Player player : players.values()) {
			player.tick();
		}
		space.tick(connection);
		listeners.onDynamicSpaceObjectsChanged();
	}

	/**
	 * Checks if another player has the specified new name.
	 */
	boolean isRenamePossible(Player player, String newName) {
		for (Player otherPlayer : players.values()) {
			if (otherPlayer != player && otherPlayer.getName().equals(newName)) {
				return false;
			}
		}
		return true;
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
