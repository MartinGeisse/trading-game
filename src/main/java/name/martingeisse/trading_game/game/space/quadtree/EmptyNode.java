package name.martingeisse.trading_game.game.space.quadtree;

import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.function.Consumer;

/**
 * This node implementation contains no child nodes and no space objects. In many places, data structures would store
 * null instead of an instance of this class, then return the singleton instance when asked. This reduces the number
 * of places where explicit null handling is needed.
 */
public final class EmptyNode extends Node {

	public static final EmptyNode INSTANCE = new EmptyNode();

	@Override
	public void select(Box box, Consumer<SpaceObject> consumer) {
	}

}
