package name.martingeisse.trading_game.game;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public final class GameListenerSet implements GameListener {

	private final ConcurrentHashMap<GameListener, GameListener> listeners = new ConcurrentHashMap<>();

	public void add(GameListener listener) {
		listeners.put(listener, listener);
	}

	public void remove(GameListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void onDynamicSpaceObjectsChanged() {
		for (GameListener listener : listeners.values()) {
			listener.onDynamicSpaceObjectsChanged();
		}
	}

}
