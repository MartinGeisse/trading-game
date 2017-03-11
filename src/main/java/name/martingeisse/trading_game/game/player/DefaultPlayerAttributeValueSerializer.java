package name.martingeisse.trading_game.game.player;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.jackson.JacksonService;

/**
 *
 */
@Singleton
public final class DefaultPlayerAttributeValueSerializer implements PlayerAttributeValueSerializer {

	private final Provider<JacksonService> jacksonServiceProvider;

	@Inject
	public DefaultPlayerAttributeValueSerializer(Provider<JacksonService> jacksonServiceProvider) {
		this.jacksonServiceProvider = jacksonServiceProvider;
	}

	@Override
	public String serializePlayerAttributeValue(Object value) {
		return jacksonServiceProvider.get().serialize(value);
	}

	@Override
	public Object deserializePlayerAttributeValue(String serializedValue) {
		return jacksonServiceProvider.get().deserialize(serializedValue, Object.class);
	}

}
