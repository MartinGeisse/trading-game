package name.martingeisse.trading_game.gui.websockets;

import name.martingeisse.trading_game.game.event.GameEventBatch;
import name.martingeisse.trading_game.game.event.GameEventListener;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;

/**
 * This interface is like {@link GameEventListener}, except that the {@link IPartialPageRequestHandler} is also
 * passed as a parameter.
 */
public interface GuiGameEventListener {
	public void receiveGameEventBatch(IPartialPageRequestHandler partialPageRequestHandler, GameEventBatch eventBatch);
}
