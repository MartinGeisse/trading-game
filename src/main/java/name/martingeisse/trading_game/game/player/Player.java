package name.martingeisse.trading_game.game.player;

import com.fasterxml.jackson.annotation.JsonValue;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.game.NameAlreadyUsedException;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.action.ActionQueueRepository;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentRepository;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import name.martingeisse.trading_game.postgres_entities.QCachedPlayerAttributeRow;

import java.sql.SQLException;

/**
 *
 */
public final class Player {

	private final PostgresService postgresService;
	private final PlayerRepository playerRepository;
	private final Space space;
	private final ActionQueueRepository actionQueueRepository;
	private final PlayerShipEquipmentRepository playerShipEquipmentRepository;
	private final PlayerAttributeValueSerializer playerAttributeValueSerializer;
	private final long id;
	private final long shipId;
	private final long actionQueueId;
	private String name;

	Player(PostgresService postgresService, PlayerRepository playerRepository, Space space, ActionQueueRepository actionQueueRepository, PlayerShipEquipmentRepository playerShipEquipmentRepository, PlayerAttributeValueSerializer playerAttributeValueSerializer, PlayerRow playerRow) {
		this.postgresService = postgresService;
		this.playerRepository = playerRepository;
		this.space = space;
		this.actionQueueRepository = actionQueueRepository;
		this.playerShipEquipmentRepository = playerShipEquipmentRepository;
		this.playerAttributeValueSerializer = playerAttributeValueSerializer;
		this.id = playerRow.getId();
		this.shipId = playerRow.getShipId();
		this.actionQueueId = playerRow.getActionQueueId();
		this.name = playerRow.getName();
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

	/**
	 * Getter method.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method.
	 *
	 * @param name the name
	 */
	public void setName(String name) throws NameAlreadyUsedException {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}
		if (!playerRepository.isRenamePossible(id, name)) {
			throw new NameAlreadyUsedException();
		}
		String oldName = this.name;
		this.name = name;
		if (getShip().getName().equals(generateName(oldName))) {
			setShipName();
		}
	}

	private void setShipName() {
		getShip().setName(generateName(name));
	}

	private static String generateName(String playerName) {
		return playerName + "'s ship";
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

		// TODO calculate these based on equipment and skills
		long shipMovementSpeed = 50_000;
		long maximumCargoMass = 10_000;

		// update the attributes in the database
		try (PostgresConnection connection = postgresService.newConnection()) {
			QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
			connection.getJdbcConnection().setAutoCommit(false);
			connection.delete(qa).where(qa.playerId.eq(id)).execute();
			insertAttribute(connection, PlayerAttributeKey.SHIP_MOVEMENT_SPEED, shipMovementSpeed);
			insertAttribute(connection, PlayerAttributeKey.MAXIMUM_CARGO_MASS, maximumCargoMass);
			connection.getJdbcConnection().commit();
		} catch (SQLException e) {
			throw new UnexpectedExceptionException(e);
		}

	}

	private void insertAttribute(PostgresConnection connection, PlayerAttributeKey key, Object value) {
		String serializedValue = playerAttributeValueSerializer.serializePlayerAttributeValue(value);
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
		return playerAttributeValueSerializer.deserializePlayerAttributeValue(serializedValue);
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
