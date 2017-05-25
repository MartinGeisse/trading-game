package name.martingeisse.trading_game.peripherals.feedback;

/**
 * Provides contextual information for user-submitted feedback. Implementations should be simple data containers that
 * are serializable by Jackson.
 */
public interface FeedbackContext {

	default public String getType() {
		return getClass().getName();
	}

}
