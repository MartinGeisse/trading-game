package name.martingeisse.trading_game.platform.postgres;

import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;

import java.sql.SQLException;

/**
 * This class gives access to a per-thread lazily initialized {@link PostgresConnection} and adds caching
 * capabilities. Note that no caching is done implicitly since it might impact correctness. Per-thread connection
 * management is important for controlling serial/parallel execution of queries and transaction management.
 * <p>
 * This context initializes its properties lazily. It gets disposed by calling reset(), which returns it to its
 * initial state.
 */
public final class PostgresContext {

	private static final ThreadLocal<PostgresConnection> connections = new ThreadLocal<>();
	private static PostgresService postgresService;

	public static void initialize(PostgresService postgresService) {
		PostgresContext.postgresService = postgresService;
	}

	public static PostgresConnection getConnection() {
		PostgresConnection connection = connections.get();
		if (connection == null) {
			connection = postgresService.newConnection();
			connections.set(connection);
		}
		return connection;
	}

	public static void reset() {
		PostgresConnection connection = connections.get();
		if (connection != null) {
			try {
				connection.close();
			} finally {
				connections.set(null);
			}
		}
	}

	public static void setAutoCommit(boolean autoCommit) {
		try {
			getConnection().getJdbcConnection().setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	public static void commit() {
		try {
			getConnection().getJdbcConnection().commit();
		} catch (SQLException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	public static void rollback() {
		try {
			getConnection().getJdbcConnection().rollback();
		} catch (SQLException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

}
