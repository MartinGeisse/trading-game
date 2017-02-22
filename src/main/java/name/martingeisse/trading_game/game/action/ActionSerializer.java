package name.martingeisse.trading_game.game.action;

/**
 * Provides a serialized representation of actions as strings.
 */
public interface ActionSerializer {

	public String serializeAction(Action action);

	public Action deserializeAction(String serializedAction);

}
