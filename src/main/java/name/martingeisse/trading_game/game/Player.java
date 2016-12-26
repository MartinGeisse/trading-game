package name.martingeisse.trading_game.game;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionExecution;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.skill.PlayerSkills;
import name.martingeisse.trading_game.game.space.PlayerShip;

/**
 *
 */
public final class Player {

	private final Game game;
	private final String id;
	private final PlayerShip ship;
	private String name;
	private final ActionQueue pendingActions = new ActionQueue();
	private ActionExecution actionExecution;
	private final PlayerSkills skills;

	public Player(Game game, String id, PlayerShip ship) {
		this.game = game;
		this.id = id;
		this.ship = ship;
		this.name = "Player " + id;
		this.skills = new PlayerSkills();
		setShipName();
	}

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
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
		if (!game.isRenamePossible(this, name)) {
			throw new NameAlreadyUsedException();
		}
		String oldName = this.name;
		this.name = name;
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
		pendingActions.enqueue(action);
	}

	/**
	 * Schedules an action to be performed after all currently pending actions.
	 *
	 * @param repetitions how often the action shall be repeated
	 * @param action      the action to schedule
	 */
	public void scheduleAction(int repetitions, Action action) {
		pendingActions.enqueue(repetitions, action);
	}

	/**
	 * Cancels the currently executed action (if any).
	 */
	public void cancelCurrentAction() {
		actionExecution.cancel();
		actionExecution = null;
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
	 * Getter method.
	 *
	 * @return the skills
	 */
	public PlayerSkills getSkills() {
		return skills;
	}

	/**
	 * Called once every second to advance game logic.
	 */
	void tick() {
		while (actionExecution == null && !pendingActions.isEmpty()) {
			actionExecution = pendingActions.dequeue().startExecution();
		}
		if (actionExecution != null) {
			actionExecution.tick();
			if (actionExecution.isFinishable()) {
				actionExecution.finish();
				actionExecution = null;
			}
		}
		skills.tick();
	}

	public long getShipMovementSpeed() {
		return 10; // TODO
	}

}
