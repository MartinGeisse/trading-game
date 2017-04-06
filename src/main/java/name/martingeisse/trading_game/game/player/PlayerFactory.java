package name.martingeisse.trading_game.game.player;

import name.martingeisse.trading_game.game.action.ActionQueueFactory;
import name.martingeisse.trading_game.game.space.SpaceObjectFactory;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 */
public class PlayerFactory {

	private final PostgresService postgresService;
	private final ActionQueueFactory actionQueueFactory;
	private final SpaceObjectFactory spaceObjectFactory;

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
		data.setMoney(0L);
		try (PostgresConnection connection = postgresService.newConnection()) {
			data.insert(connection);
		}
		Player player = instantiate(data);
		player.updateAttributes();
		return player;
	}

}
