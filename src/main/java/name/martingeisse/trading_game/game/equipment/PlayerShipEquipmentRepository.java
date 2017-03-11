package name.martingeisse.trading_game.game.equipment;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

/**
 *
 */
@Singleton
public final class PlayerShipEquipmentRepository {

	private final PostgresService postgresService;
	private final JacksonService jacksonService;
	private final Provider<PlayerRepository> playerRepositoryProvider;
	private final GameEventEmitter gameEventEmitter;

	@Inject
	public PlayerShipEquipmentRepository(PostgresService postgresService, JacksonService jacksonService, Provider<PlayerRepository> playerRepositoryProvider, GameEventEmitter gameEventEmitter) {
		this.postgresService = postgresService;
		this.jacksonService = jacksonService;
		this.playerRepositoryProvider = playerRepositoryProvider;
		this.gameEventEmitter = gameEventEmitter;
	}

	public PlayerShipEquipment getPlayerShipEquipment(long playerShipId) {
		return new PlayerShipEquipment(postgresService, jacksonService, playerRepositoryProvider.get(), gameEventEmitter, playerShipId);
	}

}
