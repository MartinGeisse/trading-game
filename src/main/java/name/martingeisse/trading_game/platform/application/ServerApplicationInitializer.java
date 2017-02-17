/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.platform.application;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.platform.application.configuration.Configurator;
import name.martingeisse.trading_game.platform.util.parameter.ParameterException;

/**
 * This class continues the work that {@link ServerApplicationBootstrapper} started.
 * In contrast to that listener, this bootstrapper can have its dependencies injected
 * by Guice.
 */
@Singleton
public final class ServerApplicationInitializer {

	private final Injector injector;

	@Inject
	public ServerApplicationInitializer(Injector injector) {
		this.injector = injector;
	}

	/**
	 * The main entry point to this class.
	 */
	void run() {
		try {
			injector.getInstance(Configurator.class).configureParticipants();
		} catch (ParameterException e) {
			throw new UnexpectedExceptionException(e);
		}
	}
	
}
