package name.martingeisse.trading_game.gui.websockets;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.event.GameEvent;
import name.martingeisse.trading_game.game.event.GameEventListener;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

/**
 * Default web socket behavior that listens to all game events and pushes them as {@link GameEventBatchPushMessage}.
 * The onPush() method handles that message and invokes {@link #onGameEventBatch(WebSocketRequestHandler, ImmutableList)}.
 * Implement the latter to react to game events within a push-request-cycle.
 */
public abstract class GameListenerWebSocketBehavior extends AbstractGameListenerWebSocketBehavior {

	@Override
	protected GameEventListener createListener(PushMessageSender pushMessageSender) {
		return new MyListener(pushMessageSender);
	}

	@Override
	protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
		if (message instanceof GameEventBatchPushMessage) {
			onGameEventBatch(handler, ((GameEventBatchPushMessage) message).getEvents());
		}
	}

	protected abstract void onGameEventBatch(WebSocketRequestHandler handler, ImmutableList<GameEvent> eventBatch);

	private static class MyListener implements GameEventListener {

		private final PushMessageSender pushMessageSender;

		public MyListener(PushMessageSender pushMessageSender) {
			this.pushMessageSender = pushMessageSender;
		}

		@Override
		public void receiveGameEventBatch(ImmutableList<GameEvent> eventBatch) {
			pushMessageSender.send(new GameEventBatchPushMessage(eventBatch));
		}

	}

}
