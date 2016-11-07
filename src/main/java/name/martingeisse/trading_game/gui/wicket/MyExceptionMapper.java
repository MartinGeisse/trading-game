/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui.wicket;

import org.apache.log4j.Logger;
import org.apache.wicket.DefaultExceptionMapper;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;

/**
 * {@link IExceptionMapper} implementation.
 */
public final class MyExceptionMapper extends DefaultExceptionMapper {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(MyExceptionMapper.class);
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IExceptionMapper#map(java.lang.Exception)
	 */
	@Override
	public IRequestHandler map(Exception e) {
		try {
			
			// disable caching for error pages
			final Response response = RequestCycle.get().getResponse();
			if (response instanceof WebResponse) {
				((WebResponse)response).disableCaching();
			}
			
		} catch (final RuntimeException e2) {
			// log this exception, then use default mapping for the original exception
			logger.error("nested exception in exception mapper", e2);
		}

		// fall back to default beahvior
		return super.map(e);
		
	}
	
}
