package name.martingeisse.trading_game.game.action.take2;

/**
 * Indicates that an action cannot be started due to failed preconditions. If possible, the action should have reported
 * any prerequisite action to satisfy the preconditions; if this exception still occurs, then either the caller ignored
 * those prerequisite actions or the action itself isn't able to give any useful prerequisite action to satisfy the
 * preconditions.
 */
public class CannotStartActionException extends Exception {

	public CannotStartActionException() {
	}

	public CannotStartActionException(String message) {
		super(message);
	}

}
