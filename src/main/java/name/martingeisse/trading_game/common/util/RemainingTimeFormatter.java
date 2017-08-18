package name.martingeisse.trading_game.common.util;

/**
 *
 */
public class RemainingTimeFormatter {

	private static final long MINUTE = 60;
	private static final long HOUR = 3600;
	private static final long DAY = 24 * 3600;

	/**
	 * Pretty-prints a remaining number of seconds.
	 */
	public static String format(long seconds) {
		if (seconds < MINUTE) {
			return seconds + "s";
		}
		StringBuilder builder = new StringBuilder();
		if (seconds > DAY) {
			builder.append(seconds / DAY).append("d ");
			seconds = seconds % DAY;
		}
		if (seconds > HOUR) {
			builder.append(seconds / HOUR).append(':');
			seconds = seconds % HOUR;
		}
		builder.append(seconds / MINUTE).append(':').append(seconds % MINUTE);
		return builder.toString();
	}

}
