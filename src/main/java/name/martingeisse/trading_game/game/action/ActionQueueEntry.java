package name.martingeisse.trading_game.game.action;

/**
 *
 */
public final class ActionQueueEntry {

	private final Action action;
	private final boolean prerequisite;

	/**
	 * Constructor.
	 *
	 * @param action       the action
	 * @param prerequisite whether the action is a prerequisite action
	 */
	public ActionQueueEntry(Action action, boolean prerequisite) {
		this.action = action;
		this.prerequisite = prerequisite;
	}

	/**
	 * Getter method.
	 *
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Getter method.
	 *
	 * @return the prerequisite
	 */
	public boolean isPrerequisite() {
		return prerequisite;
	}

}
