package name.martingeisse.trading_game.game;

/**
 * This exception type indicates a logcial problem while performing game code, not a technical problem. These kinds of
 * problems should be reported to the user.
 * <p>
 * However, only report the exception message (and possibly extra fields) to the user, not the cause exception. The
 * cause may be used to analyze problems in a debugging context and are not meaningful to the user.
 */
public class GameLogicException extends Exception {

	public GameLogicException() {
	}

	public GameLogicException(String message) {
		super(message);
	}

	public GameLogicException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameLogicException(Throwable cause) {
		super(cause);
	}

}
