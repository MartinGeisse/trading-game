/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.trading_game.util.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

/**
 * This filter is configured with a log level. Messages from libraries (i.e., those whose category
 * does not start with name.martingeisse.trading_game) are suppressed if they are below that log level.
 * Application messages aren't affected at all.
 */
@Plugin(name = "LibraryFilter", category = "Core", elementType = "filter", printObject = true)
public final class LibraryFilter extends AbstractFilter {

	private static final long serialVersionUID = 1L;

	private final Level thresholdLevel;

	// private constructor -- use the above factory method instead
	private LibraryFilter(final Level thresholdLevel, final Result onMatch, final Result onMismatch) {
		super(onMatch, onMismatch);
		this.thresholdLevel = thresholdLevel;
	}

	/**
	 * Creates a LibraryFilter.
	 *
	 * @param level      the threshold log Level.
	 * @param onMatch    the result to use if the filter matches
	 * @param onMismatch the result to use if the filter doesn't match
	 * @return the filter
	 */
	@PluginFactory
	public static LibraryFilter createFilter(@PluginAttribute("level") final Level level, @PluginAttribute("onMatch") final Result onMatch, @PluginAttribute("onMismatch") final Result onMismatch) {
		return new LibraryFilter(level == null ? Level.ERROR : level, onMatch, onMismatch);
	}

	/**
	 * Getter method.
	 *
	 * @return the thresholdLevel
	 */
	public Level getThresholdLevel() {
		return thresholdLevel;
	}

	// helper
	private Result filter(final String loggerName, final Level messageLevel) {
		if (isLibraryLogger(loggerName)) {
			return messageLevel.isMoreSpecificThan(thresholdLevel) ? onMatch : onMismatch;
		} else {
			return onMatch;
		}
	}

	// override
	@Override
	public Result filter(final LogEvent event) {
		return filter(event.getLoggerName(), event.getLevel());
	}

	// override
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
		return filter(logger.getName(), level);
	}

	// override
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
		return filter(logger.getName(), level);
	}

	// override
	@Override
	public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
		return filter(logger.getName(), level);
	}

	// override
	@Override
	public String toString() {
		return thresholdLevel.toString();
	}

	/**
	 * Checks if the specified logger name indicates a logger from a library.
	 *
	 * @param loggerName the logger name
	 * @return true if it is a library logger, false if it is an application logger
	 */
	public static boolean isLibraryLogger(String loggerName) {

		// MySchedulerFactory is a special case since Quartz uses the object's actual class to write logs, so
		// the whole class should be treated as library code
//		if (loggerName.equals("name.martingeisse.task.MySchedulerFactory")) {
//			return true;
//		}

		// Although this class is library code, its errors are useful at application level.
//		if (loggerName.equals("org.quartz.core.JobRunShell")) {
//			return false;
//		}

		// regular case
		return !loggerName.startsWith("name.martingeisse.trading_game");

	}

}
