package name.martingeisse.trading_game.game.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiators;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.skill.Skill;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.game.space.SpaceObjectType;
import name.martingeisse.trading_game.platform.util.attribute.AttributeKey;
import name.martingeisse.trading_game.platform.util.attribute.Attributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * (De-)serializes game objects.
 */
@Singleton
public final class JacksonService {

	private final Injector injector;
	private final ObjectMapper objectMapper;
	private final Map<Class<?>, ValueInstantiator> valueInstantiators = new HashMap<>();
	private final ThreadLocal<Attributes> contextAttributesHolder = new ThreadLocal();

	@Inject
	public JacksonService(Injector injector) {
		this.injector = injector;

		// value instantiators and custom deserializers
		addFromLongValueInstantiator(Player.class, id -> injector.getInstance(EntityProvider.class).getPlayer(id));
		addFromStringValueInstantiator(Skill.class, name -> injector.getInstance(GameDefinition.class).getSkillByName(name));
		addFromStringValueInstantiator(ItemType.class, name -> injector.getInstance(GameDefinition.class).getItemTypeByName(name));
		addFromLongValueInstantiator(SpaceObject.class, id -> injector.getInstance(EntityProvider.class).getSpaceObject(id));
		for (SpaceObjectType type : SpaceObjectType.values()) {
			addSpaceObjectSubtypeInstantiator(type.getSpaceObjectClass());
		}

		// create basic object mapper
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

		// register a custom module to add custom de/serializers etc.
		objectMapper.registerModule(new Module() {

			@Override
			public String getModuleName() {
				return "application";
			}

			@Override
			public Version version() {
				return Version.unknownVersion();
			}

			@Override
			public void setupModule(SetupContext context) {
				context.addValueInstantiators(new ValueInstantiators() {
					@Override
					public ValueInstantiator findValueInstantiator(DeserializationConfig config, BeanDescription beanDesc, ValueInstantiator defaultInstantiator) {
						ValueInstantiator valueInstantiator = valueInstantiators.get(beanDesc.getBeanClass());
						return valueInstantiator != null ? valueInstantiator : defaultInstantiator;
					}
				});
			}
		});

		// support Guice-injectable values
		objectMapper.setInjectableValues(new GuiceInjectableValues(injector));

	}

	private <T extends SpaceObject> void addSpaceObjectSubtypeInstantiator(Class<T> c) {
		addFromLongValueInstantiator(c, id -> c.cast(injector.getInstance(EntityProvider.class).getSpaceObject(id)));
	}

	private <T> void addFromLongValueInstantiator(Class<T> klass, Function<Long, T> actualInstantiator) {
		valueInstantiators.put(klass, new FromLongValueInstantiator<T>(klass, actualInstantiator));
	}

	private <T> void addFromStringValueInstantiator(Class<T> klass, Function<String, T> actualInstantiator) {
		valueInstantiators.put(klass, new FromStringValueInstantiator<>(klass, actualInstantiator));
	}

	public String serialize(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			throw new UnexpectedExceptionException(e);
		}
	}

	public <T> T deserialize(String json, Class<T> klass) {
		return deserialize(json, klass, null);
	}

	public <T> T deserialize(String json, Class<T> klass, Attributes contextAttributes) {
		Attributes oldContextAttributes = contextAttributesHolder.get();
		try {
			contextAttributesHolder.set(contextAttributes);
			return objectMapper.readValue(json, klass);
		} catch (IOException e) {
			throw new UnexpectedExceptionException(e);
		} finally {
			contextAttributesHolder.set(oldContextAttributes);
		}
	}

	private <T> T getContextAttribute(AttributeKey<T> key) {
		Attributes contextAttributes = contextAttributesHolder.get();
		return (contextAttributes == null ? null : contextAttributes.get(key));
	}

}
