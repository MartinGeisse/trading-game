package name.martingeisse.trading_game.platform.util.parameter;

/**
 * Indicates a parameter with an invalid format.
 */
public class ParameterFormatException extends ParameterException {

	/**
	 * Constructor.
	 *
	 * @param name the parameter name
	 */
	public ParameterFormatException(final String name) {
		super(name, "invalid value");
	}

}
