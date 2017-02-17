package name.martingeisse.trading_game.util;

import name.martingeisse.trading_game.util.parameter.ParameterException;

/**
 * Indicates a problem that a id not exist in a Database table
 */
public class DatabaseIdNotExistException extends ParameterException {

	/**
	 * Constructor.
	 *
	 * @param table      the table from
	 * @param fieldKey
	 * @param fieldValue
	 */
	public DatabaseIdNotExistException(String table, String fieldKey, String fieldValue) {
		super(fieldKey, "The " + fieldKey + "'" + fieldValue + "' does not exist in table" + table);
	}

}
