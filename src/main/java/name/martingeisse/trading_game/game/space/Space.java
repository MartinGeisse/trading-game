package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * Keeps track of all the game's {@link SpaceObject}s.
 */
public final class Space {

	private final List<SpaceObject> spaceObjects = new ArrayList<>();
	private ImmutableList<SpaceObject> spaceObjectsImmutable = null;
	private final Map<Long, SpaceObject> spaceObjectsById = new HashMap<>();

	/**
	 * Adds an object to this space.
	 */
	public void add(SpaceObject spaceObject) {
		long id = spaceObjects.size();
		spaceObject.setId(id);
		spaceObjects.add(spaceObject);
		spaceObjectsById.put(id, spaceObject);
		spaceObjectsImmutable = null;
	}

	/**
	 * Getter method.
	 *
	 * @return the spaceObjects
	 */
	public ImmutableList<SpaceObject> getSpaceObjects() {
		if (spaceObjectsImmutable == null) {
			spaceObjectsImmutable = ImmutableList.copyOf(spaceObjects);
		}
		return spaceObjectsImmutable;
	}

	/**
	 * Gets a space object by id.
	 */
	public SpaceObject get(long id) {
		return spaceObjectsById.get(id);
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
