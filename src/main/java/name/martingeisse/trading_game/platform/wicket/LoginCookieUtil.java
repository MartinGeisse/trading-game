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
	 * Gets the player ID as specified in the "loginToken" cookie in the current request cycle's web request, or null if
	 * not possible or invalid (e.g. if the cookie's login token is wrong).
	 * <p>
	 * This method can only be used in the context of a Wicket request.
	 */
	public static Long getPlayerIdFromCookie() {
		Request untypedRequest = RequestCycle.get().getRequest();
		if (!(untypedRequest instanceof WebRequest)) {
			return null;
		}
		WebRequest request = (WebRequest) untypedRequest;
		Cookie providedLoginTokenCookie = request.getCookie("loginToken");
		if (providedLoginTokenCookie == null) {
			return null;
		}
		String providedLoginToken = providedLoginTokenCookie.getValue();
		if (providedLoginToken == null) {
			return null;
		}
		Player player;
		try {
			player = MyWicketApplication.get().getDependency(PlayerRepository.class).getPlayerByLoginToken(providedLoginToken);
		} catch (IllegalArgumentException e) {
			return null;
		}
		return (player == null ? null : player.getId());
	}

	/**
	 * Sends a "loginToken" cookie through the current request cycle's response, containing the
	 * specified login token.
	 * <p>
	 * This method can only be used in the context of a Wicket request.
	 */
	public static void sendCookie(String loginToken) {
		Response untypedResponse = RequestCycle.get().getResponse();
		if (!(untypedResponse instanceof WebResponse)) {
			throw new IllegalStateException("current response is not a web response");
		}
		WebResponse response = (WebResponse) untypedResponse;
		Cookie cookie = new Cookie("loginToken", loginToken == null ? "" : loginToken);
		cookie.setMaxAge(10 * 365 * 24 * 60 * 60);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
