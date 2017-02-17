/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.platform.application;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import name.martingeisse.trading_game.platform.application.configuration.ApplicationConfiguration;
import name.martingeisse.trading_game.platform.util.logging.JulToLog4jBridge;
import name.martingeisse.trading_game.platform.util.logging.MyLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.web.Log4jServletContextListener;

import javax.servlet.ServletContextEvent;

/**
 * This listener initializes Guice and the plugin system on servlet context start.
 */
public class ServerApplicationBootstrapper extends GuiceServletContextListener {

	private final Log4jServletContextListener log4jListener = new Log4jServletContextListener();
	private Injector injector;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		// prepare the logging system
		if (MyLayout.instanceSource != null) {
			throw new IllegalStateException("log4j initialized too early!", MyLayout.instanceSource);
		}
		log4jListener.contextInitialized(servletContextEvent);
		LogManager.getLogger().info("starting application...");
		JulToLog4jBridge.enable();

		// continue with the normal Guice/Servlet startup procedure
		super.contextInitialized(servletContextEvent);

	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		LogManager.getLogger().info("application shutdown requested...");

		// remove injector from the servlet context first, so we don't accept any new HTTP requests
		super.contextDestroyed(servletContextEvent);

		// clean up the application (currently nothing to do)
		if (injector != null) {
			// ...
		}

		// shut down logging last
		LogManager.getLogger().info("application shutdown completed!");
		log4jListener.contextDestroyed(servletContextEvent);

	}

	@Override
	protected Injector getInjector() {
		ApplicationConfiguration configuration = new ApplicationConfiguration();
		Injector injector = Guice.createInjector(new ServerApplicationModule(configuration), new WebModule());
		injector.getInstance(ServerApplicationInitializer.class).run();
		return injector;
	}

}

