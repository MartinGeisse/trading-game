/*
 * Copyright (c) 2016 Service-Reisen Giessen Heyne GmbH & Co KG
 */
package name.martingeisse.trading_game.api.internal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.peripherals.payment.folding_at_home.FoldingAtHomePaymentService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 *
 */
@Singleton
public class ApiFilter implements Filter {

	private final FoldingAtHomePaymentService foldingAtHomePaymentService;

	@Inject
	public ApiFilter(FoldingAtHomePaymentService foldingAtHomePaymentService) {
		this.foldingAtHomePaymentService = foldingAtHomePaymentService;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	@Override
	public void destroy() {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			String requestUri = httpRequest.getRequestURI();
			String contextPath = httpRequest.getContextPath();
			String relativePath = StringUtils.strip(requestUri.startsWith(contextPath) ? requestUri.substring(contextPath.length()) : requestUri, "/");
			relativePath = URLDecoder.decode(relativePath, "utf-8");
			String[] segments = StringUtils.split(relativePath, '/');
			if (segments.length > 0) {
				if ("api".equals(segments[0])) {
					handlePublicApiRequest(httpRequest, httpResponse, segments);
					return;
				}
				if ("internal-api".equals(segments[0])) {
					handleInternalApiRequest(httpRequest, httpResponse, segments);
					return;
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	private void handlePublicApiRequest(HttpServletRequest request, HttpServletResponse response, String[] segments) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain; charset=utf-8");
		response.getWriter().println("unknown API path");
	}

	private void handleInternalApiRequest(HttpServletRequest request, HttpServletResponse response, String[] segments) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain; charset=utf-8");
		if (segments.length > 1) {
			if ("update-play-time-credits".equals(segments[1])) {
				if ("POST".equals(request.getMethod())) {
					foldingAtHomePaymentService.updatePlayTimeCredits();
					response.getWriter().println("OK");
				} else {
					response.setContentType("text/html; charset=utf-8");
					response.getWriter().println("<html><body><form action=\"update-play-time-credits\" method=\"POST\"><input type=\"submit\"></form></body></html>");
				}
				return;
			}
		}
		response.getWriter().println("unknown API path");
	}

}
