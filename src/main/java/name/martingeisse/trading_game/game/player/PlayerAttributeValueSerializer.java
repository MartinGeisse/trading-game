package name.martingeisse.trading_game.game.player;

/**
 *
 */
public interface PlayerAttributeValueSerializer {
	public String serializePlayerAttributeValue(Object value);
	public Object deserializePlayerAttributeValue(String serializedValue);
}
