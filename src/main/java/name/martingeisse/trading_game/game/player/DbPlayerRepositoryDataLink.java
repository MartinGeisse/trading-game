package name.martingeisse.trading_game.game.player;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.dsl.BooleanExpression;
import name.martingeisse.trading_game.game.action.ActionQueueRepository;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentRepository;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
@Singleton
public final class DbPlayerRepositoryDataLink implements PlayerRepositoryDataLink {

	private final PostgresService postgresService;
	private final Space space;
	private final ActionQueueRepository actionQueueRepository;
	private final PlayerShipEquipmentRepository playerShipEquipmentRepository;
	private final JacksonService jacksonService;
	private final Provider<PlayerRepository> playerRepositoryProvider;

	@Inject
	public DbPlayerRepositoryDataLink(PostgresService postgresService, Space space, ActionQueueRepository actionQueueRepository, PlayerShipEquipmentRepository playerShipEquipmentRepository, JacksonService jacksonService, Provider<PlayerRepository> playerRepositoryProvider) {
		this.postgresService = postgresService;
		this.space = space;
		this.actionQueueRepository = actionQueueRepository;
		this.playerShipEquipmentRepository = playerShipEquipmentRepository;
		this.jacksonService = jacksonService;
		this.playerRepositoryProvider = playerRepositoryProvider;
	}

	private Player instantiate(PlayerRow data) {
		PlayerDataLink dataLink = new DbPlayerDataLink(postgresService, space, actionQueueRepository, playerShipEquipmentRepository, jacksonService, data);
		return new Player(playerRepositoryProvider.get(), dataLink);
	}

	/**
	 * Creates a new player.
	 *
	 * @return the ID of the newly created player
	 */
	public Player createPlayer(PlayerRow data) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			data.insert(connection);
		}
		return instantiate(data);
	}

	/**
	 * Gets a list of all players.
	 *
	 * @return the players
	 */
	public ImmutableList<Player> getAllPlayers() {
		List<Player> players = new ArrayList<>();
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerRow qp = QPlayerRow.Player;
			try (CloseableIterator<PlayerRow> iterator = connection.query().select(qp).from(qp).iterate()) {
				while (iterator.hasNext()) {
					players.add(instantiate(iterator.next()));
				}
			}
		}
		return ImmutableList.copyOf(players);
	}

	/**
	 * Gets a list of login tokens for a specific email address.
	 */
	public ImmutableList<String> getLoginTokensByEmailAddress(String emailAddress) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerRow qp = QPlayerRow.Player;
			return ImmutableList.copyOf(connection.query().select(qp.loginToken).from(qp).where(qp.emailAddress.eq(emailAddress), qp.loginToken.isNotNull()).fetch());
		}
	}

	/**
	 * Gets player by id.
	 *
	 * @param id the id
	 * @return the player
	 */
	public Player getPlayerById(long id) {
		return getPlayer(QPlayerRow.Player.id.eq(id));
	}

	/**
	 * Gets a player by name
	 *
	 * @param name the name
	 * @return the player
	 */
	public Player getPlayerByName(String name) {
		return getPlayer(QPlayerRow.Player.name.eq(name));
	}

	/**
	 * Gets player by ship id.
	 *
	 * @param shipId the ship id
	 * @return the player
	 */
	public Player getPlayerByShipId(long shipId) {
		return getPlayer(QPlayerRow.Player.shipId.eq(shipId));
	}

	/**
	 * Gets player by login token.
	 *
	 * @param loginToken the login token
	 * @return the player
	 */
	public Player getPlayerByLoginToken(String loginToken) {
		return getPlayer(QPlayerRow.Player.loginToken.eq(loginToken));
	}

	/**
	 * Checks if renaming a player is possible based on uniqueness of the new name.
	 *
	 * @param id      the player's id
	 * @param newName the intended new name
	 * @return true if renaming to that name is possible (i.e. no other player uses that name), false if not possible
	 */
	public boolean isRenamePossible(long id, String newName) {
		QPlayerRow qp = QPlayerRow.Player;
		return getPlayer(qp.id.ne(id).and(qp.name.eq(newName)), false) == null;
	}

	private Player getPlayer(BooleanExpression predicate) {
		return getPlayer(predicate, true);
	}

	private Player getPlayer(BooleanExpression predicate, boolean isRequired) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerRow qp = QPlayerRow.Player;
			PlayerRow playerRow = connection.query().select(qp).from(qp).where(predicate).fetchFirst();
			if (playerRow == null) {
				if (isRequired) {
					throw new IllegalArgumentException("player not found: " + predicate);
				} else {
					return null;
				}
			}
			return instantiate(playerRow);
		}
	}

	// TODO remove connection parameter
	public void forEachPlayer(PostgresConnection connection, Consumer<Player> body) {
		QPlayerRow qp = QPlayerRow.Player;
		try (CloseableIterator<PlayerRow> iterator = connection.query().select(qp).from(qp).iterate()) {
			while (iterator.hasNext()) {
				body.accept(instantiate(iterator.next()));
			}
		}
	}

}
