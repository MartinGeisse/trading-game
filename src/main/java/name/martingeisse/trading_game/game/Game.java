package name.martingeisse.trading_game.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.generate.StarNaming;
import name.martingeisse.trading_game.game.generate.StarPlacement;
import name.martingeisse.trading_game.game.space.Asteroid;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.gui.MapPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 *
 */
@Singleton
public final class Game {

	private static final int TICK_TIME_DELAY = 100;
	private static final int TICK_MULTIPLIER = 1;

	private final Map<String, Player> players;
	private final Space space = new Space();

	@Inject
	public Game() {

		// TODO generate once and persist
		for (Pair<Long, Long> starPosition : StarPlacement.compute(1000, 2000, 2, 30000)) {
			Asteroid asteroid = new Asteroid();
			asteroid.setX(starPosition.getLeft());
			asteroid.setY(starPosition.getRight());
			asteroid.setName(StarNaming.compute());
			space.getSpaceObjects().add(asteroid);
		}

		this.players = new HashMap<>();
		new Timer(true).schedule(new TimerTask() {
			@Override
			public void run() {
				for (int i=0; i<TICK_MULTIPLIER; i++) {
					// TODO may drop ticks on high load depending on how the Timer class handles it
					GlobalGameLock.lock();
					try {
						tick();
					} finally {
						GlobalGameLock.unlock();
					}
				}
			}
		}, 0, TICK_TIME_DELAY);
	}

	public static int getTicksPerSecond() {
		return 1000 * TICK_MULTIPLIER / TICK_TIME_DELAY;
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

	public Player getPlayer(String knownPlayerId) {
		Player player = players.get(knownPlayerId);
		if (player == null) {
			String id;
			do {
				id = generatePlayerId();
			} while (players.get(id) != null);
			PlayerShip ship = new PlayerShip();
			space.getSpaceObjects().add(ship);
			player = new Player(this, id, ship);
			players.put(id, player);
		}
		return player;
	}

	private static String generatePlayerId() {
		return RandomStringUtils.random(16, true, true);
	}

	/**
	 * Called once every 100 msec to advance the game logic.
	 */
	private void tick() {
		for (Player player : players.values()) {
			player.tick();
		}
		space.tick();
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

}
