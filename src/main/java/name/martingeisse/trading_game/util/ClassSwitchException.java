package name.martingeisse.trading_game.util;

/**
 * Indicates that a "class switch" (instanceof-cast-chain) encountered a class it could not handle.
 */
public class ClassSwitchException extends RuntimeException {

	/**
	 * Constructor.
	 *
	 * @param <T>         the static parent type
	 * @param parentClass the parent class
	 * @param subclass    the subclass
	 */
	public <T> ClassSwitchException(Class<T> parentClass, Class<? extends T> subclass) {
		super("unexpected subclass of " + parentClass + ": " + subclass);
	}

}
