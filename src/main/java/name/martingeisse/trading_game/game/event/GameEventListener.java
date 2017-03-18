package name.martingeisse.trading_game.game.event;

import com.google.common.collect.ImmutableList;

/**
 *
 */
public interface GameEventListener {

	default public void receiveGameEvent(GameEvent event) {
		receiveGameEventBatch(new GameEventBatch(ImmutableList.of(event)));
	}

	default public void receiveGameEventBatch(GameEventBatch eventBatch) {
		for (GameEvent event : eventBatch.getEvents()) {
			receiveGameEvent(event);
		}
	}

}
