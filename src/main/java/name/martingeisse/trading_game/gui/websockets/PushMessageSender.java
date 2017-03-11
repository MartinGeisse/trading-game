package name.martingeisse.trading_game.gui.websockets;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.protocol.ws.api.message.AbstractClientMessage;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.protocol.ws.api.registry.IKey;
import org.apache.wicket.protocol.ws.api.registry.IWebSocketConnectionRegistry;

/**
 * Sends push messages to the server side of a single web sockets connection.
 */
public final class PushMessageSender {

	private final Application application;
	private final String sessionId;
	private final IKey key;

	public PushMessageSender(Application application, String sessionId, IKey key) {
		this.application = application;
		this.sessionId = sessionId;
		this.key = key;
	}

	public PushMessageSender(AbstractClientMessage message) {
		this.application = message.getApplication();
		this.sessionId = message.getSessionId();
		this.key = message.getKey();
	}

	public void send(IWebSocketPushMessage message) {
		IWebSocketConnectionRegistry registry = WebSocketSettings.Holder.get(application).getConnectionRegistry();
		IWebSocketConnection connection = registry.getConnection(application, sessionId, key);
		if (connection != null) {
			connection.sendMessage(message);
		}
		// TODO else? can only happen if Wicket "forgets" to handle closed connections propertly. Should
		// clean up the game listener anyway, and probably detect why this happens and file a bug ticket for Wicket
	}

}
