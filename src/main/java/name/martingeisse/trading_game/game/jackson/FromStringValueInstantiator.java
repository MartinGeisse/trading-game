package name.martingeisse.trading_game.game.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

import java.io.IOException;
import java.util.function.Function;

/**
 * Instantiates a value from a string-typed name or serialized value found in JSON.
 */
public class FromStringValueInstantiator<T> extends ValueInstantiator.Base {

	private final Function<String, T> actualInstantiator;

	public FromStringValueInstantiator(Class<T> valueClass, Function<String, T> actualInstantiator) {
		super(valueClass);
		this.actualInstantiator = actualInstantiator;
	}

	@Override
	public boolean canCreateFromString() {
		return true;
	}

	@Override
	public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
		Object instance = actualInstantiator.apply(value);
		if (instance == null) {
			throw new RuntimeException("deserialization of value " + value + " resulted in null");
		}
		return instance;
	}

}
