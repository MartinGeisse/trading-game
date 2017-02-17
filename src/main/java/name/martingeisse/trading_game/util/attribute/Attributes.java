package name.martingeisse.trading_game.util.attribute;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class Attributes {

	@SuppressWarnings("rawtypes")
	private final Map attributeKeyValues = new HashMap();

	/**
	 * Sets an attribute value.
	 *
	 * @param <T>   the static attribute type
	 * @param key   the attribute key
	 * @param value the attribute value
	 */
	@SuppressWarnings("unchecked")
	public <T> void set(AttributeKey<T> key, T value) {
		attributeKeyValues.put(key, value);
	}

	/**
	 * Gets an attribute value.
	 *
	 * @param <T> the static attribute type
	 * @param key the attribute key
	 * @return the attribute value
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(AttributeKey<T> key) {
		return (T) attributeKeyValues.get(key);
	}

	/**
	 * Gets an attribute value, falling back to the specified default value if the attribute value is null or not set.
	 *
	 * @param <T>          the static attribute type
	 * @param key          the attribute key
	 * @param defaultValue the default value
	 * @return the attribute value
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(AttributeKey<T> key, T defaultValue) {
		T value = get(key);
		return (value != null ? value : defaultValue);
	}

	// override
	@Override
	public String toString() {
		return attributeKeyValues.toString();
	}

}
