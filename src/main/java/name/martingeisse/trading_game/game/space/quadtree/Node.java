package name.martingeisse.trading_game.game.space.quadtree;

import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.function.Consumer;

/**
 *
 */
public abstract class Node {

	/**
	 * Selects all space objects within the specified box and passes them to a consumer.
	 *
	 * @param box      the selecting box
	 * @param consumer the consumer
	 */
	public abstract void select(Box box, Consumer<SpaceObject> consumer);

}
