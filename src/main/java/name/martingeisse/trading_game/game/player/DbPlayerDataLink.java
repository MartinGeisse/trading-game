package name.martingeisse.trading_game.game.player;

import com.querydsl.core.types.Path;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.action.ActionQueueRepository;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentRepository;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.player.PlayerAttributeKey;
import name.martingeisse.trading_game.game.player.PlayerDataLink;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import name.martingeisse.trading_game.postgres_entities.QCachedPlayerAttributeRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

import java.sql.SQLException;
import java.util.Map;

/**
 *
 */
public final class DbPlayerDataLink implements PlayerDataLink {

	private final PostgresService postgresService;
	private final Space space;
	private final ActionQueueRepository actionQueueRepository;
	private final PlayerShipEquipmentRepository playerShipEquipmentRepository;
	private final JacksonService jacksonService;
	private final long id;
	private final long shipId;
	private final long actionQueueId;

	public DbPlayerDataLink(PostgresService postgresService, Space space, ActionQueueRepository actionQueueRepository, PlayerShipEquipmentRepository playerShipEquipmentRepository, JacksonService jacksonService, PlayerRow playerRow) {
		this.postgresService = postgresService;
		this.space = space;
		this.actionQueueRepository = actionQueueRepository;
		this.playerShipEquipmentRepository = playerShipEquipmentRepository;
		this.jacksonService = jacksonService;
		this.id = playerRow.getId();
		this.shipId = playerRow.getShipId();
		this.actionQueueId = playerRow.getActionQueueId();
	}

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	private <T> T fetchField(Path<T> path) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerRow qp = QPlayerRow.Player;
			return connection.query().select(path).from(qp).where(qp.id.eq(id)).fetchFirst();
		}
	}

	private <T> void updateField(Path<T> path, T newValue) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerRow qp = QPlayerRow.Player;
			connection.update(qp).set(path, newValue).where(qp.id.eq(id)).execute();
		}
	}

	public String getName() {
		return fetchField(QPlayerRow.Player.name);
	}

	public void setName(String name) {
		updateField(QPlayerRow.Player.name, name);
	}

	public String getLoginToken() {
		return fetchField(QPlayerRow.Player.loginToken);
	}

	public void setLoginToken(String loginToken) {
		updateField(QPlayerRow.Player.loginToken, loginToken);
	}

	public String getEmailAddress() {
		return fetchField(QPlayerRow.Player.emailAddress);
	}

	public void setEmailAddress(String emailAddress) {
		updateField(QPlayerRow.Player.emailAddress, emailAddress);
	}

	public PlayerShip getShip() {
		// TODO rebuild using a data link for the ship
		return (PlayerShip) space.get(shipId);
	}

	public ActionQueue getActionQueue() {
		// TODO rebuild using a data link for the action queue
		return actionQueueRepository.getActionQueue(actionQueueId);
	}

	public PlayerShipEquipment getEquipment() {
		// TODO rebuild using a data link for the equipment
		return playerShipEquipmentRepository.getPlayerShipEquipment(shipId);
	}

	public void replacePlayerAttributes(Map<PlayerAttributeKey, ?> newAttributes) {
		// TODO consider moving transaction control to a central class because it affects all data links anyway
		try (PostgresConnection connection = postgresService.newConnection()) {
			QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
			connection.getJdbcConnection().setAutoCommit(false);
			connection.delete(qa).where(qa.playerId.eq(id)).execute();
			for (Map.Entry<PlayerAttributeKey, ?> entry : newAttributes.entrySet()) {
				insertAttribute(connection, entry.getKey(), entry.getValue());
			}
			connection.getJdbcConnection().commit();
		} catch (SQLException e) {
			throw new UnexpectedExceptionException(e);
		}

	}

	private void insertAttribute(PostgresConnection connection, PlayerAttributeKey key, Object value) {
		String serializedValue = jacksonService.serialize(value);
		QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
		connection.insert(qa).set(qa.playerId, id).set(qa.key, key).set(qa.value, serializedValue).execute();
	}

	public Object getAttribute(PlayerAttributeKey key) {
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
