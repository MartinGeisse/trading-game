package name.martingeisse.trading_game.platform.application.configuration;

import com.google.inject.Singleton;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.platform.util.parameter.Parameters;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
@Singleton
public class ApplicationConfiguration implements Parameters {

	private final Properties properties;

	public ApplicationConfiguration() {
		properties = new Properties();
		try (InputStream in = new FileInputStream("/etc/trading_game/trading_game.properties")) {
			properties.load(in);
		} catch (IOException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	/**
	 * Getter method.
	 *
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	@Override
	public String getOptionalParameter(String name) {
		return properties.getProperty(name);
	}

}
