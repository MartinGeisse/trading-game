package name.martingeisse.trading_game.common.util;

/**
 * This exception gets thrown when something happens that "cannot happen".
 */
public class WtfException extends RuntimeException {

	public WtfException() {
	}

	public WtfException(String message) {
		super(message);
	}

	public WtfException(String message, Throwable cause) {
		super(message, cause);
	}

	public WtfException(Throwable cause) {
		super(cause);
	}

}
