/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.trading_game.platform.util.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Redirects JUL logs to log4j (without using slf4j in between).
 */
public class JulToLog4jBridge extends Handler {

	private static boolean enabled;

	/**
	 * Enables this bridge.
	 */
	public static void enable() {
		enabled = true;
	}

	// override
	@Override
	public void flush() {
		// nothing to do
	}

	// override
	@Override
	public void close() {
		// nothing to do
	}

	// override
	@Override
	public void publish(LogRecord record) {

		// this bridge isn't enabled until Log4j has been properly initialized, otherwise we would initialize
		// it too early by logging.
		if (!enabled) {
			return;
		}

		// discard invalid records / messages
		if (record == null) {
			return;
		}
		String message = record.getMessage();
		if (message == null) {
			return;
		}

		// obtain the logger
		String loggerName = record.getLoggerName();
		if (loggerName == null) {
			loggerName = "unknown.jul.logger";
		}
		Logger logger = LogManager.getLogger(loggerName);

		// map the log level
		int julLevel = record.getLevel().intValue();
		Level log4jLevel;
		if (julLevel >= java.util.logging.Level.SEVERE.intValue()) {
			log4jLevel = Level.ERROR;
		} else if (julLevel >= java.util.logging.Level.WARNING.intValue()) {
			log4jLevel = Level.WARN;
		} else if (julLevel >= java.util.logging.Level.INFO.intValue()) {
			log4jLevel = Level.INFO;
		} else if (julLevel >= java.util.logging.Level.FINE.intValue()) {
			log4jLevel = Level.DEBUG;
		} else {
			log4jLevel = Level.TRACE;
		}

		// check if the message can be logged
		if (!logger.isEnabled(log4jLevel)) {
			return;
		}

		// translate the message
		ResourceBundle bundle = record.getResourceBundle();
		if (bundle != null) {
			try {
				message = bundle.getString(message);
			} catch (MissingResourceException e) {
			}
		}
		Object[] params = record.getParameters();
		if (params != null && params.length > 0) {
			message = MessageFormat.format(message, params);
		}

		// log the message
		logger.log(log4jLevel, message, record.getThrown());

	}

}