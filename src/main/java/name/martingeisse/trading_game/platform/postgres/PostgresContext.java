package name.martingeisse.trading_game.platform.postgres;

/**
 * Wraps a {@link PostgresConnection} and adds per-context caching capabilities. Note that no caching is done
 * implicitly since it might impact correctness. Per-thread contexts are managed by {@link PostgresThreadContextService}
 * to allow per-thread connection re-use, which is important for controlling serial/parallel execution of queries
 * and transaction management.
 * <p>
 * This class is NOT thread-safe! The typical usage pattern is to create, use and dispose of a context within a single
 * thread. If a context must be used by multiple threads, e.g. to share a database transaction, then the calling code
 * must ensure thread safety itself.
 * <p>
 * This context initializes its properties lazily. It gets disposed by calling reset(), which returns it to its
 * initial state.
 * <p>
 * Closing this context is the same as resetting it. This is done to support try-with-resource. The close() method is
 * therefore not idempotent when other calls happen in between, since the second close() call will reset the context
 * again instead of having no effect. This behavior is allowed by {@link AutoCloseable} and seems obvious in this case.
 */
public final class PostgresContext implements AutoCloseable {

	private final PostgresService postgresService;
	private PostgresConnection connection;

	/**
	 * Constructor.
	 *
	 * @param postgresService the postgres service
	 */
	public PostgresContext(PostgresService postgresService) {
		this.postgresService = postgresService;
	}

	/**
	 * Getter method.
	 *
	 * @return the connection
	 */
	public PostgresConnection getConnection() {
		if (connection == null) {
			connection = postgresService.newConnection();
		}
		return connection;
	}

	/**
	 * Resets the context to its initial state.
	 */
	public void reset() {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	@Override
	public void close() {
		reset();
	}

}
