package name.martingeisse.trading_game.game.item;

import java.io.Serializable;

/**
 * An immutable stack of items that has an item type and stack size.
 */
public final class ImmutableItemStack implements Serializable {

	private final ItemType itemType;
	private final int size;

	public ImmutableItemStack(ItemType itemType, int size) {
		this.itemType = itemType;
		this.size = size;
	}

	/**
	 * Getter method.
	 *
	 * @return the itemType
	 */
	public ItemType getItemType() {
		return itemType;
	}

	/**
	 * Getter method.
	 *
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Calculates the total mass of this stack.
	 *
	 * @return the mass
	 */
	public int getMass() {
		return size * itemType.getMass();
	}

	public ImmutableItemStack scale(double factor) {
		return new ImmutableItemStack(itemType, (int) (size * factor));
	}

}
