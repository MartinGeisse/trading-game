package name.martingeisse.trading_game.application.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.util.parameter.ParameterException;

import java.util.Set;

/**
 * Configures all configuration participants.
 */
@Singleton
public class Configurator {

	private final ApplicationConfiguration configuration;
	private final Set<ConfigurationParticipant> configurationParticipants;

	@Inject
	public Configurator(ApplicationConfiguration configuration, Set<ConfigurationParticipant> configurationParticipants) {
		this.configuration = configuration;
		this.configurationParticipants = configurationParticipants;
	}

	public void configureParticipants() throws ParameterException {
		for (ConfigurationParticipant participant : configurationParticipants) {
			participant.configure(configuration);
		}
	}

}
