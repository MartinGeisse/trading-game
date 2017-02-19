package name.martingeisse.trading_game.platform.wicket;

import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 *
 */
public class MyWicketSession extends WebSession {

	private long playerId = -1;

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
	public long getPlayerId() {
		return playerId;
	}

	public Player getPlayer() {
		bind();
		PlayerRepository playerRepository = MyWicketApplication.get().getDependency(PlayerRepository.class);
		if (playerId < 0) {
			playerId = playerRepository.createPlayer();
		}
		return playerRepository.getPlayerById(playerId);
	}

}
