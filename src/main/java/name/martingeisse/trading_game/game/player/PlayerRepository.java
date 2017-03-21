package name.martingeisse.trading_game.game.player;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
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

/**
 *
 */
@Singleton
public final class PlayerRepository {

	private final PostgresService postgresService;
	private final Space space;
	private final ActionQueueRepository actionQueueRepository;
	private final PlayerShipEquipmentRepository playerShipEquipmentRepository;
	private final JacksonService jacksonService;

	@Inject
	public PlayerRepository(PostgresService postgresService, Space space, ActionQueueRepository actionQueueRepository, PlayerShipEquipmentRepository playerShipEquipmentRepository, JacksonService jacksonService) {
		this.postgresService = postgresService;
		this.space = space;
		this.actionQueueRepository = actionQueueRepository;
		this.playerShipEquipmentRepository = playerShipEquipmentRepository;
		this.jacksonService = jacksonService;
	}

	/**
	 * Creates a new player.
	 *
	 * @return the ID of the newly created player
	 */
	public long createPlayer() {
		try (PostgresConnection connection = postgresService.newConnection()) {
			PlayerRow playerRow = new PlayerRow();
			playerRow.setShipId(space.createPlayerShip("noname's ship", 0, 0));
			playerRow.setActionQueueId(actionQueueRepository.createActionQueue());
			playerRow.setName("noname");
			playerRow.insert(connection);
			Player player = new Player(postgresService, this, space, actionQueueRepository, playerShipEquipmentRepository, jacksonService, playerRow);
			player.updateAttributes();
			return playerRow.getId();
		}
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
					PlayerRow playerRow = iterator.next();
					players.add(new Player(postgresService, this, space, actionQueueRepository, playerShipEquipmentRepository, jacksonService, playerRow));
				}
			}
		}
		return ImmutableList.copyOf(players);
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
			return new Player(postgresService, this, space, actionQueueRepository, playerShipEquipmentRepository, jacksonService, playerRow);
		}
	}

	/**
	 * Called once every second to advance the game logic.
	 */
	public void tick(PostgresConnection connection) {
		QPlayerRow qp = QPlayerRow.Player;
		try (CloseableIterator<PlayerRow> iterator = connection.query().select(qp).from(qp).iterate()) {
			while (iterator.hasNext()) {
				PlayerRow playerRow = iterator.next();
				Player player = new Player(postgresService, this, space, actionQueueRepository, playerShipEquipmentRepository, jacksonService, playerRow);
				player.tick(connection);
			}
		}
	}

}
