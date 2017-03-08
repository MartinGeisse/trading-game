package name.martingeisse.trading_game.game.space;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.definition.MiningYieldInfo;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.item.InventoryRepository;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

/**
 *
 */
@Singleton
public final class SpaceObjectFactory {

	private final PostgresService postgresService;
	private final GameEventEmitter gameEventEmitter;
	private final InventoryRepository inventoryRepository;

	@Inject
	public SpaceObjectFactory(PostgresService postgresService, GameEventEmitter gameEventEmitter, InventoryRepository inventoryRepository) {
		this.postgresService = postgresService;
		this.gameEventEmitter = gameEventEmitter;
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
		return inject(new Asteroid(gameEventEmitter, yieldInfo, yieldCapacity));
	}

	/**
	 * Creates a new {@link Planet} object.
	 *
	 * @return the new object
	 */
	Planet newPlanet() {
		return inject(new Planet());
	}

	/**
	 * Creates a new {@link PlayerShip} object.
	 *
	 * @return the new object
	 */
	PlayerShip newPlayerShip() {
		return inject(new PlayerShip(inventoryRepository));
	}

	/**
	 * Creates a new {@link SpaceStation} object.
	 *
	 * @return the new object
	 */
	SpaceStation newSpaceStation() {
		return inject(new SpaceStation(inventoryRepository));
	}

	/**
	 * Creates a new {@link Star} object.
	 *
	 * @return the new object
	 */
	Star newStar() {
		return inject(new Star());
	}

	private <T extends SpaceObject> T inject(T spaceObject) {
		spaceObject.internalSetPostgresService(postgresService);
		spaceObject.internalSetGameEventEmitter(gameEventEmitter);
		return spaceObject;
	}

}
