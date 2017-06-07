package name.martingeisse.trading_game.game.player;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.dsl.BooleanExpression;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
@Singleton
public final class PlayerRepository {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;
	private final Space space;
	private final EntityProvider entityProvider;

	@Inject
	public PlayerRepository(PostgresContextService postgresContextService, JacksonService jacksonService, Space space, EntityProvider entityProvider) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
		this.space = space;
		this.entityProvider = entityProvider;
	}

	private Player instantiate(PlayerRow data) {
		return new Player(this, postgresContextService, entityProvider, jacksonService, space, data);
	}

	/**
	 * Gets a list of all players.
	 *
	 * @return the players
	 */
	public ImmutableList<Player> getAllPlayers() {
		List<Player> players = new ArrayList<>();
		QPlayerRow qp = QPlayerRow.Player;
		try (CloseableIterator<PlayerRow> iterator = postgresContextService.select(qp).from(qp).iterate()) {
			while (iterator.hasNext()) {
				players.add(instantiate(iterator.next()));
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
		QPlayerRow qp = QPlayerRow.Player;
		PlayerRow playerRow = postgresContextService.select(qp).from(qp).where(predicate).fetchFirst();
		if (playerRow == null) {
			if (isRequired) {
				throw new IllegalArgumentException("player not found: " + predicate);
			} else {
				return null;
			}
		}
		return instantiate(playerRow);
	}

	public void forEachPlayer(Consumer<Player> body) {
		QPlayerRow qp = QPlayerRow.Player;
		try (CloseableIterator<PlayerRow> iterator = postgresContextService.query().select(qp).from(qp).iterate()) {
			while (iterator.hasNext()) {
				body.accept(instantiate(iterator.next()));
			}
		}
	}

	/**
	 * Called once every second to advance the game logic.
	 */
	public void tick() {
		forEachPlayer(Player::tick);
	}

}
