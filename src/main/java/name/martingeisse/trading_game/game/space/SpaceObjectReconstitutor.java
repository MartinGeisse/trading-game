package name.martingeisse.trading_game.game.space;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.game.definition.MiningYieldInfo;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.item.InventoryRepository;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.SpaceObjectBaseDataRow;

/**
 *
 */
@Singleton
public final class SpaceObjectReconstitutor {

	private final PostgresService postgresService;
	private final GameEventEmitter gameEventEmitter;
	private final InventoryRepository inventoryRepository;

	@Inject
	public SpaceObjectReconstitutor(PostgresService postgresService, GameEventEmitter gameEventEmitter, InventoryRepository inventoryRepository) {
		this.postgresService = ParameterUtil.ensureNotNull(postgresService, "postgresService");
		this.gameEventEmitter = ParameterUtil.ensureNotNull(gameEventEmitter, "gameEventEmitter");
		this.inventoryRepository = ParameterUtil.ensureNotNull(inventoryRepository, "inventoryRepository");
	}

	/**
	 * Creates a new {@link Asteroid} object.
	 *
	 * @return the new object
	 */
	Asteroid newAsteroid(SpaceObjectBaseDataRow row) {
		ParameterUtil.ensureNotNull(row, "row");
		ImmutableItemStacks stacks = inventoryRepository.getInventory(row.getInventoryId()).getItems(null);
		MiningYieldInfo yieldInfo = stacks::scale;
		return inject(row, new Asteroid(gameEventEmitter, yieldInfo, row.getLongField1()));
	}

	/**
	 * Creates a new {@link Planet} object.
	 *
	 * @return the new object
	 */
	Planet newPlanet(SpaceObjectBaseDataRow row) {
		ParameterUtil.ensureNotNull(row, "row");
		return inject(row, new Planet());
	}

	/**
	 * Creates a new {@link PlayerShip} object.
	 *
	 * @return the new object
	 */
	PlayerShip newPlayerShip(SpaceObjectBaseDataRow row) {
		ParameterUtil.ensureNotNull(row, "row");
		PlayerShip playerShip = inject(row, new PlayerShip(inventoryRepository));
		playerShip.internalSetInventoryId(row.getInventoryId());
		return playerShip;
	}

	/**
	 * Creates a new {@link SpaceStation} object.
	 *
	 * @return the new object
	 */
	SpaceStation newSpaceStation(SpaceObjectBaseDataRow row) {
		ParameterUtil.ensureNotNull(row, "row");
		SpaceStation spaceStation = inject(row, new SpaceStation(inventoryRepository));
		spaceStation.internalSetInventoryId(row.getInventoryId());
		return spaceStation;
	}

	/**
	 * Creates a new {@link Star} object.
	 *
	 * @return the new object
	 */
	Star newStar(SpaceObjectBaseDataRow row) {
		ParameterUtil.ensureNotNull(row, "row");
		return inject(row, new Star());
	}

	private <T extends SpaceObject> T inject(SpaceObjectBaseDataRow row, T spaceObject) {
		spaceObject.internalSetPostgresService(postgresService);
		spaceObject.internalSetGameEventEmitter(gameEventEmitter);
		spaceObject.internalSetId(row.getId());
		spaceObject.internalSetName(row.getName());
		spaceObject.internalSetX((long) row.getPosition().x);
		spaceObject.internalSetY((long) row.getPosition().y);
		return spaceObject;
	}

}
