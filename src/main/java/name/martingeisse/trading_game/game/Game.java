package name.martingeisse.trading_game.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
@Singleton
public final class Game {

	private final Map<String, Player> players;

	@Inject
	public Game() {
		this.players = new HashMap<>();
		new Timer(true).schedule(new TimerTask() {
			@Override
			public void run() {
				tick();
			}
		}, 0, 100);
	}

	public Player getPlayer(String knownPlayerId) {
		Player player = players.get(knownPlayerId);
		if (player == null) {
			String id;
			do {
				id = generatePlayerId();
			} while (players.get(id) != null);
			player = new Player(id);
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
	private synchronized void tick() {
		for (Player player : players.values()) {
			player.tick();
		}
	}

}
