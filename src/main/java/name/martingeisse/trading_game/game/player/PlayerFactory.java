package name.martingeisse.trading_game.game.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.action.ActionQueueFactory;
import name.martingeisse.trading_game.game.space.SpaceObjectFactory;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 */
@Singleton
public class PlayerFactory {

	private final PostgresContextService postgresContextService;
	private final ActionQueueFactory actionQueueFactory;
	private final SpaceObjectFactory spaceObjectFactory;
	private final EntityProvider entityProvider;

	@Inject
	public PlayerFactory(PostgresContextService postgresContextService, ActionQueueFactory actionQueueFactory, SpaceObjectFactory spaceObjectFactory, EntityProvider entityProvider) {
		this.postgresContextService = postgresContextService;
		this.actionQueueFactory = actionQueueFactory;
		this.spaceObjectFactory = spaceObjectFactory;
		this.entityProvider = entityProvider;
	}

	/**
	 * Creates a new player.
	 *
	 * @return the ID of the newly created player
	 */
	public Player createPlayer() {
		PlayerRow data = new PlayerRow();
		data.setShipId(spaceObjectFactory.createPlayerShip("noname's ship", 0, 0));
		data.setActionQueueId(actionQueueFactory.createActionQueue());
		data.setName("noname");
		data.setLoginToken(RandomStringUtils.randomAlphanumeric(50));
		data.setSpentFoldingCredits(0L);
		data.setRemainingPlayTime(3600L * 24 * 30);
		data.setFoldingUserHash(RandomStringUtils.random(64, "0123456789abcdef"));
		data.setMoney(0L);
		data.insert(postgresContextService.getConnection());
		Player player = entityProvider.getPlayer(data.getId());
		player.updateAttributes();
		return player;
	}

}
