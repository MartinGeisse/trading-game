package name.martingeisse.trading_game.game.player;

import com.fasterxml.jackson.annotation.JsonValue;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.GameLogicException;
import name.martingeisse.trading_game.game.NameAlreadyUsedException;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.definition.GameConstants;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.equipment.SlotInfo;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.market.NotEnoughMoneyException;
import name.martingeisse.trading_game.game.skill.PlayerSkills;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.game.space.SpaceStation;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import name.martingeisse.trading_game.postgres_entities.QCachedPlayerAttributeRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class Player {

	private final PlayerRepository playerRepository;
	private final PostgresContextService postgresContextService;
	private final EntityProvider entityProvider;
	private final JacksonService jacksonService;
	private final Space space;
	private final long id;
	private final long shipId;
	private final long actionQueueId;

	public Player(PlayerRepository playerRepository, PostgresContextService postgresContextService, EntityProvider entityProvider, JacksonService jacksonService, Space space, PlayerRow playerRow) {
		this.playerRepository = playerRepository;
		this.postgresContextService = postgresContextService;
		this.entityProvider = entityProvider;
		this.jacksonService = jacksonService;
		this.space = space;
		this.id = playerRow.getId();
		this.shipId = playerRow.getShipId();
		this.actionQueueId = playerRow.getActionQueueId();
	}

	@JsonValue
	public long getId() {
		return id;
	}

	private <T> T getField(Path<T> path) {
		QPlayerRow qp = QPlayerRow.Player;
		return postgresContextService.select(path).from(qp).where(qp.id.eq(id)).fetchFirst();
	}

	private <T> void setField(Path<T> path, T newValue) {
		QPlayerRow qp = QPlayerRow.Player;
		postgresContextService.update(qp).set(path, newValue).where(qp.id.eq(id)).execute();
	}

	private <T> void setField(Path<T> path, Expression<T> newValue) {
		QPlayerRow qp = QPlayerRow.Player;
		postgresContextService.update(qp).set(path, newValue).where(qp.id.eq(id)).execute();
	}

	public String getName() {
		return getField(QPlayerRow.Player.name);
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
		setField(QPlayerRow.Player.name, name);
		if (updateShipName) {
			getShip().setName(generateShipName(name));
		}
	}

	private static String generateShipName(String playerName) {
		return playerName + "'s ship";
	}

	public String getLoginToken() {
		return getField(QPlayerRow.Player.loginToken);
	}

	public void setLoginToken(String loginToken) {
		setField(QPlayerRow.Player.loginToken, loginToken);
	}

	public String getEmailAddress() {
		return getField(QPlayerRow.Player.emailAddress);
	}

	public void setEmailAddress(String emailAddress) {
		setField(QPlayerRow.Player.emailAddress, emailAddress);
	}

	public long getMoney() {
		return getField(QPlayerRow.Player.money);
	}

	public void addMoney(long amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount is negative");
		}
		if (amount == 0) {
			return;
		}
		setField(QPlayerRow.Player.money, getField(QPlayerRow.Player.money) + amount);
	}

	public void subtractMoney(long amount) throws NotEnoughMoneyException {
		if (amount < 0) {
			throw new IllegalArgumentException("amount is negative");
		}
		if (amount == 0) {
			return;
		}
		long currentMoney = getField(QPlayerRow.Player.money);
		if (amount > currentMoney) {
			throw new NotEnoughMoneyException();
		}
		setField(QPlayerRow.Player.money, currentMoney - amount);
	}

	/**
	 * Getter method.
	 *
	 * @return the ship
	 */
	public PlayerShip getShip() {
		return (PlayerShip) entityProvider.getSpaceObject(shipId);
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

	/**
	 * Getter method.
	 *
	 * @return the player skills
	 */
	public PlayerSkills getSkills() {
		return entityProvider.getPlayerSkills(id);
	}

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick() {
		long remainingPlayTime = getField(QPlayerRow.Player.remainingPlayTime);
		if (remainingPlayTime >= 1) {
			getActionQueue().tick();
			getSkills().tick();
			setField(QPlayerRow.Player.remainingPlayTime, QPlayerRow.Player.remainingPlayTime.subtract(1L));
		}
	}

	/**
	 * Recalculates player attributes. This method must be called after changing anything that affects attributes,
	 * such as skill or equipment.
	 */
	public void updateAttributes() {
		QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;

		// set up base values
		Map<PlayerAttributeKey, Integer> attributes = new HashMap<>();
		attributes.put(PlayerAttributeKey.SHIP_MOVEMENT_SPEED, 2);
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
		postgresContextService.delete(qa).where(qa.playerId.eq(id)).execute();
		for (Map.Entry<PlayerAttributeKey, ?> entry : attributes.entrySet()) {
			String serializedValue = jacksonService.serialize(entry.getValue());
			postgresContextService.insert(qa).set(qa.playerId, id).set(qa.key, entry.getKey()).set(qa.value, serializedValue).execute();
		}

	}

	/**
	 * Gets a player attribute by key.
	 *
	 * @param key the key
	 * @return the attribute value
	 */
	public Object getAttribute(PlayerAttributeKey key) {
		QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
		String serializedValue = postgresContextService.select(qa.value).from(qa).where(qa.playerId.eq(id), qa.key.eq(key)).fetchFirst();
		if (serializedValue == null) {
			throw new IllegalStateException("missing player attribute " + key + " for player ID " + id);
		}
		return jacksonService.deserialize(serializedValue, Object.class);
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
		SpaceObject result = space.get(ship.getX(), ship.getY(), SpaceObject.ITEM_LOADING_MAX_DISTANCE, (o1, o2) -> {
			int i1 = (o1 instanceof SpaceStation) ? 1 : 0;
			int i2 = (o2 instanceof SpaceStation) ? 1 : 0;
			return i1 - i2;
		});
		return (result instanceof SpaceStation) ? (SpaceStation) result : null;
	}

	/**
	 * The argument should be the total credits for this player on FaH. This method checks if the player has "spent"
	 * less than this total, and if so, "spends" the remaining credits for additional play time.
	 */
	public void updatePlayTimeCredits(long totalEarnedCredits) {
		long spentCredits = getField(QPlayerRow.Player.spentFoldingCredits);
		if (spentCredits < totalEarnedCredits) {
			long additionalPlayTime = (totalEarnedCredits - spentCredits) * GameConstants.PLAY_TIME_SECONDS_PER_FAH_CREDIT;
			setField(QPlayerRow.Player.spentFoldingCredits, totalEarnedCredits);
			setField(QPlayerRow.Player.remainingPlayTime, QPlayerRow.Player.remainingPlayTime.add(additionalPlayTime));
		}
	}

	public long getSpentFoldingCredits() {
		return getField(QPlayerRow.Player.spentFoldingCredits);
	}

	public long getRemainingPlayTime() {
		return getField(QPlayerRow.Player.remainingPlayTime);
	}

	public String getFoldingUserHash() {
		return getField(QPlayerRow.Player.foldingUserHash);
	}

}
