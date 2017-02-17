package name.martingeisse.trading_game.application.configuration.annotated;

/**
 *
 */
public @interface ConfigurationSetting {

	public static final String NO_DEFAULT_VALUE = "!=.__SPECIAL_VALUE_THAT_INDICATES_NO_DEFAULT_VALUE__";

	public String name();

	public boolean optional() default false;

	public String defaultValue() default NO_DEFAULT_VALUE;

}
