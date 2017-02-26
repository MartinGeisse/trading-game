package name.martingeisse.trading_game.game.action;

/**
 *
 */
public interface Action {

	/**
	 * @return the name of the action
	 */
	public String getName();

	/**
	 * @return the total time needed to execute the action, or null if unknown or not applicable for this action.
	 */
	public Integer getTotalTime();

	/**
	 * Checks for prerequisite actions.
	 *
	 * @return the prerequisite action, if any, or null.
	 */
	public Action getPrerequisite();

	/**
	 * Starts the action.
	 *
	 * This method must not be called if the action has any prerequisite actions as returned by {@link #getPrerequisite()}.
	 * If called anyway, this action may fail by returning FAILED, apply inconsistent or undesired changes to the database,
	 * fail with an exception, and so on.
	 */
	public Status start();

	/**
	 * @return a snapshot of the current and required progress points, or null if unknown or not applicable for this action.
	 */
	public ProgressSnapshot getProgress();

	/**
	 * @return the remaining time for this action in seconds, or null if unknown or not applicable for this action.
	 */
	public Integer getRemainingTime();

	/**
	 * Cancels this action.
	 */
	public void cancel();

	/**
	 * Called once every second to advance the game logic.
	 *
	 * @return the status of this action
	 */
	public Status tick();

	/**
	 * Returned by {@link #tick()} to indicate the status of the action.
	 */
	public enum Status {

		/**
		 * The action is currently being executed. {@link #tick()} should ne called again.
		 */
		RUNNING,

		/**
		 * The action has been finished. {@link #tick()} should not be called anymore.
		 */
		FINISHED,

		/**
		 * The action has failed. {@link #tick()} should not be called anymore.
		 */
		FAILED;

	}

}
