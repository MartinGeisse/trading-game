package name.martingeisse.trading_game.platform.postgres;

/**
 * Manages per-thread {@link PostgresContext} objects.
 *
 * TODO logging
 */
public class PostgresThreadContextService {

	private final PostgresService postgresService;
	private final ThreadLocal<PostgresContext> contexts = new ThreadLocal<>();

	public PostgresThreadContextService(PostgresService postgresService) {
		this.postgresService = postgresService;
	}

	public void withNewContext(Runnable runnable) {
		PostgresContext oldContext = contexts.get();
		try (PostgresContext context = new PostgresContext(postgresService)) {
			runnable.run();
		} finally {
			contexts.set(oldContext);
		}
	}

	public PostgresContext getContext() {
		return contexts.get();
	}

}
