package name.martingeisse.trading_game.game.space.quadtree;

import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public final class LeafNode extends Node {

	private final List<SpaceObject> spaceObjects = new ArrayList<>();

	/**
	 * Getter method.
	 *
	 * @return the spaceObjects
	 */
	public List<SpaceObject> getSpaceObjects() {
		return spaceObjects;
	}

	@Override
	public void select(Box box, Consumer<SpaceObject> consumer) {
		for (SpaceObject spaceObject : spaceObjects) {
			consumer.accept(spaceObject);
		}
	}

}
