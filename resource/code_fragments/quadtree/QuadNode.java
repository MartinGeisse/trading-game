package name.martingeisse.trading_game.game.space.quadtree;

import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.function.Consumer;

/**
 * A quad-split node with four children. The split always occurs at the middle along both axes, with the middle being
 * implicit to save memory.
 */
final class QuadNode extends Node {

	private final Node lowXlowY;
	private final Node lowXhighY;
	private final Node highXlowY;
	private final Node highXhighY;
	private int numberOfStaticObjects;

	QuadNode(Node lowXlowY, Node lowXhighY, Node highXlowY, Node highXhighY) {
		this.lowXlowY = lowXlowY;
		this.lowXhighY = lowXhighY;
		this.highXlowY = highXlowY;
		this.highXhighY = highXhighY;
	}

	void initializeStatic() {
		numberOfStaticObjects = 0;
		numberOfStaticObjects += lowXlowY.getNumberOfStaticObjects();
		numberOfStaticObjects += lowXhighY.getNumberOfStaticObjects();
		numberOfStaticObjects += highXlowY.getNumberOfStaticObjects();
		numberOfStaticObjects += highXhighY.getNumberOfStaticObjects();
	}

	/**
	 * Getter method.
	 *
	 * @return the lowXlowY
	 */
	Node getLowXlowY() {
		return lowXlowY;
	}

	/**
	 * Getter method.
	 *
	 * @return the lowXhighY
	 */
	Node getLowXhighY() {
		return lowXhighY;
	}

	/**
	 * Getter method.
	 *
	 * @return the highXlowY
	 */
	Node getHighXlowY() {
		return highXlowY;
	}

	/**
	 * Getter method.
	 *
	 * @return the highXhighY
	 */
	Node getHighXhighY() {
		return highXhighY;
	}

	@Override
	void select(Box box, Consumer<SpaceObject> consumer) {

	}

	@Override
	int getNumberOfStaticObjects() {
		return numberOfStaticObjects;
	}

}
