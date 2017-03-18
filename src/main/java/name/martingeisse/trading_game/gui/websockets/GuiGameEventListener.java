package name.martingeisse.trading_game.gui.websockets;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.event.GameEvent;
import name.martingeisse.trading_game.game.event.GameEventBatch;
import name.martingeisse.trading_game.game.event.GameEventListener;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;

/**
 * This interface is like {@link GameEventListener}, except that the {@link IPartialPageRequestHandler} is also
 * passed as a parameter.
 */
public interface GuiGameEventListener {

	default public void receiveGameEvent(IPartialPageRequestHandler partialPageRequestHandler, GameEvent event) {
		receiveGameEventBatch(partialPageRequestHandler, new GameEventBatch(ImmutableList.of(event)));
	}

	default public void receiveGameEventBatch(IPartialPageRequestHandler partialPageRequestHandler, GameEventBatch eventBatch) {
		for (GameEvent event : eventBatch.getEvents()) {
			receiveGameEvent(partialPageRequestHandler, event);
		}
	}

}
