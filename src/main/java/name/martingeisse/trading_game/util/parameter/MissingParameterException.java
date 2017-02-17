package name.martingeisse.trading_game.util.parameter;

/**
 * Indicates a missing parameter.
 */
public class MissingParameterException extends ParameterException {

	/**
	 * Constructor.
	 *
	 * @param name the parameter name
	 */
	public MissingParameterException(String name) {
		super(name, "parameter is missing");
	}

}
