package name.martingeisse.trading_game.util;

/**
 * Indicates that another class has thrown an exception it was not supposed to throw the way it was
 * used.
 */
public class UnexpectedExceptionException extends RuntimeException {

	/**
	 * Constructor.
	 *
	 * @param cause the original exception
	 */
	public UnexpectedExceptionException(Throwable cause) {
		super("unexpected exception", cause);
	}

	/**
	 * Constructor.
	 *
	 * @param message a message explaining where the original exception happened
	 * @param cause   the original exception
	 */
	public UnexpectedExceptionException(String message, Throwable cause) {
		super(message, cause);
	}

}
