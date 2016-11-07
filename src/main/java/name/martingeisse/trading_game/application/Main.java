/**
 * Copyright (c) 2016 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.trading_game.application;

import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.EnumSet;

/**
 *
 */
public final class Main {

	// prevent instantiation
	private Main() {
	}

	/**
	 * The main method.
	 *
	 * @param args command-line arguments (ignored)
	 */
	public static void main(String[] args) throws Exception {

		Log.setLog(new NoLogging());

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addFilterWithMapping(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
		servletHandler.addServletWithMapping(NopServlet.class, "/*");

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setContextPath("/trading_game");
		contextHandler.setServletHandler(servletHandler);
		contextHandler.setInitParameter("logfilePath", "logs");
		contextHandler.addEventListener(new ServerApplicationBootstrapper());

		SessionHandler sessionHandler = new SessionHandler(new HashSessionManager());
		sessionHandler.setHandler(contextHandler);

		Server server = new Server(8080);
		server.setHandler(sessionHandler);
		server.setSessionIdManager(new HashSessionIdManager());
		server.start();
		server.join();

	}

	/**
	 *
	 */
	public static class NopServlet extends HttpServlet {
	}

	/**
	 *
	 */
	public static class ResourceFlushFilter implements Filter {

		// override
		@Override
		public void init(FilterConfig arg0) throws ServletException {
			// no initialization necessary
		}

		// override
		@Override
		public void destroy() {
			// no cleanup necessary
		}

		// override
		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			try {
				chain.doFilter(request, response);
			} finally {
				response.flushBuffer();
			}
		}

	}

	/**
	 *
	 */
	public static class NoLogging implements Logger {

		@Override
		public String getName() {
			return "no";
		}

		@Override
		public void warn(String msg, Object... args) {
			// don't log
		}

		@Override
		public void warn(Throwable thrown) {
			// don't log
		}

		@Override
		public void warn(String msg, Throwable thrown) {
			// don't log
		}

		@Override
		public void info(String msg, Object... args) {
			// don't log
		}

		@Override
		public void info(Throwable thrown) {
			// don't log
		}

		@Override
		public void info(String msg, Throwable thrown) {
			// don't log
		}

		@Override
		public boolean isDebugEnabled() {
			// don't log
			return false;
		}

		@Override
		public void setDebugEnabled(boolean enabled) {
			// don't log
		}

		@Override
		public void debug(String msg, Object... args) {
			// don't log
		}

		@Override
		public void debug(Throwable thrown) {
			// don't log
		}

		@Override
		public void debug(String msg, Throwable thrown) {
			// don't log
		}

		@Override
		public Logger getLogger(String name) {
			return this;
		}

		@Override
		public void ignore(Throwable ignored) {
			// don't log
		}

		@Override
		public void debug(String msg, long value) {
			// don't log
		}

	}

}
