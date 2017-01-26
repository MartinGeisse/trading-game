package name.martingeisse.trading_game.game.space.quadtree;

import name.martingeisse.trading_game.game.space.Space;
import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Note: The QT will be built around static objects (and decide its number of nodes based on that), then dynamically
 * move (dynamic) objects around as needed without changing the node structure. This is sufficient for now since
 * the game mechanics will likely cause dynamic objects to spread roughly the same way as static objects.
 * <p>
 * Object numbers are not needed at inner nodes. These numbers are only relevant for density maps (far zoomed out
 * tiles), which should only contain static objects and can be statically rendered once, then stored.
 * <p>
 * TODO consider using a special grid node for the first N levels to reduce those 4^N nodes to a single array with
 * 4^N elements, many of which would contain null (representing EmptyNode). Maybe even a more optimized structure.
 * This requires, however, to use an explicit node for each and every grid cell that does contain objects unless that
 * structure can handle super-cells. Explicit nodes are still needed to contain lists of space objects. REQUIRES
 * SOME MEASURING FIRST!
 */
public final class Quadtree {

	private final long radius;
	private Node root;

	/**
	 * Constructor
	 */
	public Quadtree(Space space, int maximumNumberOfLeafObjects) {
		this.radius = computeRadius(space.getSpaceObjects());
		this.root = buildNodes(space.getSpaceObjects(), 0, 0, radius, maximumNumberOfLeafObjects);
	}

	private static long computeRadius(List<SpaceObject> spaceObjects) {
		long radius = 0;
		for (SpaceObject spaceObject : spaceObjects) {
			long x = spaceObject.getX();
			long y = spaceObject.getY();
			if (x > radius) {
				radius = x;
			} else if (x < -radius) {
				radius = -x;
			}
			if (y > radius) {
				radius = y;
			} else if (y < -radius) {
				radius = -y;
			}
		}
		return radius;
	}

	private static Node buildNodes(List<SpaceObject> spaceObjects, long splitX, long splitY, long radius, int maximumNumberOfLeafObjects) {
		if (spaceObjects.size() <= maximumNumberOfLeafObjects) {
			LeafNode node = new LeafNode();
			node.getSpaceObjects().addAll(spaceObjects);
			node.initializeStatic();
			return node;
		} else {
			List<SpaceObject> lowXlowYList = new ArrayList<>();
			List<SpaceObject> lowXhighYList = new ArrayList<>();
			List<SpaceObject> highXlowYList = new ArrayList<>();
			List<SpaceObject> highXhighYList = new ArrayList<>();
			for (SpaceObject spaceObject : spaceObjects) {
				if (spaceObject.getX() < splitX) {
					if (spaceObject.getY() < splitY) {
						lowXlowYList.add(spaceObject);
					} else {
						lowXhighYList.add(spaceObject);
					}
				} else {
					if (spaceObject.getY() < splitY) {
						highXlowYList.add(spaceObject);
					} else {
						highXhighYList.add(spaceObject);
					}
				}
			}
			long halfRadius = radius >> 1;
			Node lowXlowYNode = buildNodes(lowXlowYList, splitX - halfRadius, splitY - halfRadius, halfRadius, maximumNumberOfLeafObjects);
			Node lowXhighYNode = buildNodes(lowXhighYList, splitX - halfRadius, splitY + halfRadius, halfRadius, maximumNumberOfLeafObjects);
			Node highXlowYNode = buildNodes(highXlowYList, splitX + halfRadius, splitY - halfRadius, halfRadius, maximumNumberOfLeafObjects);
			Node highXhighYNode = buildNodes(highXhighYList, splitX + halfRadius, splitY + halfRadius, halfRadius, maximumNumberOfLeafObjects);
			QuadNode node = new QuadNode(lowXlowYNode, lowXhighYNode, highXlowYNode, highXhighYNode);
			node.initializeStatic();
			return node;
		}
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

	/**
	 * Generates a heat map for the input tile (tileX, tileY) at the specified zoom level and of output size
	 * (outputSize * outputSize), with outputSize = 2^outputSizeShift. Heat map pixels are output using the heat map
	 * writer.
	 */
	public void generateHeatMap(long tileX, long tileY, long zoom, int outputSizeShift, HeatMapWriter writer) {
		// root.generateHeatMap(tileX, tileY, zoom, outputSizeShift, writer);
	}

}
