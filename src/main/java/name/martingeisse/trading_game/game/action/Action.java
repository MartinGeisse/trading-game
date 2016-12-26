package name.martingeisse.trading_game.game.action;

/**
 * An action which can be executed.
 *
 * An action object is passive unless executed: Creating and discarding it doesn't have any effects on the game.
 *
 * Actions crested for a specific player or manufacturing site should only be used in that context.
 */
public interface Action {

	/**
	 * @return the total time needed to execute the action, or null if unknown or not applicable for this action.
	 */
	public Integer getTotalTime();

	/**
	 * Starts the execution of this action.
	 *
	 * The execution must be finished or cancelled; you cannot just discard it. For example, manufacturing actions
	 * remove the input items from an inventory when the action execution is started. These items will be lost when
	 * the action is discarded without being properly cancelled.
	 */
	public ActionExecution startExecution();

}
