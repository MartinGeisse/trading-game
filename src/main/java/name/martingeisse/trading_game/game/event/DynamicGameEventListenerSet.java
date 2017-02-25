package name.martingeisse.trading_game.game.event;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
final class DynamicGameEventListenerSet implements GameEventListener {

	private final ConcurrentHashMap<GameEventListener, GameEventListener> listeners = new ConcurrentHashMap<>();

	public void add(GameEventListener listener) {
		listeners.put(listener, listener);
	}

	public void remove(GameEventListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void receive(GameEvent event) {
		for (GameEventListener listener : listeners.values()) {
			listener.receive(event);
		}
	}

}
