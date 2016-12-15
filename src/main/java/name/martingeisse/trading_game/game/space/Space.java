package name.martingeisse.trading_game.game.space;

import java.util.HashSet;
import java.util.Set;

/**
 * Keeps track of all the game's {@link SpaceObject}s.
 */
public final class Space {

	private final Set<SpaceObject> spaceObjects = new HashSet<>();

	/**
	 * Getter method.
	 *
	 * @return the spaceObjects
	 */
	public Set<SpaceObject> getSpaceObjects() {
		return spaceObjects;
	}

	/**
	 * Advances game logic.
	 */
	public void tick() {
		for (SpaceObject spaceObject : spaceObjects) {
			spaceObject.tick();
		}
	}

}
