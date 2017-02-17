package name.martingeisse.trading_game.util.parser;

/**
 * Indicates that a parameter value could not be parsed.
 */
public final class ParserException extends Exception {

	/**
	 * Constructor.
	 */
	public ParserException() {
		super();
	}

	/**
	 * Constructor.
	 *
	 * @param message the exception message (usually not relevant for parser exceptions, except for debugging)
	 * @param cause   the cause (usually not relevant for parser exceptions, except for debugging)
	 */
	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 *
	 * @param message the exception message (usually not relevant for parser exceptions, except for debugging)
	 */
	public ParserException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 *
	 * @param cause the cause (usually not relevant for parser exceptions, except for debugging)
	 */
	public ParserException(Throwable cause) {
		super(cause);
	}

}
