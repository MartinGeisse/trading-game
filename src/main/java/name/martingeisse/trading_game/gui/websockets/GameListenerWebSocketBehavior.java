package name.martingeisse.trading_game.gui.websockets;

import name.martingeisse.trading_game.game.event.GameEventBatch;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.event.GameEventListener;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.WicketWebSocketJQueryResourceReference;
import org.apache.wicket.protocol.ws.api.message.*;
import org.apache.wicket.protocol.ws.api.registry.IKey;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Generics;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.visit.Visits;

import java.util.Map;

/**
 * This behavior can be added to a page to make all components which implement {@link GuiGameEventListener} receive
 * game events.
 * <p>
 * Only pages can be targeted by this behavior to avoid having two such behaviors, and thus two websocket connections,
 * within a single page.
 * <p>
 * Note that components implementing just {@link GameEventListener} are not supported since there is little point in
 * doing so -- such a component would not be able to update itsef on the client side in response to events.
 */
public class GameListenerWebSocketBehavior extends WebSocketBehavior {

	private static final Logger logger = LogManager.getLogger();

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

		// the base websocket scripts must be replaced by "deferred" ones that open the websocket only after the
		// page has checked whether it is being displayed in multiple tabs
		renderDeferredBaseClassHead(component, response);

		// Render a simple timer that sends a dummy text message after half the timeout. This message
		// will be passed directly to the onMessage method of this behavior, which ignores it by default.
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(
				GameListenerWebSocketBehavior.class,
				GameListenerWebSocketBehavior.class.getSimpleName() + ".js"
		)));

	}

	private void renderDeferredBaseClassHead(Component component, IHeaderResponse response) {

		response.render(JavaScriptHeaderItem.forReference(WicketWebSocketJQueryResourceReference.get()));

		// render the init script as a priority header item since the function must be present as soon as the
		// multiple-tabs-check gets sent -- after sending it, the response calling the websocket function may arrive at
		// any time
		PackageTextTemplate webSocketSetupTemplate = new PackageTextTemplate(GameListenerWebSocketBehavior.class,
				"wicket-websocket-setup.js.tmpl");
		WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(component.getApplication());
		Map<String, Object> variables = Generics.newHashMap();
		variables.put("pageId", component.getPage().getPageId());
		variables.put("resourceName", "");
		variables.put("baseUrl", Args.notNull(getBaseUrl(webSocketSettings), "baseUrl"));
		variables.put("contextPath", Args.notNull(getContextPath(webSocketSettings), "contextPath"));
		variables.put("applicationName", component.getApplication().getName());
		variables.put("filterPrefix", Args.notNull(getFilterPrefix(webSocketSettings), "filterPrefix"));
		variables.put("gameListenerWebSocketBehaviorInitializer", GameListenerWebSocketBehavior.class.getSimpleName());
		variables.put("gameListenerWebSocketBehaviorTimeout", WebSocketConstants.TIMEOUT_MILLISECONDS);
		String script = webSocketSetupTemplate.asString(variables);
		response.render(new PriorityHeaderItem(JavaScriptHeaderItem.forScript(script, null)));

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
			} else {
				logger.error("WebSocket GameEventListener received events without having a connection. This means that a WebSocket connection broke but Jetty/Wicket didn't notice. Investigate and file a bug ticket.");
			}
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
