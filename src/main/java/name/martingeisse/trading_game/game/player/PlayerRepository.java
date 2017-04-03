package name.martingeisse.trading_game.game.player;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.action.ActionQueueRepository;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 */
@Singleton
public final class PlayerRepository {

	private final Space space;
	private final ActionQueueRepository actionQueueRepository;
	private final PlayerRepositoryDataLink datalink;

	@Inject
	public PlayerRepository(Space space, ActionQueueRepository actionQueueRepository, PlayerRepositoryDataLink datalink) {
		this.space = space;
		this.actionQueueRepository = actionQueueRepository;
		this.datalink = datalink;
	}

	/**
	 * Creates a new player.
	 *
	 * @return the ID of the newly created player
	 */
	public Player createPlayer() {
		PlayerRow data = new PlayerRow();
		data.setShipId(space.createPlayerShip("noname's ship", 0, 0));
		data.setActionQueueId(actionQueueRepository.createActionQueue());
		data.setName("noname");
		data.setLoginToken(RandomStringUtils.randomAlphanumeric(50));
		data.setMoney(0L);
		Player player = dataLink.create(data);
		player.updateAttributes();
		return player;
	}

	/**
	 * Gets a list of all players.
	 *
	 * @return the players
	 */
	public ImmutableList<Player> getAllPlayers() {
		return dataLink.getAllPlayers();
	}

	/**
	 * Gets a list of login tokens for a specific email address.
	 */
	public ImmutableList<String> getLoginTokensByEmailAddress(String emailAddress) {
		return dataLink.getLoginTokensByEmailAddress(emailAddress);
	}

	/**
	 * Gets player by id.
	 *
	 * @param id the id
	 * @return the player
	 */
	public Player getPlayerById(long id) {
		return dataLink.getPlayerById(id);
	}

	/**
	 * Gets a player by name
	 *
	 * @param name the name
	 * @return the player
	 */
	public Player getPlayerByName(String name) {
		return dataLink.getPlayerByName(name);
	}

	/**
	 * Gets player by ship id.
	 *
	 * @param shipId the ship id
	 * @return the player
	 */
	public Player getPlayerByShipId(long shipId) {
		return dataLink.getPlayerByShipId(shipId);
	}

	/**
	 * Gets player by login token.
	 *
	 * @param loginToken the login token
	 * @return the player
	 */
	public Player getPlayerByLoginToken(String loginToken) {
		return dataLink.getPlayerByLoginToken(loginToken);
	}

	/**
	 * Checks if renaming a player is possible based on uniqueness of the new name.
	 *
	 * @param id      the player's id
	 * @param newName the intended new name
	 * @return true if renaming to that name is possible (i.e. no other player uses that name), false if not possible
	 */
	public boolean isRenamePossible(long id, String newName) {
		return dataLink.isRenamePossible(id, newName);
	}

	/**
	 * Called once every second to advance the game logic.
	 */
	public void tick(PostgresConnection connection) {
		dataLink.forEachPlayer(p -> p.tick(connection));
	}

}
