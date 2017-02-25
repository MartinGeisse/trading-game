package name.martingeisse.trading_game.gui.websockets;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.event.GameEvent;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

/**
 * Wraps a batch of game events as an {@link IWebSocketPushMessage}.
 */
public final class GameEventBatchPushMessage implements IWebSocketPushMessage {

	private final ImmutableList<GameEvent> events;

	/**
	 * Constructor.
	 *
	 * @param events the events
	 */
	public GameEventBatchPushMessage(ImmutableList<GameEvent> events) {
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
