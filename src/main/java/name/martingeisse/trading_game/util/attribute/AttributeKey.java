package name.martingeisse.trading_game.util.attribute;

/**
 * <p>
 * This class is used for the keys of {@link Attributes} objects.
 * </p>
 * <p>
 * Comparison of keys is based on object identity. A name may be specified when creating a key.
 * However, this name has no effect on key comparison -- it is only used to make {@link #toString()}
 * more useful.
 * </p>
 *
 * @param <T> the type of the attribute value for this key
 */
@SuppressWarnings("unused")
public final class AttributeKey<T> {

	private final String symbolicName;
	private final String readableName;

	/**
	 * Constructor.
	 *
	 * @param symbolicName
	 * @param readableName
	 */
	public AttributeKey(String symbolicName, String readableName) {
		this.symbolicName = symbolicName;
		this.readableName = readableName;
	}

	/**
	 * Getter method.
	 *
	 * @return the symbolicName
	 */
	public String getSymbolicName() {
		return symbolicName;
	}

	/**
	 * Getter method.
	 *
	 * @return the readableName
	 */
	public String getReadableName() {
		return readableName;
	}

	// override
	@Override
	public String toString() {
		return symbolicName == null ? "?" : symbolicName;
	}

}
