package name.martingeisse.trading_game.game.player;

import com.fasterxml.jackson.annotation.JsonValue;
import name.martingeisse.trading_game.game.NameAlreadyUsedException;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.equipment.SlotInfo;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class Player {

	private final PlayerRepository playerRepository;
	private final PlayerDataLink dataLink;

	public Player(PlayerRepository playerRepository, PlayerDataLink dataLink) {
		this.playerRepository = playerRepository;
		this.dataLink = dataLink;
	}

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	@JsonValue
	public long getId() {
		return dataLink.getId();
	}

	/**
	 * Getter method.
	 *
	 * @return the name
	 */
	public String getName() {
		return dataLink.getName();
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
		if (!playerRepository.isRenamePossible(getId(), name)) {
			throw new NameAlreadyUsedException();
		}
		boolean updateShipName = getShip().getName().equals(generateShipName(getName()));
		dataLink.setName(name);
		if (updateShipName) {
			getShip().setName(generateShipName(name));
		}
	}

	private static String generateShipName(String playerName) {
		return playerName + "'s ship";
	}

	public String getLoginToken() {
		return dataLink.getLoginToken();
	}

	public String getEmailAddress() {
		return dataLink.getEmailAddress();
	}

	public void setEmailAddress(String emailAddress) {
		dataLink.setEmailAddress(emailAddress);
	}

	/**
	 * Getter method.
	 *
	 * @return the ship
	 */
	public PlayerShip getShip() {
		return dataLink.getShip();
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
		return dataLink.getActionQueue();
	}

	/**
	 * Getter method.
	 *
	 * @return the player ship equipment
	 */
	public PlayerShipEquipment getEquipment() {
		return dataLink.getEquipment();
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
		dataLink.replacePlayerAttributes(attributes);

	}

	/**
	 * Gets a player attribute by key.
	 *
	 * @param key the key
	 * @return the attribute value
	 */
	public Object getAttribute(PlayerAttributeKey key) {
		return dataLink.getAttribute(key);
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
