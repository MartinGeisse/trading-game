package name.martingeisse.trading_game.game;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public final class Game {

	private final Map map;
	private final Player player;

	public Game() {
		this.map = new Map();
		this.player = new Player();
		new Timer(true).schedule(new TimerTask() {
			@Override
			public void run() {
				tick();
			}
		}, 100);
	}

	/**
	 * Getter method.
	 *
	 * @return the map
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Getter method.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Called once every 100 msec to advance the game logic.
	 */
	private synchronized void tick() {

	}

}
