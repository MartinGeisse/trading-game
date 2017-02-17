package name.martingeisse.trading_game.platform.util.parser;

/**
 * This parser parses enum constants, except that they are expected to be written in lowercase.
 *
 * @param <E> the enum type
 */
public class EnumParser<E extends Enum<E>> implements Parser<E> {

	private final Class<E> enumClass;

	/**
	 * Constructor.
	 *
	 * @param enumClass the enum class to parse
	 */
	public EnumParser(Class<E> enumClass) {
		this.enumClass = enumClass;
	}

	// override
	@Override
	public E parse(final String s) throws ParserException {
		if (!s.toLowerCase().equals(s)) {
			throw new ParserException("string is not lower-case");
		}
		String upper = s.toUpperCase();
		try {
			return Enum.valueOf(enumClass, upper);
		} catch (IllegalArgumentException e) {
			throw new ParserException("unknown enum constant: " + upper);
		}
	}

}
