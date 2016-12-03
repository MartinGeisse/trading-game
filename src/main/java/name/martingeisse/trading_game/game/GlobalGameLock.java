package name.martingeisse.trading_game.game;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class holds an application-wide lock to synchronize access to the {@link Game}.
 */
public final class GlobalGameLock {

	private static final String REQUEST_ATTRIBUTE_NAME = "GlobalGameLock";
	private static final String REQUEST_ATTRIBUTE_VALUE = "locked";
	private static final Lock lock = new ReentrantLock();

	// prevent instantiation
	private GlobalGameLock() {
	}

	public static void lock() {
		lock.lock();
	}

	public static void unlock() {
		lock.unlock();
	}

	public static void onBeginRequest(HttpServletRequest request) {
		if (request.getAttribute(REQUEST_ATTRIBUTE_NAME) == null) {
			request.setAttribute(REQUEST_ATTRIBUTE_NAME, REQUEST_ATTRIBUTE_VALUE);
			lock.lock();
		}
	}

	public static void onFinishRequest(HttpServletRequest request) {
		if (request.getAttribute(REQUEST_ATTRIBUTE_NAME) != null) {
			lock.unlock();
			request.removeAttribute(REQUEST_ATTRIBUTE_NAME);
		}
	}

}
