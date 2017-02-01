package name.martingeisse.trading_game.game.space.quadtree;

import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.game.space.StaticSpaceObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
final class LeafNode extends Node {

	private final List<StaticSpaceObject> spaceObjects = new ArrayList<>();
	private int numberOfStaticObjects;

	/**
	 * Getter method.
	 *
	 * @return the spaceObjects
	 */
	List<StaticSpaceObject> getSpaceObjects() {
		return spaceObjects;
	}

	void initializeStatic() {
		numberOfStaticObjects = spaceObjects.size();
	}

	@Override
	void select(Box box, Consumer<StaticSpaceObject> consumer) {
		for (SpaceObject spaceObject : spaceObjects) {
			consumer.accept(spaceObject);
		}
	}

	@Override
	int getNumberOfStaticObjects() {
		return numberOfStaticObjects;
	}

}
