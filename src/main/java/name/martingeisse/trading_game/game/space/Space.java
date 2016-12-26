package name.martingeisse.trading_game.game.space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Keeps track of all the game's {@link SpaceObject}s.
 */
public final class Space {

	private final List<SpaceObject> spaceObjects = new ArrayList<>();

	/**
	 * Getter method.
	 *
	 * @return the spaceObjects
	 */
	public List<SpaceObject> getSpaceObjects() {
		return spaceObjects;
	}

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick() {
		for (SpaceObject spaceObject : spaceObjects) {
			spaceObject.tick();
		}
	}

}
