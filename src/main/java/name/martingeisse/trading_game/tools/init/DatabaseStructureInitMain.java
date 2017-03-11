package name.martingeisse.trading_game.tools.init;

import com.google.inject.Injector;
import name.martingeisse.trading_game.platform.application.CommandLineApplicationBootstrapper;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class DatabaseStructureInitMain {

	/**
	 * @param args command-line arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {
		init(CommandLineApplicationBootstrapper.bootstrap());
	}

	/**
	 * @param injector ...
	 */
	public static void init(Injector injector) throws Exception {
		String initScript = FileUtils.readFileToString(new File("resource/dbinit/dbinit.sql"), StandardCharsets.UTF_8);
		try (PostgresConnection connection = injector.getInstance(PostgresService.class).newConnection()) {
			connection.getJdbcConnection().createStatement().execute(initScript);
		}
	}

}
