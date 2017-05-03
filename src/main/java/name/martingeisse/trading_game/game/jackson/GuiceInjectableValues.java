package name.martingeisse.trading_game.game.jackson;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.google.inject.Injector;

/**
 *
 */
public class GuiceInjectableValues extends InjectableValues {

	private final Injector injector;

	public GuiceInjectableValues(Injector injector) {
		this.injector = injector;
	}

	@Override
	public Object findInjectableValue(Object valueId, DeserializationContext ctxt, BeanProperty forProperty, Object beanInstance) {
		if (!(valueId instanceof String)) {
			String type = (valueId == null) ? "[null]" : valueId.getClass().getName();
			throw new IllegalArgumentException("Unrecognized inject value id type (" + type + "), expecting String");
		}
		String stringKey = (String) valueId;
		Class<?> classKey;
		try {
			classKey = Class.forName(stringKey);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unrecognized inject value id: " + stringKey);
		}
		return injector.getInstance(classKey);
	}

}
