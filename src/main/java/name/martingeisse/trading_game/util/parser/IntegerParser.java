package name.martingeisse.trading_game.util.parser;

/**
 * This parser parses integer values.
 */
public class IntegerParser implements Parser<Integer> {

	/**
	 * The shared instance of this class.
	 */
	public static final IntegerParser INSTANCE = new IntegerParser();

	// override
	@Override
	public Integer parse(final String s) throws ParserException {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new ParserException();
		}
	}

}
