package name.martingeisse.trading_game.game.player;

import com.querydsl.core.types.Path;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import name.martingeisse.trading_game.postgres_entities.QCachedPlayerAttributeRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

/**
 *
 */
public final class DbPlayerDataLink {

	private final PlayerRepository playerRepository;
	private final PostgresService postgresService;
	private final Space space;
	private final EntityProvider entityProvider;
	private final JacksonService jacksonService;
	private final long id;
	private final long shipId;
	private final long actionQueueId;

	public DbPlayerDataLink(PlayerRepository playerRepository, PostgresService postgresService, Space space, EntityProvider entityProvider, JacksonService jacksonService, PlayerRow playerRow) {
		this.playerRepository = playerRepository;
		this.postgresService = postgresService;
		this.space = space;
		this.entityProvider = entityProvider;
		this.jacksonService = jacksonService;
		this.id = playerRow.getId();
		this.shipId = playerRow.getShipId();
		this.actionQueueId = playerRow.getActionQueueId();
	}

	public long getId() {
		return id;
	}

	public <T> T getField(Path<T> path) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerRow qp = QPlayerRow.Player;
			return connection.query().select(path).from(qp).where(qp.id.eq(id)).fetchFirst();
		}
	}

	public <T> void setField(Path<T> path, T newValue) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerRow qp = QPlayerRow.Player;
			connection.update(qp).set(path, newValue).where(qp.id.eq(id)).execute();
		}
	}

	// TODO playerRepository.isRenamePossible ?

	/**
	 * Getter method.
	 *
	 * @return the ship
	 */
	public PlayerShip getShip() {
		// TODO EntityProvider?
		return (PlayerShip) space.get(shipId);
	}

	/**
	 * Getter method.
	 *
	 * @return the action queue
	 */
	public ActionQueue getActionQueue() {
		return entityProvider.getActionQueue(actionQueueId);
	}

	/**
	 * Getter method.
	 *
	 * @return the player ship equipment
	 */
	public PlayerShipEquipment getEquipment() {
		return entityProvider.getPlayerShipEquipment(shipId);
	}

	public void clearCachedAttributes() {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
			connection.delete(qa).where(qa.playerId.eq(id)).execute();
		}
	}

	public void insertCachedAttribute(PlayerAttributeKey key, Object value) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			String serializedValue = jacksonService.serialize(value);
			QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
			// TODO handle serialization here?
			connection.insert(qa).set(qa.playerId, id).set(qa.key, key).set(qa.value, serializedValue).execute();
		}
	}

	public Object getCachedAttribute(PlayerAttributeKey key) {
		String serializedValue;
		try (PostgresConnection connection = postgresService.newConnection()) {
			QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
			serializedValue = connection.query().select(qa.value).from(qa).where(qa.playerId.eq(id), qa.key.eq(key)).fetchFirst();
			if (serializedValue == null) {
				throw new IllegalStateException("missing player attribute " + key + " for player ID " + id);
			}
		}
		return jacksonService.deserialize(serializedValue, Object.class);
	}

}
