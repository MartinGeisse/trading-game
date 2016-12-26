package name.martingeisse.trading_game.game.action;

/**
 *
 */
public interface ActionExecution {

	/**
	 * @return the name of the action
	 */
	public String getName();

	/**
	 * @return a snapshot of the current and required progress points, or null if unknown or not applicable for this action.
	 */
	public ProgressSnapshot getProgress();

	/**
	 * @return the remaining time for this action in seconds, or null if unknown or not applicable for this action.
	 */
	public Integer getRemainingTime();

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick();

	/**
	 * Cancels this action execution. This makes this object invalid to use afterwards.
	 */
	public void cancel();

	/**
	 * @return whether this action can be finished.
	 */
	public boolean isFinishable();

	/**
	 * Finishes this action. This makes this object invalid to use afterwards.
	 */
	public void finish();

}
