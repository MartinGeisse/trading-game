package name.martingeisse.trading_game.peripherals.feedback;

import name.martingeisse.trading_game.game.jackson.JacksonService;

/**
 *
 */
public class SubmitFeedbackService {

	private final JacksonService jacksonService;

	public SubmitFeedbackService(JacksonService jacksonService) {
		this.jacksonService = jacksonService;
	}

	public void submitFeedback(String sessionId, long playerId, FeedbackContext context, String text) {
		String serializedContext = jacksonService.serialize(context);
		System.out.println("feedback by session " + sessionId + ", playerId " + playerId + ", context " + serializedContext + ": " + text);
	}

}
