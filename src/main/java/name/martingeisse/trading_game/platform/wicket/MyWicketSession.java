package name.martingeisse.trading_game.platform.wicket;

import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerFactory;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 *
 */
public class MyWicketSession extends WebSession {

	private Long playerId = null;

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
	public Long getPlayerId() {
		return playerId;
	}

	/**
	 * Setter method.
	 *
	 * @param playerId the playerId
	 */
	public void setPlayerId(Long playerId) {
		bind();
		this.playerId = playerId;
	}

	/**
	 * Creates a new player and sets that new player's ID as the current player ID.
	 */
	public void createPlayer() {
		setPlayerId(MyWicketApplication.get().getDependency(PlayerFactory.class).createPlayer().getId());
	}

	/**
	 * Obtains the current player from the player ID set in this session.
	 */
	public Player getPlayer() {
		if (playerId == null) {
			return null;
		} else {
			return MyWicketApplication.get().getDependency(EntityProvider.class).getPlayer(playerId);
		}
	}

}
