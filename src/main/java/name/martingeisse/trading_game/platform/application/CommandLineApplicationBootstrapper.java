/**
 * Copyright (c) 2016 Martin Geisse
 *
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
		return Guice.createInjector(new CommandLineApplicationModule());
	}

}
