package name.martingeisse.trading_game.util.parameter;

import name.martingeisse.trading_game.util.parser.Parser;

/**
 * Encapsulates the information needed to call one of the {@link Parameters} methods, together with
 * an optional description.
 *
 * @param <T> the static parameter type
 */
public final class ParameterSpecification<T> {

	private final String name;
	private final boolean optional;
	private final Parser<T> parser;
	private final Getter<T> getter;
	private final String description;

	/**
	 * Constructor.
	 */
	private ParameterSpecification(String name, boolean optional, Parser<T> parser, Getter<T> getter) {
		this(name, optional, parser, getter, null);
	}

	/**
	 * Constructor.
	 */
	private ParameterSpecification(String name, boolean optional, Parser<T> parser, Getter<T> getter, String description) {
		if (name == null) {
			throw new IllegalArgumentException("parameter name is required");
		}
		if (getter == null) {
			throw new IllegalArgumentException("getter is required");
		}
		this.name = name;
		this.optional = optional;
		this.parser = parser;
		this.getter = getter;
		this.description = description;
	}

	/**
	 * Getter method.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method.
	 *
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * Getter method.
	 *
	 * @return the parser
	 */
	public Parser<T> getParser() {
		return parser;
	}

	/**
	 * Getter method.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Creates a copy of this object with another description.
	 *
	 * @param newDescription the new description to use
	 * @return the changed copy
	 */
	public ParameterSpecification<T> withDescription(String newDescription) {
		return new ParameterSpecification<>(name, optional, parser, getter, newDescription);
	}

	/**
	 * Gets the parameter defined by this specification from a parameter set. This handles
	 * optionality and parsing automatically.
	 *
	 * @param paramters the parameter set
	 * @return the parameter value, default value, or null (as defined by this specification)
	 * @throws ParameterException on errors
	 */
	public T get(Parameters paramters) throws ParameterException {
		return getter.get(paramters);
	}

	/**
	 * @param name the parameter name
	 * @return the parameter specification
	 */
	public static ParameterSpecification<String> forOptionalParameter(String name) {
		return new ParameterSpecification<>(name, true, null, p -> p.getOptionalParameter(name));
	}

	/**
	 * @param name         the parameter name
	 * @param defaultValue the default value
	 * @return the parameter specification
	 */
	public static ParameterSpecification<String> forOptionalParameter(String name, String defaultValue) {
		return new ParameterSpecification<>(name, true, null, p -> p.getOptionalParameter(name, defaultValue));
	}

	/**
	 * @param name the parameter name
	 * @return the parameter specification
	 */
	public static ParameterSpecification<String> forRequiredParameter(String name) {
		return new ParameterSpecification<>(name, false, null, p -> p.getRequiredParameter(name));
	}

	/**
	 * @param <T>    the static parameter type
	 * @param name   the parameter name
	 * @param parser the parameter parser
	 * @return the parameter specification
	 */
	public static <T> ParameterSpecification<T> forOptionalParameter(String name, Parser<T> parser) {
		return new ParameterSpecification<>(name, true, parser, p -> p.getOptionalParameter(name, parser));
	}

	/**
	 * @param <T>          the static parameter type
	 * @param name         the parameter name
	 * @param defaultValue the default value
	 * @param parser       the parameter parser
	 * @return the parameter specification
	 */
	public static <T> ParameterSpecification<T> forOptionalParameter(String name, String defaultValue, Parser<T> parser) {
		return new ParameterSpecification<>(name, true, parser, p -> p.getOptionalParameter(name, defaultValue, parser));
	}

	/**
	 * @param <T>          the static parameter type
	 * @param name         the parameter name
	 * @param parser       the parameter parser
	 * @param defaultValue the default value
	 * @return the parameter specification
	 */
	public static <T> ParameterSpecification<T> forOptionalParameter(String name, Parser<T> parser, T defaultValue) {
		return new ParameterSpecification<>(name, true, parser, p -> p.getOptionalParameter(name, parser, defaultValue));
	}

	/**
	 * @param <T>    the static parameter type
	 * @param name   the parameter name
	 * @param parser the parameter parser
	 * @return the parameter specification
	 */
	public static <T> ParameterSpecification<T> forRequiredParameter(String name, Parser<T> parser) {
		return new ParameterSpecification<>(name, false, parser, p -> p.getRequiredParameter(name, parser));
	}

	private interface Getter<T> {
		T get(Parameters parameters) throws ParameterException;
	}

}
