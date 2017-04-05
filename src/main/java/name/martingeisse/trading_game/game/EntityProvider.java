package name.martingeisse.trading_game.game;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import name.martingeisse.trading_game.game.skill.PlayerSkills;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

/**
 * Allows to get arbitrary game entities by ID, injecting their dependencies.
 *
 * This class was introduced as an improvement over the previous repository-based system. In that previous system,
 * a separate repository was used for each entity type. This runs contrary to the idea that non-root entities can be
 * obtained via relationships, starting from other entities. The practical consequence was that all entities had
 * numerous repositories as dependencies.
 */
@Singleton
public class EntityProvider {

	private final Provider<PostgresService> postgresServiceProvider;
	private final Provider<JacksonService> jacksonServiceProvider;
	private final Provider<GameEventEmitter> gameEventEmitterProvider;
	private final Provider<PlayerRepository> playerRepositoryProvider;

	@Inject
	public EntityProvider(Provider<PostgresService> postgresServiceProvider, Provider<JacksonService> jacksonServiceProvider, Provider<GameEventEmitter> gameEventEmitterProvider, Provider<PlayerRepository> playerRepositoryProvider) {
		this.postgresServiceProvider = postgresServiceProvider;
		this.jacksonServiceProvider = jacksonServiceProvider;
		this.gameEventEmitterProvider = gameEventEmitterProvider;
		this.playerRepositoryProvider = playerRepositoryProvider;
	}

	public ActionQueue getActionQueue(long id) {
		return new ActionQueue(postgresServiceProvider.get(), jacksonServiceProvider.get(), id);
	}

	public Inventory getInventory(long id) {
		return new Inventory(postgresServiceProvider.get(), jacksonServiceProvider.get(), gameEventEmitterProvider.get(), id);
	}

	public Player getPlayer(long id) {
		return playerRepositoryProvider.get().getPlayerById(id);
	}

	public PlayerShipEquipment getPlayerShipEquipment(long playerShipId) {
		return new PlayerShipEquipment(postgresServiceProvider.get(), jacksonServiceProvider.get(), playerRepositoryProvider.get(), gameEventEmitterProvider.get(), playerShipId);
	}

	public PlayerSkills getPlayerSkills(long playerId) {
		return new PlayerSkills(postgresServiceProvider.get(), playerId);
	}

}
