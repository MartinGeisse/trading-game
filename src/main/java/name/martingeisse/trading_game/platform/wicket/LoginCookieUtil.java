package name.martingeisse.trading_game.platform.wicket;

import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerRepository;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;

import javax.servlet.http.Cookie;

/**
 *
 */
public final class LoginCookieUtil {

	// prevent instantiation
	private LoginCookieUtil() {
	}

	/**
	 * Gets the player ID as specified in the "login" cookie in the current request cycle's web request, or null if
	 * not possible or invalid (e.g. if the cookie's login token is wrong).
	 * <p>
	 * This method can only be used in the context of a Wicket request.
	 */
	public Long getPlayerIdFromCookie() {
		Request untypedRequest = RequestCycle.get().getRequest();
		if (!(untypedRequest instanceof WebRequest)) {
			return null;
		}
		WebRequest request = (WebRequest) untypedRequest;
		Cookie providedLoginStringCookie = request.getCookie("login");
		if (providedLoginStringCookie == null) {
			return null;
		}
		String providedLoginString = providedLoginStringCookie.getValue();
		if (providedLoginString == null) {
			return null;
		}
		int index = providedLoginString.indexOf(':');
		if (index == -1) {
			return null;
		}
		long providedPlayerId;
		try {
			providedPlayerId = Long.parseLong(providedLoginString.substring(0, index));
		} catch (NumberFormatException e) {
			return null;
		}
		String providedLoginToken = providedLoginString.substring(index + 1);
		Player player;
		try {
			player = MyWicketApplication.get().getDependency(PlayerRepository.class).getPlayerById(providedPlayerId);
		} catch (IllegalArgumentException e) {
			return null;
		}
		if (player != null && player.getLoginToken().equals(providedLoginToken)) {
			return providedPlayerId;
		} else {
			return null;
		}
	}

	/**
	 * Sends a "login" cookie through the current request cycle's response, containing the specified player ID and
	 * login token.
	 * <p>
	 * This method can only be used in the context of a Wicket request.
	 */
	public void sendCookie(Long playerId, String loginToken) {
		Response untypedResponse = RequestCycle.get().getResponse();
		if (!(untypedResponse instanceof WebResponse)) {
			throw new IllegalStateException("current response is not a web response");
		}
		WebResponse response = (WebResponse) untypedResponse;
		String loginString;
		if (playerId == null || loginToken == null) {
			loginString = "";
		} else {
			loginString = playerId + ':' + loginToken;
		}
		Cookie cookie = new Cookie("login", loginString);
		cookie.setMaxAge(10 * 365 * 24 * 60 * 60);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
