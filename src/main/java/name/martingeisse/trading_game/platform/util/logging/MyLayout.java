/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.trading_game.platform.util.logging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 *
 */
@Plugin(name = "MyLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public final class MyLayout extends AbstractStringLayout {

	/**
	 *
	 */
	public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS").withZone(DateTimeZone.UTC);

	/**
	 * This is a check to prevent log4j from initializing too early.
	 */
	public static volatile Exception instanceSource;

	private final Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
	private final boolean useParagraphSeparator;

	/**
	 * Constructor.
	 *
	 * @param useParagraphSeparator whether to use a paragraph separator
	 */
	public MyLayout(boolean useParagraphSeparator) {
		super(StandardCharsets.UTF_8);
		this.useParagraphSeparator = useParagraphSeparator;
		instanceSource = new Exception();
	}

	// override
	@Override
	public String toSerializable(LogEvent event) {
		try {
			StringBuilder builder = getStringBuilder();
			PipeEscaper pipeEscaper = new PipeEscaper(builder);

			// timestamp, thread name, logger name
			TIMESTAMP_FORMATTER.printTo(builder, event.getTimeMillis());
			builder.append(" | ");
			pipeEscaper.append(event.getThreadName());
			builder.append(" | ");
			pipeEscaper.append(event.getLoggerName());
			builder.append(" | ");

			// context map
			Map<String, String> contextMap = event.getContextMap();
			if (contextMap != null && !contextMap.isEmpty()) {
				gson.toJson(contextMap, pipeEscaper);
				builder.append(" | ");
			} else {
				builder.append("| ");
			}

			// context stack
			ContextStack contextStack = event.getContextStack();
			if (contextStack != null && !contextStack.isEmpty()) {
				gson.toJson(contextStack, pipeEscaper);
				builder.append(" | ");
			} else {
				builder.append("| ");
			}

			// level
			builder.append(event.getLevel());
			builder.append(" | ");

			// message
			pipeEscaper.append(event.getMessage().getFormattedMessage());

			// exception
			printException(builder, event.getThrown());
			printException(builder, event.getMessage().getThrowable());

			// end of record
			if (useParagraphSeparator) {
				builder.append((char) 8233); // paragraph separator
			}
			builder.append('\n');

			return builder.toString();
		} catch (IOException e) {
			return e.toString();
		}
	}

	private void printException(StringBuilder builder, Throwable e) {
		if (e == null) {
			return;
		}
		builder.append('\n');
		e.printStackTrace(new PrintWriter(new StringBuilderWriter(builder)));
	}

	/**
	 * Factory method.
	 *
	 * @param useParagraphSeparator whether to use a paragraph separator
	 * @return the instance
	 */
	@PluginFactory
	public static MyLayout createLayout(@PluginAttribute(value = "useParagraphSeparator", defaultBoolean = true) boolean useParagraphSeparator) {
		return new MyLayout(useParagraphSeparator);
	}

	/**
	 *
	 */
	private static final class PipeEscaper implements Appendable {

		private final Appendable target;

		PipeEscaper(Appendable target) {
			this.target = target;
		}

		// override
		@Override
		public Appendable append(CharSequence s) throws IOException {
			return append(s, 0, s.length());
		}

		// override
		@Override
		public Appendable append(CharSequence s, int start, int end) throws IOException {
			for (int i = start; i < end; i++) {
				append(s.charAt(i));
			}
			return this;
		}

		// override
		@Override
		public Appendable append(char c) throws IOException {
			if (c == '|') {
				target.append("\\u007c");
			} else {
				target.append(c);
			}
			return this;
		}

	}

}
