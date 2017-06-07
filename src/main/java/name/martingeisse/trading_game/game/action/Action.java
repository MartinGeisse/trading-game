package name.martingeisse.trading_game.game.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public interface Action {

	/**
	 * @return the name of the action
	 */
	@JsonIgnore
	public String getName();

	/**
	 * @return the Glyphicon name of the action
	 */
	@JsonIgnore
	public String getGlyphiconName();

	/**
	 * @return the total time needed to execute the action, or null if unknown or not applicable for this action.
	 */
	@JsonIgnore
	public Integer getTotalTime();

	/**
	 * Checks for prerequisite actions.
	 *
	 * @return the prerequisite action, if any, or null.
	 */
	@JsonIgnore
	public Action getPrerequisite();

	/**
	 * Starts the action.
	 * <p>
	 * This method must not be called if the action has any prerequisite actions as returned by {@link #getPrerequisite()}.
	 * If called anyway, this action may fail by returning FAILED, apply inconsistent or undesired changes to the database,
	 * fail with an exception, and so on.
	 */
	@JsonIgnore
	public Status start();

	/**
	 * @return a snapshot of the current and required progress points, or null if unknown or not applicable for this action.
	 */
	@JsonIgnore
	public ProgressSnapshot getProgress();

	/**
	 * @return the remaining time for this action in seconds, or null if unknown or not applicable for this action.
	 */
	@JsonIgnore
	public Integer getRemainingTime();

	/**
	 * Cancels this action.
	 */
	@JsonIgnore
	public void cancel();

	/**
	 * Called once every second to advance the game logic.
	 *
	 * @return the status of this action
	 */
	@JsonIgnore
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
