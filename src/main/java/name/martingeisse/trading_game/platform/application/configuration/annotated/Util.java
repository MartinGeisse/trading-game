package name.martingeisse.trading_game.platform.application.configuration.annotated;

import name.martingeisse.trading_game.platform.application.configuration.ApplicationConfiguration;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.platform.util.parameter.ParameterException;
import name.martingeisse.trading_game.platform.util.parser.*;

import java.lang.reflect.Method;

/**
 *
 */
class Util {

	static void assignSettingsToSetters(ApplicationConfiguration configuration, AnnotatedConfigurationParticipant participant) throws ParameterException {
		for (Method method : participant.getClass().getMethods()) {
			ConfigurationSetting annotation = method.getAnnotation(ConfigurationSetting.class);
			if (annotation == null) {
				continue;
			}
			Class<?> parameterType;
			{
				Class<?>[] parameterClasses = method.getParameterTypes();
				if (parameterClasses.length != 1) {
					throw newAnnotationException(participant, method, "method must have exactly one parameter");
				}
				parameterType = parameterClasses[0];
			}
			Parser<?> parser = determineParser(parameterType);
			Object value = determineSettingValue(configuration, parser, annotation);
			try {
				method.invoke(participant, value);
			} catch (Exception e) {
				throw new UnexpectedExceptionException(e);
			}
		}
	}

	private static RuntimeException newAnnotationException(AnnotatedConfigurationParticipant participant, Method method, String text) {
		return new RuntimeException("error in annotation participant " + participant.getClass() + ", method " + method + ": " + text);
	}

	private static Parser<?> determineParser(Class<?> type) {
		if (type.equals(String.class)) {
			return StringParser.INSTANCE;
		} else if (type.equals(Integer.class)) {
			return IntegerParser.INSTANCE;
		} else if (type.equals(Boolean.class)) {
			return BooleanWordParser.INSTANCE;
		} else if (type.isEnum()) {
			return new EnumParser<>((Class)type);
		} else {
			throw new IllegalArgumentException("no configuration parser known for type " + type);
		}
	}

	private static <T> T determineSettingValue(ApplicationConfiguration configuration, Parser<T> parser, ConfigurationSetting annotation) throws ParameterException {
		String name = annotation.name();
		if (annotation.optional()) {
			String defaultValue = annotation.defaultValue();
			if (defaultValue.equals(ConfigurationSetting.NO_DEFAULT_VALUE)) {
				return configuration.getOptionalParameter(name, parser);
			} else {
				return configuration.getOptionalParameter(name, defaultValue, parser);
			}
		} else {
			return configuration.getRequiredParameter(name, parser);
		}
	}

}
