package name.martingeisse.trading_game.game.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

import java.io.IOException;
import java.util.function.Function;

/**
 * Instantiates a value from a long-typed database ID found in JSON.
 */
public class DatabaseValueInstantiator<T> extends ValueInstantiator.Base {

	private final Function<Long, T> actualInstantiator;

	public DatabaseValueInstantiator(Class<T> valueClass, Function<Long, T> actualInstantiator) {
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
		return actualInstantiator.apply(value);
	}

}
