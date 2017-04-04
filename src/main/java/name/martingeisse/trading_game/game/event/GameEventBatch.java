package name.martingeisse.trading_game.game.event;

import com.google.common.collect.ImmutableList;

/**
 *
 */
public final class GameEventBatch {

	private final ImmutableList<GameEvent> events;

	public GameEventBatch(ImmutableList<GameEvent> events) {
		this.events = events;
	}

	/**
	 * Getter method.
	 *
	 * @return the events
	 */
	public ImmutableList<GameEvent> getEvents() {
		return events;
	}

}
