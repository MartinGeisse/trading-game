package name.martingeisse.trading_game.game.item;

/**
 * Similar to an {@link ItemStack}, but with a fixed size (and thus totally immutable).
 */
public final class FixedItemStack {

	private final ItemType itemType;
	private final int size;

	public FixedItemStack(ItemType itemType, int size) {
		this.itemType = itemType;
		this.size = size;
	}

	public static FixedItemStack fromItemStack(ItemStack itemStack) {
		return new FixedItemStack(itemStack.getItemType(), itemStack.getSize());
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

	public FixedItemStack scale(double factor) {
		return new FixedItemStack(itemType, (int) (size * factor));
	}

}
