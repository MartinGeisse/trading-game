package name.martingeisse.trading_game.game.repository;

import name.martingeisse.trading_game.common.util.UnexpectedExceptionException;
import name.martingeisse.trading_game.game.space.*;

import java.io.Serializable;

/**
 * Distinguishes different types of space objects. This enum corresponds to the subclasses of {@link SpaceObject}.
 */
public enum SpaceObjectType {

	ASTEROID(Asteroid.class),
	PLANET(Planet.class),
	PLAYER_SHIP(PlayerShip.class),
	SPACE_STATION(SpaceStation.class),
	STAR(Star.class);

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
	 * Creates a new space object of this type.
	 *
	 * @return the newly created object
	 */
	public SpaceObject newSpaceObject() {
		try {
			return spaceObjectClass.newInstance();
		} catch (Exception e) {
			throw new UnexpectedExceptionException(e);
		}
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

}
