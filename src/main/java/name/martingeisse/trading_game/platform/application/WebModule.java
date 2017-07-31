/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.platform.application;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import name.martingeisse.trading_game.api.internal.ApiFilter;
import name.martingeisse.trading_game.common.util.profiling.ThreadProfiling;
import name.martingeisse.trading_game.gui.websockets.WebSocketConstants;
import name.martingeisse.trading_game.peripherals.payment.folding_at_home.FoldingAtHomeApiService;
import name.martingeisse.trading_game.peripherals.payment.folding_at_home.FoldingAtHomePaymentService;
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

		// handle API requests
		filterRegex("/.*").through(ApiFilter.class);

		// bind Wicket
		{
			bind(WebApplication.class).to(MyWicketApplication.class);
			bind(MyWicketFilter.class).in(Singleton.class);
			final Map<String, String> initParams = new HashMap<String, String>(1);
			initParams.put(WicketFilter.FILTER_MAPPING_PARAM, "/*");
			initParams.put("maxIdleTime", "" + WebSocketConstants.TIMEOUT_MILLISECONDS);
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
				ThreadProfiling.start();
				chain.doFilter(request, response);
			} finally {
				postgresContextService.reset();
				ThreadProfiling.measure("MyFilter, end of request");
				Thread.currentThread().setName(THREAD_NAME_PREFIX);
			}
		}

	}

	private static String getThreadNameSuffix(ServletRequest untypedRequest) {
		if (untypedRequest instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) untypedRequest;
			StringBuilder builder = new StringBuilder();
			appendForThreadNameSuffix(request.getRequestURI(), builder);
			if (request.getQueryString() != null) {
				builder.append('?');
				appendForThreadNameSuffix(request.getQueryString(), builder);
			}
			return builder.toString();
		}
		return "unknown";
	}

	private static void appendForThreadNameSuffix(CharSequence what, StringBuilder builder) {
		for (int i = 0; i < what.length(); i++) {
			char c = what.charAt(i);
			if (c == '/' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
				builder.append(c);
			}
		}
	}

}
