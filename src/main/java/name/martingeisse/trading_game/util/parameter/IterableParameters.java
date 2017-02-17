package name.martingeisse.trading_game.util.parameter;

import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Implemented by {@link Parameters} that are able to iterator over all parameters.
 */
public interface IterableParameters extends Parameters {

	/**
	 * Returns the names of all parameters.
	 *
	 * @return the names
	 */
	public Iterable<String> getAllNames();

	/**
	 * Returns the names and values of all parameters.
	 *
	 * @return the names and values
	 */
	default public Iterable<Pair<String, String>> getAllParameters() {
		return () -> new TransformIterator<>(getAllNames().iterator(), name -> Pair.of(name, getOptionalParameter(name)));
	}

	/**
	 * Passes all parameter names to the specified action.
	 *
	 * @param action the action to perform for each name
	 */
	default public void forEachName(Consumer<String> action) {
		getAllNames().forEach(action);
	}

	/**
	 * Passes all parameter names and values to the specified action.
	 *
	 * @param action the action to perform for each name
	 */
	default public void forEach(BiConsumer<String, String> action) {
		for (String name : getAllNames()) {
			action.accept(name, getOptionalParameter(name));
		}
	}

}
