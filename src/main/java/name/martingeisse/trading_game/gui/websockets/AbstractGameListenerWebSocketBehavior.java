package name.martingeisse.trading_game.gui.websockets;

import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.event.GameEventListener;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.message.AbortedMessage;
import org.apache.wicket.protocol.ws.api.message.ClosedMessage;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;

/**
 * Specialized web socket behavior that manages a single game listener driven by the life-cycle of the web socket
 * connection. The listener is built around a {@link PushMessageSender} and is expected to send relevant changes as
 * push messages.
 *
 * The default implementation of this class is {@link GameListenerWebSocketBehavior}, which should be sufficient for
 * most cases. Custom subclasses could filter the received events to avoid creating and handling a push-request-cycle
 * for unrelated events.
 */
public abstract class AbstractGameListenerWebSocketBehavior extends WebSocketBehavior {

	private transient GameEventListener listener = null;

	@Override
	protected void onConnect(ConnectedMessage message) {
		discardListener();
		listener = createListener(new PushMessageSender(message));
		getGameEventEmitter().addListener(listener);
	}

	@Override
	protected void onClose(ClosedMessage message) {
		discardListener();
	}

	@Override
	protected void onAbort(AbortedMessage message) {
		discardListener();
	}

	private GameEventEmitter getGameEventEmitter() {
		return MyWicketApplication.get().getDependency(GameEventEmitter.class);
	}

	private void discardListener() {
		if (listener != null) {
			getGameEventEmitter().removeListener(listener);
			destroyListener(listener);
			listener = null;
		}
	}

	protected abstract GameEventListener createListener(PushMessageSender pushMessageSender);

	protected void destroyListener(GameEventListener listener) {
	}

}
