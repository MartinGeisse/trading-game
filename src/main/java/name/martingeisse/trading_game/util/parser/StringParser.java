package name.martingeisse.trading_game.util.parser;

/**
 * This parser just returns the parsed text.
 */
public final class StringParser implements Parser<String> {

	/**
	 * The shared instance of this class.
	 */
	public static final StringParser INSTANCE = new StringParser();

	// override
	@Override
	public String parse(final String s) {
		return s;
	}

}
