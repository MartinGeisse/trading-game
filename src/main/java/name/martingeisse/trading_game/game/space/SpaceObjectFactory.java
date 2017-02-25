package name.martingeisse.trading_game.game.space;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.definition.MiningYieldInfo;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.item.InventoryRepository;

/**
 *
 */
@Singleton
public final class SpaceObjectFactory {

	private final Game game;
	private final InventoryRepository inventoryRepository;

	@Inject
	public SpaceObjectFactory(Game game, InventoryRepository inventoryRepository) {
		this.game = game;
		this.inventoryRepository = inventoryRepository;
	}

	/**
	 * Creates a new {@link Asteroid} object.
	 *
	 * @return the new object
	 */
	Asteroid newAsteroid(long inventoryId, long yieldCapacity) {
		ImmutableItemStacks stacks = inventoryRepository.getInventory(inventoryId).getItems();
		MiningYieldInfo yieldInfo = stacks::scale;
		return new Asteroid(game, yieldInfo, yieldCapacity);
	}

	/**
	 * Creates a new {@link Planet} object.
	 *
	 * @return the new object
	 */
	Planet newPlanet() {
		return new Planet();
	}

	/**
	 * Creates a new {@link PlayerShip} object.
	 *
	 * @return the new object
	 */
	PlayerShip newPlayerShip() {
		return new PlayerShip(inventoryRepository);
	}

	/**
	 * Creates a new {@link SpaceStation} object.
	 *
	 * @return the new object
	 */
	SpaceStation newSpaceStation() {
		return new SpaceStation(inventoryRepository);
	}

	/**
	 * Creates a new {@link Star} object.
	 *
	 * @return the new object
	 */
	Star newStar() {
		return new Star();
	}

}
