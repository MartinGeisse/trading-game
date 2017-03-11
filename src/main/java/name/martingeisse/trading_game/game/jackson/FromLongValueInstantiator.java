package name.martingeisse.trading_game.game.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

import java.io.IOException;
import java.util.function.Function;

/**
 * Instantiates a value from a long-typed value found in JSON (converting int to long if needed).
 */
public class FromLongValueInstantiator<T> extends ValueInstantiator.Base {

	private final Function<Long, T> actualInstantiator;

	public FromLongValueInstantiator(Class<T> valueClass, Function<Long, T> actualInstantiator) {
		super(valueClass);
		this.actualInstantiator = actualInstantiator;
	}

	@Override
	public boolean canCreateFromInt() {
		return true;
	}

	@Override
	public boolean canCreateFromLong() {
		return true;
	}

	@Override
	public Object createFromInt(DeserializationContext ctxt, int value) throws IOException {
		return createFromLong(ctxt, value);
	}

	@Override
	public Object createFromLong(DeserializationContext ctxt, long value) throws IOException {
		Object instance = actualInstantiator.apply(value);
		if (instance == null) {
			throw new RuntimeException("deserialization of value " + value + " resulted in null");
		}
		return instance;
	}

}
