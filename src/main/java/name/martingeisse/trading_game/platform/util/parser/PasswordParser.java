package name.martingeisse.trading_game.platform.util.parser;

/**
 * This parser just returns the parsed text. In contrast to {@link StringParser}, it is used to
 * indicate that the parsed text is a password. This is used, for example, by Swagger UI to mask the
 * input value.
 */
public final class PasswordParser implements Parser<String> {

	/**
	 * The shared instance of this class.
	 */
	public static final PasswordParser INSTANCE = new PasswordParser();

	// override
	@Override
	public String parse(final String s) {
		return s;
	}

}
