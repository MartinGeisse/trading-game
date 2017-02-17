package name.martingeisse.trading_game.util.parameter;

import name.martingeisse.trading_game.util.parser.Parser;
import name.martingeisse.trading_game.util.parser.ParserException;

/**
 *
 */
class Util {

	// prevent instantiation
	private Util() {
	}

	public static <T> T parse(String name, String value, T onNullValue, Parser<T> parser) throws ParameterFormatException {
		if (value == null) {
			return onNullValue;
		}
		try {
			return parser.parse(value);
		} catch (ParserException e) {
			throw new ParameterFormatException(name);
		}
	}

}
