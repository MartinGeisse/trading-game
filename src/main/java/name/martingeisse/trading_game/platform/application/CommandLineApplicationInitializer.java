/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.platform.application;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * This class continues the work that {@link CommandLineApplicationBootstrapper} started.
 * In contrast to that class, this initializer can have its dependencies injected
 * by Guice.
 */
@Singleton
public final class CommandLineApplicationInitializer extends AbstractApplicationInitializer {

	@Inject
	public CommandLineApplicationInitializer(Injector injector) {
		super(injector);
	}

}
