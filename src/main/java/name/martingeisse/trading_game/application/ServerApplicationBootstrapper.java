/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.application;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * This listener initializes Guice and the plugin system on servlet context start.
 */
public class ServerApplicationBootstrapper extends GuiceServletContextListener {

	// override
	@Override
	protected Injector getInjector() {
		Injector injector = Guice.createInjector(new ServerApplicationModule(), new WebModule());
		injector.getInstance(ServerApplicationInitializer.class).run();
		return injector;
	}

}

