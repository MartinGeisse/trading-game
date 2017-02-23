package name.martingeisse.trading_game.tools.codegen;

/**
 * Wrapper around a string to represent PostgreSQL's jsonb type.
 */
public final class PostgresJsonb {

	private final String value;

	/**
	 * Constructor.
	 *
	 * @param value the value
	 */
	public PostgresJsonb(String value) {
		this.value = value;
	}

	/**
	 * Getter method.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

}
