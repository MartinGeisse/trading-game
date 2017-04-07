package name.martingeisse.trading_game.platform.postgres;

import com.google.inject.Inject;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.postgresql.PostgreSQLQuery;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Wraps a {@link PostgresConnection} and adds caching capabilities. Note that no caching is done implicitly since it
 * might impact correctness.
 * <p>
 * This context initializes its properties lazily. It gets disposed by calling reset(), which returns it to its
 * initial state.
 * <p>
 * Closing this context is the same as resetting it. This is done to support try-with-resource. The close() method is
 * therefore not idempotent when other calls happen in between, since the second close() call will reset the context
 * again instead of having no effect. This behavior is allowed by {@link AutoCloseable} and seems obvious in this case.
 * <p>
 * This class is NOT thread-safe! The typical usage pattern is to create, use and dispose of a context within a single
 * thread. If a context must be used by multiple threads, e.g. to share a database transaction, then the calling code
 * must ensure thread safety itself.
 */
public final class PostgresContext implements Closeable {

	private final PostgresService postgresService;
	private PostgresConnection connection;

	@Inject
	public PostgresContext(PostgresService postgresService) {
		this.postgresService = postgresService;
	}

	public PostgresConnection getConnection() {
		if (connection == null) {
			connection = postgresService.newConnection();
		}
		return connection;
	}

	public void reset() {
		if (connection != null) {
			try {
				connection.close();
			} finally {
				connection = null;
			}
		}
	}

	@Override
	public void close() throws IOException {
		reset();
	}

	public void setAutoCommit(boolean autoCommit) {
		try {
			getConnection().getJdbcConnection().setAutoCommit(autoCommit);
		} catch (SQLException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	public void commit() {
		try {
			getConnection().getJdbcConnection().commit();
		} catch (SQLException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	public void rollback() {
		try {
			getConnection().getJdbcConnection().rollback();
		} catch (SQLException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	public <T> PostgreSQLQuery<T> query() {
		return getConnection().query();
	}

	public <T> PostgreSQLQuery<T> select(Expression<T> expr) {
		return query().select(expr);
	}

	public PostgreSQLQuery<Tuple> select(Expression<?>... exprs) {
		return query().select(exprs);
	}

	public SQLInsertClause insert(RelationalPath<?> entity) {
		return getConnection().insert(entity);
	}

	public SQLUpdateClause update(RelationalPath<?> entity) {
		return getConnection().update(entity);
	}

	public SQLDeleteClause delete(RelationalPath<?> entity) {
		return getConnection().delete(entity);
	}

}
