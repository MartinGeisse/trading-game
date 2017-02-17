package name.martingeisse.trading_game.platform.postgres;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.postgresql.PostgreSQLQuery;
import name.martingeisse.trading_game.common.database.DatabaseConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

/**
 *
 */
public class PostgresConnection implements DatabaseConnection {

	private static final Logger logger = LogManager.getLogger();

	private final Connection connection;
	private final Configuration configuration;

	PostgresConnection(Connection connection, Configuration configuration) {
		this.connection = connection;
		this.configuration = configuration;
	}

	// override
	@Override
	public void close() {
		try {
			connection.close();
		} catch (Exception e) {
			logger.error("could not close PostgreSQL connection", e);
		}
	}

	// override
	@Override
	public Connection getJdbcConnection() {
		return connection;
	}

	// override
	@Override
	public <T> PostgreSQLQuery<T> query() {
		return new PostgreSQLQuery<T>(connection, configuration) {
			@Override
			protected SQLSerializer createSerializer() {
				return new MyPostgresSqlSerializer(configuration, false, useLiterals);
			}
		};
	}

	// override
	@Override
	public SQLInsertClause insert(RelationalPath<?> entity) {
		return new SQLInsertClause(connection, configuration, entity) {
			@Override
			protected SQLSerializer createSerializer() {
				return new MyPostgresSqlSerializer(configuration, true, useLiterals);
			}
		};
	}

	// override
	@Override
	public SQLUpdateClause update(RelationalPath<?> entity) {
		return new SQLUpdateClause(connection, configuration, entity) {
			@Override
			protected SQLSerializer createSerializer() {
				return new MyPostgresSqlSerializer(configuration, true, useLiterals);
			}
		};
	}

	// override
	@Override
	public SQLDeleteClause delete(RelationalPath<?> entity) {
		return new SQLDeleteClause(connection, configuration, entity) {
			@Override
			protected SQLSerializer createSerializer() {
				return new MyPostgresSqlSerializer(configuration, true, useLiterals);
			}
		};
	}

}
