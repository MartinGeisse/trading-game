package name.martingeisse.trading_game.game.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.HashSet;
import java.util.Set;

/**
 * Used by game logic classes to send change events.
 */
@Singleton
public final class GameEventEmitter {

	private final Set<GameEventListener> staticListeners;
	private final DynamicGameEventListenerSet dynamicListeners;

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
		for (GameEventListener listener : staticListeners) {
			listener.receive(event);
		}
		dynamicListeners.receive(event);
	}

}
