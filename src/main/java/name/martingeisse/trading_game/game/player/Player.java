package name.martingeisse.trading_game.game.player;

import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.NameAlreadyUsedException;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionExecution;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.skill.PlayerSkills;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

/**
 *
 */
public final class Player {

	private final PostgresService postgresService;
	private final PlayerRepository playerRepository;

	private final Game game;
	private final long id;
	private final PlayerShip ship;
	private String name;
	private final ActionQueue pendingActions = new ActionQueue();
	private ActionExecution actionExecution;

	public Player(PostgresService postgresService, PlayerRepository playerRepository, long id) {
		this.postgresService = postgresService;
		this.playerRepository = playerRepository;
		// TODO merge constructors
		this.id = id;
	}

	public Player(Game game, long id, PlayerShip ship) {
		this.game = game;
		this.id = id;
		this.ship = ship;
		this.name = "Player " + id;
		setShipName();
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
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * Getter method.
	 *
	 * @return the ship
	 */
	public PlayerShip getShip() {
		return ship;
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
		// TODO update name in database
		if (ship.getName().equals(generateName(oldName))) {
			setShipName();
		}
	}

	private void setShipName() {
		ship.setName(generateName(name));
	}

	private static String generateName(String playerName) {
		return playerName + "'s ship";
	}

	/**
	 * Getter method.
	 *
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return ship.getInventory();
	}

	/**
	 * Getter method.
	 *
	 * @return the pendingActions
	 */
	public ActionQueue getPendingActions() {
		return pendingActions;
	}

	/**
	 * Getter method.
	 *
	 * @return the actionExecution
	 */
	public ActionExecution getActionExecution() {
		return actionExecution;
	}

	/**
	 * Schedules an action to be performed after all currently pending actions.
	 *
	 * @param action the action to schedule
	 */
	public void scheduleAction(Action action) {
		pendingActions.add(action);
	}

	/**
	 * Cancels the currently executed action (if any).
	 */
	public void cancelCurrentAction() {
		if (actionExecution != null) {
			actionExecution.cancel();
			actionExecution = null;
		}
	}

	/**
	 * Cancels the pending action at the specified index.
	 *
	 * @param index the index of the action to cancel
	 */
	public void cancelPendingAction(int index) {
		if (index >= 0 && index < pendingActions.size()) {
			pendingActions.remove(index);
		}
	}

	/**
	 * Cancels all pending actions (but not the current action).
	 */
	public void cancelAllPendingActions() {
		pendingActions.clear();
	}

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick(PostgresConnection connection) {
		while (actionExecution == null && !pendingActions.isEmpty()) {
			actionExecution = pendingActions.startNext();
		}
		if (actionExecution != null) {
			actionExecution.tick();
			if (actionExecution.isFinishable()) {
				actionExecution.finish();
				actionExecution = null;
			}
		}
	}

	public long getShipMovementSpeed() {
		return 50000; // TODO
	}

	public int getMaximumCargoMass() {
		return 10_000;
	}

}
