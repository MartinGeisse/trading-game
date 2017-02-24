package name.martingeisse.trading_game.game.space;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.item.InventoryRepository;

/**
 *
 */
@Singleton
public final class SpaceObjectFactory {

	private final InventoryRepository inventoryRepository;

	@Inject
	public SpaceObjectFactory(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	/**
	 * Creates a new {@link Asteroid} object.
	 *
	 * @return the new object
	 */
	public Asteroid newAsteroid() {

	}

	/**
	 * Creates a new {@link Planet} object.
	 *
	 * @return the new object
	 */
	public Planet newPlanet() {
		return new Planet();
	}

	/**
	 * Creates a new {@link PlayerShip} object.
	 *
	 * @return the new object
	 */
	public PlayerShip newPlayerShip() {
		return new PlayerShip(inventoryRepository);
	}

	/**
	 * Creates a new {@link SpaceStation} object.
	 *
	 * @return the new object
	 */
	public SpaceStation newSpaceStation() {
		return new SpaceStation(inventoryRepository);
	}

	/**
	 * Creates a new {@link Star} object.
	 *
	 * @return the new object
	 */
	public Star newStar() {
		return new Star();
	}

}
