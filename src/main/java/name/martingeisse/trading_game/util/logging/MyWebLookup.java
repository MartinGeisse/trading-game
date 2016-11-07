/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.trading_game.util.logging;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.web.WebLookup;

/**
 *
 */
@Plugin(name = "myweb", category = "Lookup")
public class MyWebLookup extends WebLookup {

	/**
	 * Indicates whether the application runs in command-line mode. This causes this lookup to
	 * provide fake values since no servlet context is available.
	 */
	public static boolean commandLineMode;

	// override
	@Override
	public String lookup(LogEvent event, String key) {
		if (commandLineMode) {
			if (key.equals("initParam.logfilePath")) {
				return "logs";
			} else {
				throw new RuntimeException("unsupported web lookup: " + key);
			}
		} else {
			return super.lookup(event, key);
		}
	}

}
