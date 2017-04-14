package name.martingeisse.trading_game.game.space;

import name.martingeisse.trading_game.game.event.GameEvent;

/**
 *
 */
public class SpaceObjectPositionChangedEvent extends GameEvent {

	private final long id;

	public SpaceObjectPositionChangedEvent(long id) {
		this.id = id;
	}

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

}
