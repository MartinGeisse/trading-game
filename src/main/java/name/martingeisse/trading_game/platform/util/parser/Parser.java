package name.martingeisse.trading_game.platform.util.parser;

/**
 * A parser that parses a string to a value of type T.
 *
 * @param <T> the type of parsed value
 */
@FunctionalInterface
public interface Parser<T> {

	/**
	 * Parses a parameter value.
	 *
	 * @param s the string to parse (never null)
	 * @return the parsed value
	 * @throws ParserException if the string cannot be parsed
	 */
	public T parse(String s) throws ParserException;

}
