package name.martingeisse.trading_game.platform.application;

import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.EnumSet;

/**
 *
 */
public final class Launcher {

	// prevent instantiation
	private Launcher() {
	}

	public static void launch() throws Exception {

		//
		// TODO support HTTP/2. Right now, this is a nightmare because you need to include alpn-boot-XXX.jar in the
		// BOOT classpath, and the version of that jar has to match the version of the JRE *down to the individual build*.
		// Until the Jetty devs get this sorted out, HTTP/2 support isn't worth the hassle.
		// "With Java 9, ALPN will become part of the Java SE standard, i.e. Java 9 will provide native support for ALPN"
		//
		// Once ALPN is ready, this site shows how to enable HTTP/2:
		//
		//   https://github.com/fstab/http2-examples/blob/master/jetty-http2-server-example/src/main/java/de/consol/labs/h2c/examples/server/Http2Server.java
		//   https://github.com/fstab/http2-examples/blob/master/jetty-http2-echo-server/src/main/java/de/consol/labs/h2c/Http2EchoServer.java
		//
		// Jetty integration of JDK 9 ALPN support:
		//
		//   https://github.com/eclipse/jetty.project/issues/486
		//
		// If Java 9 takes too long, this project may be able to help:
		//
		//   https://github.com/jetty-project/jetty-alpn-agent
		//

		Log.setLog(new NoLogging());

		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addFilterWithMapping(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
		servletHandler.addServletWithMapping(NopServlet.class, "/*");

		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setContextPath("/");
		contextHandler.setServletHandler(servletHandler);
		contextHandler.setInitParameter("logfilePath", "logs");
		contextHandler.addEventListener(new ServerApplicationBootstrapper());

		SessionHandler sessionHandler = new SessionHandler(new HashSessionManager());
		sessionHandler.setHandler(contextHandler);

		Server server = new Server(8080);
		server.addConnector(buildSslConnector(server));
		server.setHandler(sessionHandler);
		server.setSessionIdManager(new HashSessionIdManager());
		server.start();
		server.join();

	}

	private static ServerConnector buildSslConnector(Server server) {
		SslContextFactory contextFactory = new SslContextFactory();
		contextFactory.setKeyStorePath("/etc/trading_game/keystore.jks");
		contextFactory.setKeyStorePassword("password"); // pointless since there is no more secure place to store the password
		ServerConnector connector = new ServerConnector(server, contextFactory);
		connector.setPort(8443);
		return connector;
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
