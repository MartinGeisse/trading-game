package name.martingeisse.trading_game.platform.postgres;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.postgresql.PostgreSQLQuery;

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
			contexts.set(context);
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

	public <T> PostgreSQLQuery<T> query() {
		return getContext().query();
	}

	public <T> PostgreSQLQuery<T> select(Expression<T> expr) {
		return getContext().select(expr);
	}

	public PostgreSQLQuery<Tuple> select(Expression<?>... exprs) {
		return getContext().select(exprs);
	}

	public SQLInsertClause insert(RelationalPath<?> entity) {
		return getContext().insert(entity);
	}

	public SQLUpdateClause update(RelationalPath<?> entity) {
		return getContext().update(entity);
	}

	public SQLDeleteClause delete(RelationalPath<?> entity) {
		return getContext().delete(entity);
	}

}
