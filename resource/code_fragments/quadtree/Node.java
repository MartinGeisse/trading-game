package name.martingeisse.trading_game.game.space.quadtree;

import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.function.Consumer;

/**
 *
 */
abstract class Node {

	/**
	 * Selects all space objects within the specified box and passes them to a consumer.
	 *
	 * @param box      the selecting box
	 * @param consumer the consumer
	 */
	abstract void select(Box box, Consumer<SpaceObject> consumer);

	/**
	 * @return the number of static objects within this node
	 */
	abstract int getNumberOfStaticObjects();

	/**
	 *
	 */
	// abstract void generateHeatMap(long relativeTileX, long relativeTileY, long relativeZoom, int outputSizeShift, HeatMapWriter writer, long nodeStartX, long nodeStartY, long nodeRadius);

}
