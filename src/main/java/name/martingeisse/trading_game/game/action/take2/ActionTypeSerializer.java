package name.martingeisse.trading_game.game.action.take2;

/**
 * Provides a serialized representation of action types as strings.
 */
public interface ActionTypeSerializer {

	public String serializeActionType(ActionType actionType);

	public ActionType deserializeActionType(String serializedActionType);

}
