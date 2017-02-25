package name.martingeisse.trading_game.game.event;

import com.google.common.collect.ImmutableList;

/**
 *
 */
public interface GameEventListener {

	default public void receiveGameEvent(GameEvent event) {
		receiveGameEventBatch(ImmutableList.of(event));
	}

	default public void receiveGameEventBatch(ImmutableList<GameEvent> eventBatch) {
		for (GameEvent event : eventBatch) {
			receiveGameEvent(event);
		}
	}

}
