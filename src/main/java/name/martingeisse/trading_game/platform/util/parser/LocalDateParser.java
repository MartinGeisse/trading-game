package name.martingeisse.trading_game.platform.util.parser;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Parses yyyy-MM-dd (local) dates.
 */
public final class LocalDateParser implements Parser<LocalDate> {

	/**
	 * The shared instance of this class.
	 */
	public static final LocalDateParser INSTANCE = new LocalDateParser();

	private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	@Override
	public LocalDate parse(String s) throws ParserException {
		try {
			return formatter.parseLocalDate(s);
		} catch (IllegalArgumentException e) {
			throw new ParserException("invalid date: " + s);
		}
	}

}
