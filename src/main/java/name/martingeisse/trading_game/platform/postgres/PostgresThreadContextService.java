package name.martingeisse.trading_game.platform.postgres;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Manages per-thread {@link PostgresContext} objects.
 *
 * TODO logging
 */
@Singleton
public class PostgresThreadContextService {

	private final PostgresService postgresService;
	private final ThreadLocal<PostgresContext> contexts = new ThreadLocal<>();

	@Inject
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
