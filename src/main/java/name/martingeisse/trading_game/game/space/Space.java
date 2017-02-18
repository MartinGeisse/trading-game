package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * Keeps track of all the game's {@link SpaceObject}s.
 */
public final class Space {

	private final List<StaticSpaceObject> staticSpaceObjects = new ArrayList<>();
	private ImmutableList<StaticSpaceObject> staticSpaceObjectsImmutable = null;
	private final List<DynamicSpaceObject> dynamicSpaceObjects = new ArrayList<>();
	private ImmutableList<DynamicSpaceObject> dynamicSpaceObjectsImmutable = null;
	private ImmutableList<SpaceObject> spaceObjectsImmutable = null;
	private final Map<Long, SpaceObject> spaceObjectsById = new HashMap<>();
	private long idCounter = 0;
	private final List<SpaceObject> spaceObjectsThatSupportTick = new ArrayList<>();

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
		addInternal(spaceObject);
		staticSpaceObjects.add(spaceObject);
		staticSpaceObjectsImmutable = null;
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
	 * Finds a space object by position and matching radius. If multiple objects match then the nearest one is returned.
	 * Returns null if no object lies within the radius.
	 */
	public SpaceObject get(long x, long y, long radius, Comparator<SpaceObject> priorityComparator) {
		SpaceObject matchingObject = null;
		long matchingSquaredDistance = Long.MAX_VALUE;
		long squaredRadius = radius * radius;
		for (SpaceObject spaceObject : spaceObjectsById.values()) {
			long dx = spaceObject.getX() - x;
			if (dx > radius || dx < -radius) {
				continue;
			}
			long dy = spaceObject.getY() - y;
			if (dy > radius || dy < -radius) {
				continue;
			}
			long squaredDistance = dx * dx + dy * dy;
			if (squaredDistance > squaredRadius) {
				continue;
			}
			int priorityOrder = priorityComparator.compare(spaceObject, matchingObject);
			boolean replaceMatching;
			if (priorityOrder > 0) {
				replaceMatching = true;
			} else if (priorityOrder < 0) {
				replaceMatching = false;
			} else {
				replaceMatching = squaredDistance < matchingSquaredDistance;
			}
			if (replaceMatching) {
				matchingSquaredDistance = squaredDistance;
				matchingObject = spaceObject;
			}
		}
		return matchingObject;
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
