package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.common.util.WtfException;

import java.util.HashSet;
import java.util.Set;

/**
 * Distinguishes different types of space objects. This enum corresponds to the subclasses of {@link SpaceObject}.
 */
public enum SpaceObjectType {

	ASTEROID(Asteroid.class),
	PLANET(Planet.class),
	PLAYER_SHIP(PlayerShip.class),
	SPACE_STATION(SpaceStation.class),
	STAR(Star.class);

	private static final ImmutableSet<SpaceObjectType> staticTypes;
	private static final ImmutableSet<SpaceObjectType> dynamicTypes;
	static {
		Set<SpaceObjectType> staticTypesAccumulator = new HashSet<>();
		Set<SpaceObjectType> dynamicTypesAccumulator = new HashSet<>();
		for (SpaceObjectType type : values()) {
			if (StaticSpaceObject.class.isAssignableFrom(type.getSpaceObjectClass())) {
				staticTypesAccumulator.add(type);
			} else if (DynamicSpaceObject.class.isAssignableFrom(type.getSpaceObjectClass())) {
				dynamicTypesAccumulator.add(type);
			} else {
				throw new WtfException("found space object type that is neither static nor dynamic: " + type);
			}
		}
		staticTypes = ImmutableSet.copyOf(staticTypesAccumulator);
		dynamicTypes = ImmutableSet.copyOf(dynamicTypesAccumulator);
	}

	private final Class<? extends SpaceObject> spaceObjectClass;

	SpaceObjectType(Class<? extends SpaceObject> spaceObjectClass) {
		this.spaceObjectClass = spaceObjectClass;
	}

	/**
	 * Getter method.
	 *
	 * @return the spaceObjectClass
	 */
	public Class<? extends SpaceObject> getSpaceObjectClass() {
		return spaceObjectClass;
	}

	/**
	 * Getter method.
	 *
	 * @return the supportsTick
	 */
	public boolean isSupportsTick() {
		return false;
	}

	/**
	 * Determines the enum constant for the type of the specified space object class.
	 *
	 * @param spaceObjectClass the space object class whose type to determine
	 * @return the enum constant for that type
	 */
	public static SpaceObjectType getType(Class<? extends SpaceObject> spaceObjectClass) {
		for (SpaceObjectType type : values()) {
			if (type.getSpaceObjectClass() == spaceObjectClass) {
				return type;
			}
		}
		throw new IllegalArgumentException("unknown space object type: " + spaceObjectClass);
	}

	/**
	 * Determines the enum constant for the type of the specified space object.
	 *
	 * @param spaceObject the object whose type to determine
	 * @return the enum constant for that type
	 */
	public static SpaceObjectType getType(SpaceObject spaceObject) {
		return getType(spaceObject.getClass());
	}

	/**
	 * @return all types that support the tick() method
	 */
	public static ImmutableSet<SpaceObjectType> getTypesThatSupportTick() {
		return ImmutableSet.of();
	}

	/**
	 * @return all static types
	 */
	public static ImmutableSet<SpaceObjectType> getStaticTypes() {
		return staticTypes;
	}

	/**
	 * @return all dynamic types
	 */
	public static ImmutableSet<SpaceObjectType> getDynamicTypes() {
		return dynamicTypes;
	}

}
