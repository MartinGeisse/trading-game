/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.platform.application;

import com.google.inject.Injector;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.platform.application.configuration.Configurator;
import name.martingeisse.trading_game.platform.util.parameter.ParameterException;

/**
 * This class continues the work that the bootstrapper classes started.
 * In contrast to those classes, this initializer can have its dependencies injected
 * by Guice.
 */
public abstract class AbstractApplicationInitializer {

	protected final Injector injector;

	public AbstractApplicationInitializer(Injector injector) {
		this.injector = injector;
	}

	/**
	 * The main entry point to this class.
	 */
	protected void run() {
		configureConfigurationParticipants();
	}

	protected final void configureConfigurationParticipants() {
		try {
			injector.getInstance(Configurator.class).configureParticipants();
		} catch (ParameterException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

}
