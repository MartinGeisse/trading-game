package name.martingeisse.trading_game.game.space.quadtree;

import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.function.Consumer;

/**
 * Note: The QT will be built around static objects (and decide its number of nodes based on that), then dynamically
 * move (dynamic) objects around as needed without changing the node structure. This is sufficient for now since
 * the game mechanics will likely cause dynamic objects to spread roughly the same way as static objects.
 *
 * Object numbers are not needed at inner nodes. These numbers are only relevant for density maps (far zoomed out
 * tiles), which should only contain static objects and can be statically rendered once, then stored.
 *
 * TODO consider using a special grid node for the first N levels to reduce those 4^N nodes to a single array with
 * 4^N elements, many of which would contain null (representing EmptyNode). Maybe even a more optimized structure.
 * This requires, however, to use an explicit node for each and every grid cell that does contain objects unless that
 * structure can handle super-cells. Explicit nodes are still needed to contain lists of space objects. REQUIRES
 * SOME MEASURING FIRST!
 */
public final class Quadtree {

	private Node root;

	/**
	 * Constructor
	 *
	 * @param radius the "radius" of the universe, centered at the origin. If this isn't the actual radius then
	 *               the quadtree will be out of balance.
	 * @param depth TODO
	 */
	public Quadtree(long radius, int depth) {
		// TODO
	}

	/**
	 * Selects all space objects within the specified box and passes them to a consumer.
	 *
	 * @param box      the selecting box
	 * @param consumer the consumer
	 */
	public void select(Box box, Consumer<SpaceObject> consumer) {
		root.select(box, consumer);
	}

}
