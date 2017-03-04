package name.martingeisse.trading_game.game;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;

import java.io.IOException;

/**
 * (De-)serializes game objects.
 */
@Singleton
public final class JacksonService {

	private final ObjectMapper objectMapper;

	@Inject
	public JacksonService() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
		objectMapper.disable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
		objectMapper.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
		// objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.disable(SerializationFeature.CLOSE_CLOSEABLE);
		// objectMapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
		// objectMapper.enable(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS);
		objectMapper.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
		objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
		// objectMapper.enable(FAIL_ON_MISSING_CREATOR_PROPERTIES);
		objectMapper.disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
		objectMapper.disable(MapperFeature.AUTO_DETECT_CREATORS);
		objectMapper.disable(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS);
		// whether property naming strategies should also affect explicit names
		// objectMapper.enable(ALLOW_EXPLICIT_PROPERTY_RENAMING);
		// anything time-related
		// SerializationFeature.WRITE_DATES_AS_TIMESTAMPS -- check with Joda types if needed
		// SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS -- check with Joda types if needed
		// SerializationFeature.WRITE_DATES_WITH_ZONE_ID -- check with Joda types if needed
		// SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS -- check with Joda types if needed
		// SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS -- check with Joda types if needed
		// DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS -- check with Joda types if needed
		// DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE -- check with Joda types if needed
	}

	public String serialize(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	public <T> T deserialize(String json, Class<T> klass) {
		try {
			return objectMapper.readValue(json, klass);
		} catch (IOException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	/**
	 * Getter method.
	 *
	 * @return the objectMapper
	 */
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

}
