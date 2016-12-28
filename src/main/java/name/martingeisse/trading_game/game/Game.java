package name.martingeisse.trading_game.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.definition.GameConstants;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.generate.StarNaming;
import name.martingeisse.trading_game.game.generate.StarPlacement;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.space.Asteroid;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.game.space.Space;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 *
 */
@Singleton
public final class Game {

	// debugging switch to speed up the game
	private static final int TICK_MULTIPLIER = 1;

	private final GameDefinition gameDefinition;
	private final Map<String, Player> players;
	private final Space space = new Space();

	@Inject
	public Game(GameDefinition gameDefinition) {
		this.gameDefinition = gameDefinition;

		// TODO generate once and persist
		{
			long yieldCapacity = 6 * GameConstants.BASE_MINING_SPEED;
			double oreDensity = 0.01;
			FixedInventory asteroidYieldPerTick = FixedInventory.from(gameDefinition.getRedPixelItemType(), 5);
			for (Pair<Long, Long> starPosition : StarPlacement.compute(1000, 2000, 2, 30000)) {
				Asteroid asteroid = new Asteroid(amount -> asteroidYieldPerTick.scale(amount * oreDensity), yieldCapacity);
				asteroid.setX(starPosition.getLeft());
				asteroid.setY(starPosition.getRight());
				asteroid.setName(StarNaming.compute());
				space.getSpaceObjects().add(asteroid);
			}
		}

		this.players = new HashMap<>();
		new Timer(true).schedule(new TimerTask() {
			@Override
			public void run() {
				for (int i = 0; i < TICK_MULTIPLIER; i++) {
					// TODO may drop ticks on high load depending on how the Timer class handles it
					GlobalGameLock.lock();
					try {
						tick();
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
	 * Called once every second to advance the game logic.
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
