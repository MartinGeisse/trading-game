package name.martingeisse.trading_game.game.space.quadtree;

import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.function.Consumer;

/**
 * This node implementation contains no child nodes and no space objects. In many places, data structures would store
 * null instead of an instance of this class, then return the singleton instance when asked. This reduces the number
 * of places where explicit null handling is needed.
 */
final class EmptyNode extends Node {

	static final EmptyNode INSTANCE = new EmptyNode();

	@Override
	void select(Box box, Consumer<SpaceObject> consumer) {
	}

	@Override
	int getNumberOfStaticObjects() {
		return 0;
	}

}
