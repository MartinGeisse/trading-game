package name.martingeisse.trading_game.platform.util.parameter;

import name.martingeisse.trading_game.platform.util.parser.Parser;
import name.martingeisse.trading_game.platform.util.parser.ParserException;

/**
 * This interface represents querystring, x-www-form-urlencoded or similar parameters which are
 * represented as key/value pairs, where both the key and the value are strings.
 */
@FunctionalInterface
public interface Parameters {

	/**
	 * @param name the parameter name
	 * @return the specified parameter value, or null if the parameter is not present
	 */
	public String getOptionalParameter(String name);

	/**
	 * @param name         the parameter name
	 * @param defaultValue the default value
	 * @return the specified parameter value, or the default value if the parameter is not present
	 */
	public default String getOptionalParameter(String name, String defaultValue) {
		String value = getOptionalParameter(name);
		return value == null ? defaultValue : value;
	}

	/**
	 * @param name the parameter name
	 * @return the specified parameter value
	 * @throws MissingParameterException if the parameter is missing
	 */
	public default String getRequiredParameter(String name) throws MissingParameterException {
		String value = getOptionalParameter(name);
		if (value == null) {
			throw new MissingParameterException(name);
		}
		return value;
	}

	/**
	 * @param <T>    the static parameter type
	 * @param name   the parameter name
	 * @param parser the parameter parser
	 * @return the specified parameter value, or null if the parameter is not present
	 * @throws ParameterFormatException if the parser fails to parse the parameter
	 */
	public default <T> T getOptionalParameter(String name, Parser<T> parser) throws ParameterFormatException {
		String value = getOptionalParameter(name);
		return Util.parse(name, value, null, parser);
	}

	/**
	 * @param <T>          the static parameter type
	 * @param name         the parameter name
	 * @param defaultValue the default value
	 * @param parser       the parameter parser
	 * @return the specified parameter value, or the default value if the parameter is not present
	 * @throws ParameterFormatException if the parser fails to parse the parameter
	 */
	public default <T> T getOptionalParameter(String name, String defaultValue, Parser<T> parser) throws ParameterFormatException {
		String value = getOptionalParameter(name, defaultValue);
		return Util.parse(name, value, null, parser);
	}

	/**
	 * @param <T>          the static parameter type
	 * @param name         the parameter name
	 * @param parser       the parameter parser
	 * @param defaultValue the default value
	 * @return the specified parameter value, or the default value if the parameter is not present
	 * @throws ParameterFormatException if the parser fails to parse the parameter
	 */
	public default <T> T getOptionalParameter(String name, Parser<T> parser, T defaultValue) throws ParameterFormatException {
		String value = getOptionalParameter(name);
		return Util.parse(name, value, defaultValue, parser);
	}

	/**
	 * @param <T>    the static parameter type
	 * @param name   the parameter name
	 * @param parser the parameter parser
	 * @return the specified parameter value
	 * @throws MissingParameterException if the parameter is missing
	 * @throws ParameterFormatException  if the parser fails to parse the parameter
	 */
	public default <T> T getRequiredParameter(String name, Parser<T> parser) throws MissingParameterException, ParameterFormatException {
		try {
			return parser.parse(getRequiredParameter(name));
		} catch (ParserException e) {
			throw new ParameterFormatException(name);
		}
	}

}
