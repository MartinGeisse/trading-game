package name.martingeisse.trading_game.platform.wicket;

import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;

import javax.servlet.http.Cookie;

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

	public void createPlayer() {
		playerId = MyWicketApplication.get().getDependency(PlayerRepository.class).createPlayer();
	}

	public void determinePlayerIdFromCookie() {
		playerId = null;
		WebRequest request = (WebRequest) RequestCycle.get().getRequest();
		Cookie providedLoginStringCookie = request.getCookie("login");
		if (providedLoginStringCookie != null) {
			String providedLoginString = providedLoginStringCookie.getValue();
			if (providedLoginString != null) {
				int index = providedLoginString.indexOf(':');
				if (index != -1) {
					long providedPlayerId;
					try {
						providedPlayerId = Long.parseLong(providedLoginString.substring(0, index));
					} catch (NumberFormatException e) {
						return;
					}
					String providedLoginToken = providedLoginString.substring(index + 1);
					Player player = MyWicketApplication.get().getDependency(PlayerRepository.class).getPlayerById(providedPlayerId);
					if (player != null && player.getLoginToken().equals(providedLoginToken)) {
						playerId = providedPlayerId;
					}
				}
			}
		}
	}

	public void storePlayerIdToCookie() {
		Player player = getPlayer();
		String loginString = (player == null ? "" : (player.getId() + ":" + player.getLoginToken()));
		WebResponse response = (WebResponse) RequestCycle.get().getResponse();
		Cookie cookie = new Cookie("login", loginString);
		cookie.setMaxAge(10 * 365 * 24 * 60 * 60);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public Long getPlayerId() {
		return playerId;
	}

	public Player getPlayer() {
		bind();
		if (playerId == null) {
			return null;
		} else {
			return MyWicketApplication.get().getDependency(PlayerRepository.class).getPlayerById(playerId);
		}
	}

	public void unsetPlayerId() {
		playerId = null;
	}

}
