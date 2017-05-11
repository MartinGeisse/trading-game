package name.martingeisse.trading_game.gui.websockets;

import name.martingeisse.trading_game.game.event.GameEventBatch;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.event.GameEventListener;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.WicketWebSocketJQueryResourceReference;
import org.apache.wicket.protocol.ws.api.message.*;
import org.apache.wicket.protocol.ws.api.registry.IKey;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.visit.Visits;

/**
 * This behavior can be added to a page to make all components which implement {@link GuiGameEventListener} receive
 * game events.
 * <p>
 * Only pages can be targeted by this behavior to avoid having two such behaviors, and thus two websocket connections,
 * within a single page.
 * <p>
 * Note that components implementing just {@link GameEventListener} are not supported since there is little point in
 * doing so -- such a component would not be able to update itsef on the client side in response to events.
 *
 * TODO: if no message gets sent, the websocket times out (why? timeout should handle lost connections, not idle ones).
 * Investigate. If this is correct behavior, send keepalive messages (from the client, to make sure they are still
 * active. If only the server sends keepalives, lost clients might go unnoticed)
 * --> http://stackoverflow.com/questions/10585355/sending-websocket-ping-pong-frame-from-browser
 * TCP only detects disconnects when trying to send stuff. The timeout comes from Jetty. A solution would be regular
 * client/server initiated keepalive messages -- they prevent the timeout from happening and will also detect broken
 * connections at TCP level since stuff gets sent. --> client or server initiated?
 * --->
 * client-initiated: Server won't notice dropped connections until stuff gets sent. Blocks a TCP port, but nothing else.
 * server-initiated: client won't notice dropped connections until stuff gets sent. May confuse the user since nothing
 *   gets updated until user interaction, which then fails.
 * The server (Jetty) closesthe connection on timeout.
 * -->
 * Only client-initiated messages are needed, with the interval being shorter than Jetty's timeout.
 *
 * TODO the web socket breaks if the same page instance is opened in multiple tabs. Since websocket based pages must
 * be stateful in Wicket, this means that another page instance must be created for each tab.
 */
public class GameListenerWebSocketBehavior extends WebSocketBehavior {

	private Page page;
	private transient SourceListener sourceListener;

	@Override
	public void bind(Component component) {
		super.bind(component);
		if (!(component instanceof Page)) {
			throw new RuntimeException("this behavior can only be used with a page");
		}
		if (this.page != null) {
			throw new IllegalStateException("cannot use this behavior for more than one page");
		}
		this.page = (Page) component;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);

		// Render a simple timer that sends a dummy text message after half the timeout. This message
		// will be passed directly to the onMessage method of this behavior, which ignores it by default.
		// TODO: Only send a dummy message if no real message was sent or received in the meantime
		StringBuilder builder = new StringBuilder();
		builder.append("setInterval(function() {");
		builder.append("Wicket.WebSocket.INSTANCE.send('keepalive');");
		builder.append("}, ").append(WebSocketConstants.TIMEOUT_MILLISECONDS / 2).append(");");
		response.render(JavaScriptHeaderItem.forScript(builder.toString(), null));

	}

	@Override
	protected void onConnect(ConnectedMessage message) {
		discardListener();
		sourceListener = new SourceListener(message);
		MyWicketApplication.get().getDependency(GameEventEmitter.class).addListener(sourceListener);
	}

	@Override
	protected void onClose(ClosedMessage message) {
		discardListener();
	}

	@Override
	protected void onAbort(AbortedMessage message) {
		discardListener();
	}

	private void discardListener() {
		if (sourceListener != null) {
			MyWicketApplication.get().getDependency(GameEventEmitter.class).removeListener(sourceListener);
			sourceListener = null;
		}
	}

	@Override
	protected void onPush(WebSocketRequestHandler partialPageRequestHandler, IWebSocketPushMessage message) {
		if (message instanceof GameEventBatchPushMessage) {
			GameEventBatch batch = ((GameEventBatchPushMessage) message).getBatch();
			Visits.visit(page, (component, visit) -> {
				if (component instanceof GuiGameEventListener) {
					((GuiGameEventListener) component).receiveGameEventBatch(partialPageRequestHandler, batch);
				}
			});
		}
	}

	/**
	 * This listener is actually registered with the game logic.
	 */
	private static final class SourceListener implements GameEventListener {

		private final Application application;
		private final String sessionId;
		private final IKey key;

		public SourceListener(AbstractClientMessage message) {
			this.application = message.getApplication();
			this.sessionId = message.getSessionId();
			this.key = message.getKey();
		}

		@Override
		public void receiveGameEventBatch(GameEventBatch eventBatch) {
			IWebSocketConnectionRegistry registry = WebSocketSettings.Holder.get(application).getConnectionRegistry();
			IWebSocketConnection connection = registry.getConnection(application, sessionId, key);
			if (connection != null) {
				connection.sendMessage(new GameEventBatchPushMessage(eventBatch));
			}
			// TODO else? can only happen if Wicket "forgets" to handle closed connections propertly. Should
			// clean up the game listener anyway, and probably detect why this happens and file a bug ticket for Wicket
		}

	}

	/**
	 * Wraps a batch of game events as an {@link IWebSocketPushMessage}.
	 */
	private static final class GameEventBatchPushMessage implements IWebSocketPushMessage {

		private final GameEventBatch batch;

		/**
		 * Constructor.
		 *
		 * @param batch the batch
		 */
		public GameEventBatchPushMessage(GameEventBatch batch) {
			this.batch = batch;
		}

		/**
		 * Getter method.
		 *
		 * @return the batch
		 */
		public GameEventBatch getBatch() {
			return batch;
		}

	}

}
