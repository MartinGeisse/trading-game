/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.application;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import name.martingeisse.trading_game.gui.wicket.MyWicketApplication;
import name.martingeisse.trading_game.gui.wicket.MyWicketFilter;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * This module provides the low-level servlet objects.
 */
public class WebModule extends ServletModule {

	// override
	@Override
	protected void configureServlets() {

		// bind Wicket
		{
			bind(WebApplication.class).to(MyWicketApplication.class);
			bind(MyWicketFilter.class).in(Singleton.class);
			final Map<String, String> initParams = new HashMap<String, String>(1);
			initParams.put(WicketFilter.FILTER_MAPPING_PARAM, "/*");
			filterRegex("/.*").through(MyWicketFilter.class, initParams);
		}
		
	}

}
