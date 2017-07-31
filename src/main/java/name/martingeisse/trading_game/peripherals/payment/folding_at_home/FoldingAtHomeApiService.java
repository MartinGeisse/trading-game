package name.martingeisse.trading_game.peripherals.payment.folding_at_home;

import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Singleton
public class FoldingAtHomeApiService {

	private static final String BASE_URL = "http://folding.stanford.edu/stats/api";

	private final HttpClient httpClient;
	private final JacksonService jacksonService;

	@Inject
	public FoldingAtHomeApiService(HttpClient httpClient, JacksonService jacksonService) {
		this.httpClient = httpClient;
		this.jacksonService = jacksonService;
	}

	public TeamScore getTeamScore() {
		// note: /team/TEAM_ID should have worked, but the API is buggy and doesn't return the "rank" field for that URL
		Map<String, Object> data = fetchMap(BASE_URL + "/teams?team=" + FoldingAtHomeConstants.TEAM_ID);
		if (!(data.get("results") instanceof List)) {
			throw new RuntimeException("missing results list");
		}
		List<?> results = (List<?>) data.get("results");
		if (results.size() != 1) {
			throw new RuntimeException("wrong number of results: " + results.size());
		}
		if (!(results.get(0) instanceof Map)) {
			throw new RuntimeException("invalid result element structure");
		}
		return parseTeamScore((Map<String, Object>) results.get(0));
	}

	public UserScore getUserScore(String name) {
		return parseUserScore(fetchMap(BASE_URL + "/donor/" + name));
	}

	private Map<String, Object> fetchMap(String url) {
		try {
			HttpGet request = new HttpGet(url);
			HttpResponse response = httpClient.execute(request);
			try {
				if (response.getStatusLine().getStatusCode() == 404) {
					return null;
				} else if (response.getStatusLine().getStatusCode() != 200) {
					throw new RuntimeException("unexpected status code: " + response.getStatusLine().getStatusCode());
				}
				HttpEntity responseBody = response.getEntity();
				if (responseBody.getContentType() != null) {
					if (!responseBody.getContentType().getValue().equals("application/json")) {
						if (!responseBody.getContentType().getValue().equals("application/json; charset=utf-8")) {
							throw new RuntimeException("unexpected response content type: " + responseBody.getContentType().getValue());
						}
					}
				}
				return jacksonService.deserialize(responseBody.getContent(), Map.class);
			} finally {
				response.getEntity().getContent().close();
			}
		} catch (IOException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	private UserScore parseUserScore(Map<String, ?> data) {
		if (data == null) {
			return new UserScore(0, 0);
		} else {
			return new UserScore(parseInt(data, "wus"), parseInt(data, "credit"));
		}
	}

	private TeamScore parseTeamScore(Map<String, ?> data) {
		if (data == null) {
			throw new RuntimeException("team score record not found");
		} else {
			return new TeamScore(parseInt(data, "wus"), parseInt(data, "credit"), parseInt(data, "rank"));
		}
	}

	private static int parseInt(Map<String, ?> data, String key) {
		try {
			return (Integer) data.get(key);
		} catch (Exception e) {
			return -1;
		}
	}

}
