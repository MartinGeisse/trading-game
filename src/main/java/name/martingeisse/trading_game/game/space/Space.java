package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.space.quadtree.Quadtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps track of all the game's {@link SpaceObject}s.
 */
public final class Space {

	private final List<StaticSpaceObject> staticSpaceObjects = new ArrayList<>();
	private ImmutableList<StaticSpaceObject> staticSpaceObjectsImmutable = null;
	private Quadtree staticSpaceObjectsQuadtree = null;
	private final List<DynamicSpaceObject> dynamicSpaceObjects = new ArrayList<>();
	private ImmutableList<DynamicSpaceObject> dynamicSpaceObjectsImmutable = null;
	private ImmutableList<SpaceObject> spaceObjectsImmutable = null;
	private final Map<Long, SpaceObject> spaceObjectsById = new HashMap<>();
	private long idCounter = 0;
	private final List<SpaceObject> spaceObjectsThatSupportTick = new ArrayList<>();

	private void checkStaticNotSealed() {
		if (staticSpaceObjectsQuadtree != null) {
			throw new IllegalStateException("the set of static space objects has already been sealed");
		}
	}

	private void addInternal(SpaceObject spaceObject) {
		spaceObject.setId(idCounter);
		spaceObjectsById.put(idCounter, spaceObject);
		idCounter++;
		if (spaceObject.supportsTick()) {
			spaceObjectsThatSupportTick.add(spaceObject);
		}
	}

	/**
	 * Adds an object to this space.
	 */
	public void add(StaticSpaceObject spaceObject) {
		checkStaticNotSealed();
		addInternal(spaceObject);
		staticSpaceObjects.add(spaceObject);
		staticSpaceObjectsImmutable = null;
	}

	/**
	 * Seals the set of static space objects. This initializes static data like the quadtree.
	 */
	public void sealStatic() {
		checkStaticNotSealed();
		staticSpaceObjectsQuadtree = new Quadtree(this, 10);
	}


	/**
	 * Adds an object to this space.
	 */
	public void add(DynamicSpaceObject spaceObject) {
		addInternal(spaceObject);
		dynamicSpaceObjects.add(spaceObject);
		dynamicSpaceObjectsImmutable = null;
	}

	/**
	 * Getter method.
	 *
	 * @return the spaceObjects
	 */
	public ImmutableList<StaticSpaceObject> getStaticSpaceObjects() {
		if (staticSpaceObjectsImmutable == null) {
			staticSpaceObjectsImmutable = ImmutableList.copyOf(staticSpaceObjects);
		}
		return staticSpaceObjectsImmutable;
	}

	/**
	 * Getter method.
	 *
	 * @return the spaceObjects
	 */
	public ImmutableList<DynamicSpaceObject> getDynamicSpaceObjects() {
		if (dynamicSpaceObjectsImmutable == null) {
			dynamicSpaceObjectsImmutable = ImmutableList.copyOf(dynamicSpaceObjects);
		}
		return dynamicSpaceObjectsImmutable;
	}

	/**
	 * Getter method.
	 *
	 * @return the spaceObjects
	 */
	public ImmutableList<SpaceObject> getSpaceObjects() {
		if (spaceObjectsImmutable == null) {
			ArrayList<SpaceObject> spaceObjects = new ArrayList<>();
			spaceObjects.addAll(staticSpaceObjects);
			spaceObjects.addAll(dynamicSpaceObjects);
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
		for (SpaceObject spaceObject : spaceObjectsThatSupportTick) {
			spaceObject.tick();
		}
	}

}
