package name.martingeisse.trading_game.common.database;

import java.sql.Connection;

/**
 * Implemented by services that provide database connections.
 */
public interface DatabaseService {

	/**
	 * @return
	 */
	public Connection newJdbcConnection();

	/**
	 * @return
	 */
	public DatabaseConnection newConnection();

}
