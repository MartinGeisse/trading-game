package name.martingeisse.trading_game.util.parser;

/**
 * This parser parses boolean values encoded as "false" (false) or "true" (true).
 */
public class BooleanWordParser implements Parser<Boolean> {

	/**
	 * The shared instance of this class.
	 */
	public static final BooleanWordParser INSTANCE = new BooleanWordParser();

	// override
	@Override
	public Boolean parse(final String s) throws ParserException {
		if ("false".equals(s)) {
			return Boolean.FALSE;
		} else if ("true".equals(s)) {
			return Boolean.TRUE;
		} else {
			throw new ParserException();
		}
	}

}
