package name.martingeisse.trading_game.gui.wicket;

import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.Player;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 *
 */
public class MyWicketSession extends WebSession {

	private String playerId;

	/**
	 * Constructor.
	 *
	 * @param request the request that creates this session
	 */
	public MyWicketSession(Request request) {
		super(request);
	}

	/**
	 * Gets the session associated with the current thread.
	 */
	public static MyWicketSession get() {
		return (MyWicketSession) WebSession.get();
	}

	/**
	 * Getter method.
	 *
	 * @return the playerId
	 */
	public String getPlayerId() {
		return playerId;
	}

	/**
	 * Setter method.
	 *
	 * @param playerId the playerId
	 */
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public Player getPlayer() {
		bind();
		Game game = MyWicketApplication.get().getDependency(Game.class);
		Player player = game.getPlayer(playerId);
		playerId = player.getId();
		return player;
	}

}
