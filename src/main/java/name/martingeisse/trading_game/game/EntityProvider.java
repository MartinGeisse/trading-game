package name.martingeisse.trading_game.game;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.market.MarketOrder;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import name.martingeisse.trading_game.game.skill.PlayerSkills;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;

/**
 * Allows to get arbitrary game entities by ID, injecting their dependencies.
 * <p>
 * This class was introduced as an improvement over the previous repository-based system. In that previous system,
 * a separate repository was used for each entity type. This runs contrary to the idea that non-root entities can be
 * obtained via relationships, starting from other entities. The practical consequence was that all entities had
 * numerous repositories as dependencies.
 */
@Singleton
public class EntityProvider {

	private final Provider<PostgresContextService> postgresContextServiceProvider;
	private final Provider<JacksonService> jacksonServiceProvider;
	private final Provider<GameEventEmitter> gameEventEmitterProvider;
	private final Provider<PlayerRepository> playerRepositoryProvider;
	private final Provider<Space> spaceProvider;

	@Inject
	public EntityProvider(Provider<PostgresContextService> postgresContextServiceProvider, Provider<JacksonService> jacksonServiceProvider, Provider<GameEventEmitter> gameEventEmitterProvider, Provider<PlayerRepository> playerRepositoryProvider, Provider<Space> spaceProvider) {
		this.postgresContextServiceProvider = postgresContextServiceProvider;
		this.jacksonServiceProvider = jacksonServiceProvider;
		this.gameEventEmitterProvider = gameEventEmitterProvider;
		this.playerRepositoryProvider = playerRepositoryProvider;
		this.spaceProvider = spaceProvider;
	}

	public ActionQueue getActionQueue(long id) {
		return new ActionQueue(postgresContextServiceProvider.get(), jacksonServiceProvider.get(), id);
	}

	public Inventory getInventory(long id) {
		return new Inventory(postgresContextServiceProvider.get(), jacksonServiceProvider.get(), gameEventEmitterProvider.get(), id);
	}

	public Player getPlayer(long id) {
		return playerRepositoryProvider.get().getPlayerById(id);
	}

	public Player getPlayerByShipId(long shipId) {
		return playerRepositoryProvider.get().getPlayerByShipId(shipId);
	}

	public PlayerShipEquipment getPlayerShipEquipment(long playerShipId) {
		return new PlayerShipEquipment(postgresContextServiceProvider.get(), jacksonServiceProvider.get(), this, gameEventEmitterProvider.get(), playerShipId);
	}

	public PlayerSkills getPlayerSkills(long playerId) {
		return new PlayerSkills(postgresContextServiceProvider.get(), playerId);
	}

	public SpaceObject getSpaceObject(long id) {
		return spaceProvider.get().get(id);
	}

	public MarketOrder getMarketOrder(long id) {
		return new MarketOrder(postgresContextServiceProvider.get(), jacksonServiceProvider.get(), id);
	}

}
