package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.space.quadtree.Quadtree;

import java.util.*;

/**
 * Keeps track of all the game's {@link SpaceObject}s.
 *
 * Building phase: After construction, add static space objects, then call {@link #initializeStatic()}, then start
 * adding nonstatic objects.
 */
public final class Space {

	private final List<SpaceObject> spaceObjects = new ArrayList<>();
	private ImmutableList<SpaceObject> spaceObjectsImmutable = null;
	private final Map<Long, SpaceObject> spaceObjectsById = new HashMap<>();
	private Quadtree quadtree;

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
	 * Initializes data for static space objects. Should be called after adding all static objects and before
	 * adding any nonstatic objects.
	 */
	public void initializeStatic() {
		quadtree = new Quadtree(this, 10);
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
