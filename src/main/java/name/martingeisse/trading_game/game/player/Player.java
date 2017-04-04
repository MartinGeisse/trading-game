package name.martingeisse.trading_game.game.player;

import com.fasterxml.jackson.annotation.JsonValue;
import com.querydsl.core.types.Path;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.game.NameAlreadyUsedException;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.action.ActionQueueRepository;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentRepository;
import name.martingeisse.trading_game.game.equipment.SlotInfo;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import name.martingeisse.trading_game.postgres_entities.QCachedPlayerAttributeRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class Player {

	private final PlayerRepository playerRepository;
	private final PostgresService postgresService;
	private final Space space;
	private final ActionQueueRepository actionQueueRepository;
	private final PlayerShipEquipmentRepository playerShipEquipmentRepository;
	private final JacksonService jacksonService;
	private final long id;
	private final long shipId;
	private final long actionQueueId;

	public Player(PlayerRepository playerRepository, PostgresService postgresService, Space space, ActionQueueRepository actionQueueRepository, PlayerShipEquipmentRepository playerShipEquipmentRepository, JacksonService jacksonService, PlayerRow playerRow) {
		this.playerRepository = playerRepository;
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
	@JsonValue
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

	/**
	 * Setter method.
	 *
	 * TODO rename method to "rename"
	 *
	 * @param name the name
	 */
	public void setName(String name) throws NameAlreadyUsedException {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}
		if (!playerRepository.isRenamePossible(getId(), name)) {
			throw new NameAlreadyUsedException();
		}
		boolean updateShipName = getShip().getName().equals(generateShipName(getName()));
		updateField(QPlayerRow.Player.name, name);
		if (updateShipName) {
			getShip().setName(generateShipName(name));
		}
	}

	private static String generateShipName(String playerName) {
		return playerName + "'s ship";
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

	/**
	 * Getter method.
	 *
	 * @return the ship
	 */
	public PlayerShip getShip() {
		return (PlayerShip) space.get(shipId);
	}

	/**
	 * Getter method.
	 *
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return getShip().getInventory();
	}

	/**
	 * Getter method.
	 *
	 * @return the action queue
	 */
	public ActionQueue getActionQueue() {
		return actionQueueRepository.getActionQueue(actionQueueId);
	}

	/**
	 * Getter method.
	 *
	 * @return the player ship equipment
	 */
	public PlayerShipEquipment getEquipment() {
		return playerShipEquipmentRepository.getPlayerShipEquipment(shipId);
	}

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick(PostgresConnection connection) {
		getActionQueue().tick(connection);
	}

	/**
	 * Recalculates player attributes. This method must be called after changing anything that affects attributes,
	 * such as skill or equipment.
	 */
	public void updateAttributes() {

		// set up base values
		Map<PlayerAttributeKey, Integer> attributes = new HashMap<>();
		attributes.put(PlayerAttributeKey.SHIP_MOVEMENT_SPEED, 50_000);
		attributes.put(PlayerAttributeKey.MAXIMUM_CARGO_MASS, 10_000);

		// apply equipment bonus
		for (SlotInfo slotInfo : getEquipment().getAllSlots()) {
			for (Map.Entry<PlayerAttributeKey, Integer> bonusEntry : slotInfo.getItemType().getPlayerBonus().entrySet()) {
				PlayerAttributeKey key = bonusEntry.getKey();
				attributes.put(key, attributes.get(key) + bonusEntry.getValue());
			}
		}

		// update the attributes in the database
		// TODO consider moving transaction control to a central class because it affects all domain objects anyway
		try (PostgresConnection connection = postgresService.newConnection()) {
			QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
			connection.getJdbcConnection().setAutoCommit(false);
			connection.delete(qa).where(qa.playerId.eq(id)).execute();
			for (Map.Entry<PlayerAttributeKey, ?> entry : attributes.entrySet()) {
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

	/**
	 * Gets a player attribute by key.
	 *
	 * @param key the key
	 * @return the attribute value
	 */
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

	/**
	 * @return the movement speed of the player's ship
	 */
	public long getShipMovementSpeed() {
		return ((Number)getAttribute(PlayerAttributeKey.SHIP_MOVEMENT_SPEED)).longValue();
	}

	/**
	 * @return the maximum cargo mass that can be loaded into the player's ship
	 */
	public int getMaximumCargoMass() {
		return ((Number)getAttribute(PlayerAttributeKey.MAXIMUM_CARGO_MASS)).intValue();
	}

}
