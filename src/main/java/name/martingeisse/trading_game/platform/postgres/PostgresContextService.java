package name.martingeisse.trading_game.platform.postgres;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This class gives access to a per-thread {@link PostgresContext}. It also provides the same
 * methods for convenience, which delegate to the calling thread's context.
 */
@Singleton
public final class PostgresContextService {

	private final PostgresService postgresService;
	private final ThreadLocal<PostgresContext> contexts = new ThreadLocal<>();

	@Inject
	public PostgresContextService(PostgresService postgresService) {
		this.postgresService = postgresService;
	}

	public PostgresContext getContext() {
		PostgresContext context = contexts.get();
		if (context == null) {
			context = new PostgresContext(postgresService);
		}
		return context;
	}

	public PostgresConnection getConnection() {
		return getContext().getConnection();
	}

	public void reset() {
		// The context itself is lightweight, so we can keep it and avoid problems with other calling code keeping
		// a direct reference to it.
		getContext().reset();
	}

	public void setAutoCommit(boolean autoCommit) {
		getContext().setAutoCommit(autoCommit);
	}

	public void commit() {
		getContext().commit();
	}

	public void rollback() {
		getContext().rollback();
	}

}
