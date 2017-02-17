package name.martingeisse.trading_game.common.database;

import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;

import java.io.Closeable;
import java.sql.Connection;

/**
 * The high-level interface for database connections.
 */
public interface DatabaseConnection extends Closeable {

	/**
	 * Gets the JDBC connection.
	 *
	 * @return the JDBC connection
	 */
	public Connection getJdbcConnection();

	/**
	 * Creates a new QueryDSL query object.
	 *
	 * @param <T> the result type
	 * @return the query object
	 */
	public <T> AbstractSQLQuery<T, ?> query();

	/**
	 * Creates an insert clause.
	 *
	 * @param entity the entity path
	 * @return the insert clause
	 */
	public SQLInsertClause insert(RelationalPath<?> entity);

	/**
	 * Creates an update clause.
	 *
	 * @param entity the entity path
	 * @return the update clause
	 */
	public SQLUpdateClause update(RelationalPath<?> entity);

	/**
	 * Creates a delete clause.
	 *
	 * @param entity the entity path
	 * @return the delete clause
	 */
	public SQLDeleteClause delete(RelationalPath<?> entity);

}
