package name.martingeisse.trading_game.game.event;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Used by game logic classes to send change events.
 */
@Singleton
public final class GameEventEmitter {

	private final Set<GameEventListener> staticListeners;
	private final DynamicGameEventListenerSet dynamicListeners;
	private final AtomicInteger batchNesting = new AtomicInteger(0);
	private final ConcurrentLinkedQueue<GameEvent> batch = new ConcurrentLinkedQueue<>();

	@Inject
	public GameEventEmitter(Set<GameEventListener> listeners) {
		this.staticListeners = new HashSet<>(listeners);
		this.dynamicListeners = new DynamicGameEventListenerSet();
	}

	/**
	 * Adds a listener dynamically.
	 *
	 * @param listener the listener to add
	 */
	public void addListener(GameEventListener listener) {
		dynamicListeners.add(listener);
	}

	/**
	 * Removes a listener dynamically.
	 *
	 * @param listener the listener to remove
	 */
	public void removeListener(GameEventListener listener) {
		dynamicListeners.remove(listener);
	}

	/**
	 * Sends an event to all listeners.
	 *
	 * @param event the event to send
	 */
	public void emit(GameEvent event) {
		if (batchNesting.get() == 0) {
			for (GameEventListener listener : staticListeners) {
				listener.receiveGameEvent(event);
			}
			dynamicListeners.receiveGameEvent(event);
		} else {
			// Note that we might have detected a running batch, but by the time we want to add an
			// event to it, the batch has been finished. If so, we flush the batch. The way we check,
			// we may run into a second batch that has been started in the meantime, but then the
			// current event will just be merged into the second batch and be flushed together with it.
			batch.add(event);
			if (batchNesting.get() == 0) {
				flushBatch();
			}
		}
	}

	/**
	 * Begins a batch of events that should be delivered at once to improve performance.
	 */
	public void beginBatch() {
		batchNesting.addAndGet(1);
	}

	/**
	 * Finishes a batch of events.
	 */
	public void finishBatch() {
		if (batchNesting.addAndGet(-1) == 0) {
			flushBatch();
		}
	}

	//
	private void flushBatch() {
		List<GameEvent> events = new ArrayList<>();
		while (true) {
			GameEvent event = batch.poll();
			if (event == null) {
				break;
			}
			events.add(event);
		}
		ImmutableList<GameEvent> immutableEvents = ImmutableList.copyOf(events);
		for (GameEventListener listener : staticListeners) {
			listener.receiveGameEventBatch(immutableEvents);
		}
		dynamicListeners.receiveGameEventBatch(immutableEvents);
	}

}
