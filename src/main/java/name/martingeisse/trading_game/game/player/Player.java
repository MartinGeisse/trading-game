package name.martingeisse.trading_game.game.player;

import com.fasterxml.jackson.annotation.JsonValue;
import name.martingeisse.trading_game.game.GameLogicException;
import name.martingeisse.trading_game.game.NameAlreadyUsedException;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.equipment.SlotInfo;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.market.NotEnoughMoneyException;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.game.space.SpaceStation;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

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

	public String getName() {
		return dataLink.getField(QPlayerRow.Player.name);
	}

	/**
	 * Setter method.
	 *
	 * @param name the name
	 */
	public void renameTo(String name) throws NameAlreadyUsedException {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}
		if (!playerRepository.isRenamePossible(getId(), name)) {
			throw new NameAlreadyUsedException();
		}
		boolean updateShipName = getShip().getName().equals(generateShipName(getName()));
		dataLink.setField(QPlayerRow.Player.name, name);
		if (updateShipName) {
			getShip().setName(generateShipName(name));
		}
	}

	private static String generateShipName(String playerName) {
		return playerName + "'s ship";
	}

	public String getLoginToken() {
		return dataLink.getField(QPlayerRow.Player.loginToken);
	}

	public void setLoginToken(String loginToken) {
		dataLink.setField(QPlayerRow.Player.loginToken, loginToken);
	}

	public String getEmailAddress() {
		return dataLink.getField(QPlayerRow.Player.emailAddress);
	}

	public void setEmailAddress(String emailAddress) {
		dataLink.setField(QPlayerRow.Player.emailAddress, emailAddress);
	}

	public long getMoney() {
		return dataLink.getField(QPlayerRow.Player.money);
	}

	public void addMoney(long amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount is negative");
		}
		if (amount == 0) {
			return;
		}
		dataLink.setField(QPlayerRow.Player.money, dataLink.getField(QPlayerRow.Player.money) + amount);
	}

	public void subtractMoney(long amount) throws NotEnoughMoneyException {
		if (amount < 0) {
			throw new IllegalArgumentException("amount is negative");
		}
		if (amount == 0) {
			return;
		}
		long currentMoney = dataLink.getField(QPlayerRow.Player.money);
		if (amount > currentMoney) {
			throw new NotEnoughMoneyException();
		}
		dataLink.setField(QPlayerRow.Player.money, currentMoney - amount);
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
	public void tick() {
		getActionQueue().tick();
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
		// TODO wrap in transaction
		dataLink.clearCachedAttributes();
		for (Map.Entry<PlayerAttributeKey, ?> entry : attributes.entrySet()) {
			dataLink.insertCachedAttribute(entry.getKey(), entry.getValue());
		}

	}

	/**
	 * Gets a player attribute by key.
	 *
	 * @param key the key
	 * @return the attribute value
	 */
	public Object getAttribute(PlayerAttributeKey key) {
		return dataLink.getCachedAttribute(key);
	}

	/**
	 * @return the movement speed of the player's ship
	 */
	public long getShipMovementSpeed() {
		return ((Number) getAttribute(PlayerAttributeKey.SHIP_MOVEMENT_SPEED)).longValue();
	}

	/**
	 * @return the maximum cargo mass that can be loaded into the player's ship
	 */
	public int getMaximumCargoMass() {
		return ((Number) getAttribute(PlayerAttributeKey.MAXIMUM_CARGO_MASS)).intValue();
	}

	public void transferMoneyTo(Player destination, long amount) throws GameLogicException {
		subtractMoney(amount); // throws GameLogicException if the player doesn't have enough money
		destination.addMoney(amount);
	}

	public SpaceStation getSpaceStationForItemLoading() {
		PlayerShip ship = getShip();
		return dataLink.getItemTransferSpaceStation(ship.getX(), ship.getY());
	}

}
