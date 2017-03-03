package name.martingeisse.trading_game.game.equipment;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

/**
 *
 */
@Singleton
public final class PlayerShipEquipmentRepository {

	private final PostgresService postgresService;

	@Inject
	public PlayerShipEquipmentRepository(PostgresService postgresService) {
		this.postgresService = postgresService;
	}

	public PlayerShipEquipment getPlayerShipEquipment(long playerShipId) {
		return new PlayerShipEquipment(postgresService, playerShipId);
	}

}
