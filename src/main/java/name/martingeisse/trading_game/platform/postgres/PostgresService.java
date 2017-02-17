package name.martingeisse.trading_game.platform.postgres;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.common.database.DatabaseService;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.platform.application.configuration.annotated.AnnotatedConfigurationParticipant;
import name.martingeisse.trading_game.platform.application.configuration.annotated.ConfigurationSetting;
import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 */
@Singleton
public class PostgresService implements DatabaseService, AnnotatedConfigurationParticipant {

	private final PGPoolingDataSource dataSource;

	/**
	 * Constructor.
	 */
	@Inject
	public PostgresService() {
		dataSource = new PGPoolingDataSource();
		dataSource.setDataSourceName("Companion PostgreSQL Database");
		dataSource.setMaxConnections(100);
	}

	@ConfigurationSetting(name = "postgresHost")
	public void setPostgresHost(String value) {
		dataSource.setServerName(value);
	}

	@ConfigurationSetting(name = "postgresDatabase")
	public void setPostgresDatabaseName(String value) {
		dataSource.setDatabaseName(value);
	}

	@ConfigurationSetting(name = "postgresUser")
	public void setPostgresUser(String value) {
		dataSource.setUser(value);
	}

	@ConfigurationSetting(name = "postgresPassword")
	public void setPostgresPassword(String value) {
		dataSource.setPassword(value);
	}

	// override
	@Override
	public Connection newJdbcConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	// override
	@Override
	public PostgresConnection newConnection() {
		return new PostgresConnection(newJdbcConnection(), MyPostgresConfiguration.CONFIGURATION);
	}

}
