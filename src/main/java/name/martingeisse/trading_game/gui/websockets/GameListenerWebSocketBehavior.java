package name.martingeisse.trading_game.gui.websockets;

import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.GameListener;
import name.martingeisse.trading_game.gui.wicket.MyWicketApplication;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.message.AbortedMessage;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;

/**
 * Specialized web socket behavior that manages a single game listener driven by the life-cycle of the web socket
 * connection. The listener is built around a {@link PushMessageSender} and is expected to send relevant changes as
 * push messages.
 */
public abstract class GameListenerWebSocketBehavior extends WebSocketBehavior {

	private transient GameListener listener = null;

	@Override
	protected void onConnect(ConnectedMessage message) {
		discardListener();
		listener = createListener(new PushMessageSender(message));
		getGame().getListeners().add(listener);
	}

	@Override
	protected void onClose(ClosedMessage message) {
		discardListener();
	}

	@Override
	protected void onAbort(AbortedMessage message) {
		discardListener();
	}

	private Game getGame() {
		return MyWicketApplication.get().getDependency(Game.class);
	}

	private void discardListener() {
		if (listener != null) {
			getGame().getListeners().remove(listener);
			destroyListener(listener);
			listener = null;
		}
	}

	protected abstract GameListener createListener(PushMessageSender pushMessageSender);

	protected void destroyListener(GameListener listener) {
	}

}