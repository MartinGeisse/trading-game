/**
 * Copyright (c) 2016 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.trading_game.platform.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * This class is used for simple command-line applications to initialize Guice. This class does not
 * launch maintenance jobs, startup listeners, or similar.
 */
public final class CommandLineApplicationBootstrapper {

	// prevent instantiation
	private CommandLineApplicationBootstrapper() {
	}

	/**
	 * Bootstraps a command-line application.
	 *
	 * @return the injector
	 */
	public static Injector bootstrap() {
		Injector injector = Guice.createInjector(new CommandLineApplicationModule());
		injector.getInstance(CommandLineApplicationInitializer.class).run();
		return injector;
	}

}
