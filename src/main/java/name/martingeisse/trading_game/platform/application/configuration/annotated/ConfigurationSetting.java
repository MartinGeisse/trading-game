package name.martingeisse.trading_game.platform.application.configuration.annotated;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationSetting {

	public static final String NO_DEFAULT_VALUE = "!=.__SPECIAL_VALUE_THAT_INDICATES_NO_DEFAULT_VALUE__";

	public String name();

	public boolean optional() default false;

	public String defaultValue() default NO_DEFAULT_VALUE;

}
