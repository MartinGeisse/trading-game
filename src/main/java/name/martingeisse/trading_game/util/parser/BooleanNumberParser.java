package name.martingeisse.trading_game.util.parser;

/**
 * This parser parses boolean values encoded as 0 (false) or 1 (true).
 */
public class BooleanNumberParser implements Parser<Boolean> {

	/**
	 * The shared instance of this class.
	 */
	public static final BooleanNumberParser INSTANCE = new BooleanNumberParser();

	// override
	@Override
	public Boolean parse(final String s) throws ParserException {
		if ("0".equals(s)) {
			return Boolean.FALSE;
		} else if ("1".equals(s)) {
			return Boolean.TRUE;
		} else {
			throw new ParserException();
		}
	}

}
