package name.martingeisse.trading_game.game.player;

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

/**
 *
 */
public final class Player {

	private final PostgresService postgresService;
	private final PlayerRepository playerRepository;
	private final Space space;
	private final ActionQueueRepository actionQueueRepository;
	private final PlayerShipEquipmentRepository playerShipEquipmentRepository;
	private final long id;
	private final long shipId;
	private final long actionQueueId;
	private String name;

	public Player(PostgresService postgresService, PlayerRepository playerRepository, Space space, ActionQueueRepository actionQueueRepository, PlayerShipEquipmentRepository playerShipEquipmentRepository, PlayerRow playerRow) {
		this.postgresService = postgresService;
		this.playerRepository = playerRepository;
		this.space = space;
		this.actionQueueRepository = actionQueueRepository;
		this.playerShipEquipmentRepository = playerShipEquipmentRepository;
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
		return (PlayerShip)space.get(shipId);
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
	 * @return the movement speed of the player's ship
	 */
	public long getShipMovementSpeed() {
		return 50000;
	}

	/**
	 * @return the maximum cargo mass that can be loaded into the player's ship
	 */
	public int getMaximumCargoMass() {
		return 10_000;
	}

}
