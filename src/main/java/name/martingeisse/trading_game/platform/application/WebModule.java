/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.platform.application;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import name.martingeisse.trading_game.platform.wicket.MyWicketFilter;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This module provides the low-level servlet objects.
 */
public class WebModule extends ServletModule {

	public static final String THREAD_NAME_PREFIX = "web";

	// override
	@Override
	protected void configureServlets() {

		// reset the per-thread PostgresContext after each request, so the next request uses a fresh state
		filterRegex("/.*").through(MyFilter.class);

		// bind Wicket
		{
			bind(WebApplication.class).to(MyWicketApplication.class);
			bind(MyWicketFilter.class).in(Singleton.class);
			final Map<String, String> initParams = new HashMap<String, String>(1);
			initParams.put(WicketFilter.FILTER_MAPPING_PARAM, "/*");
			filterRegex("/.*").through(MyWicketFilter.class, initParams);
		}

	}

	@Singleton
	public static class MyFilter implements Filter {

		private final PostgresContextService postgresContextService;

		@Inject
		public MyFilter(PostgresContextService postgresContextService) {
			this.postgresContextService = postgresContextService;
		}

		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
		}

		@Override
		public void destroy() {
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
			try {
				Thread.currentThread().setName(THREAD_NAME_PREFIX + '-' + getThreadNameSuffix(request));
				chain.doFilter(request, response);
			} finally {
				postgresContextService.reset();
				Thread.currentThread().setName(THREAD_NAME_PREFIX);
			}
		}

	}

	private static String getThreadNameSuffix(ServletRequest untypedRequest) {
		if (untypedRequest instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) untypedRequest;
			String requestUri = request.getRequestURI();
			StringBuilder builder = new StringBuilder();
			for (int i=0; i<requestUri.length(); i++) {
				char c = requestUri.charAt(i);
				if (c == '/' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
					builder.append(c);
				}
			}
			return builder.toString();
		}
		return "unknown";
	}

}
