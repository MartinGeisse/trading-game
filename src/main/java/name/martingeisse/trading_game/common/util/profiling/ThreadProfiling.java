package name.martingeisse.trading_game.common.util.profiling;

import java.text.NumberFormat;

/**
 * Allows per-thread profiling.
 */
public final class ThreadProfiling {

	private static final NumberFormat INDEX_COUNTER_FORMAT = NumberFormat.getIntegerInstance();

	static {
		INDEX_COUNTER_FORMAT.setMinimumIntegerDigits(3);
	}

	private static final ThreadLocal<Long> baseTimePerThread = new ThreadLocal<>();
	private static final ThreadLocal<Long> lastTimePerThread = new ThreadLocal<>();
	private static final ThreadLocal<Integer> indexCounterPerThread = new ThreadLocal<>();

	public static void start() {
		long now = System.currentTimeMillis();
		baseTimePerThread.set(now);
		lastTimePerThread.set(now);
		indexCounterPerThread.set(0);
	}

	public static void measure(String checkpointDescription) {
		long now = System.currentTimeMillis();
		StringBuilder builder = new StringBuilder();
		builder.append("*** PROFILING thread '").append(Thread.currentThread().getName());
		builder.append("' (").append(INDEX_COUNTER_FORMAT.format(indexCounterPerThread.get())).append("): ").append(checkpointDescription);
		builder.append(" at ").append(now - baseTimePerThread.get());
		builder.append(", delta = ").append(now - lastTimePerThread.get());
		System.out.println(builder);
		lastTimePerThread.set(now);
		indexCounterPerThread.set(indexCounterPerThread.get() + 1);
	}

}
