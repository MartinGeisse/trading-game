package name.martingeisse.trading_game.platform.application.configuration.annotated;

import name.martingeisse.trading_game.platform.application.configuration.ApplicationConfiguration;
import name.martingeisse.trading_game.platform.application.configuration.ConfigurationParticipant;
import name.martingeisse.trading_game.platform.util.parameter.ParameterException;

/**
 * Implementing this interface indicates (by way of the default configure() implementation)
 * to inject configuration settings through annotated setter methods.
 */
public interface AnnotatedConfigurationParticipant extends ConfigurationParticipant {

	@Override
	default void configure(ApplicationConfiguration configuration) throws ParameterException {
		Util.assignSettingsToSetters(configuration, this);
	}

}
