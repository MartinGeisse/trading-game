package name.martingeisse.trading_game.util.parameter;

/**
 * Indicates a problem with a parameter.
 */
public class ParameterException extends Exception {

	private final String name;

	/**
	 * Constructor.
	 *
	 * @param name    the parameter name
	 * @param message the message for that parameter
	 */
	public ParameterException(final String name, final String message) {
		super("error for parameter '" + name + "': " + message);
		this.name = name;
	}

	/**
	 * Getter method for the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
