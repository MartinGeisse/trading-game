package name.martingeisse.trading_game.game.item;

/**
 *
 */
public final class ItemStack {

	private final ItemType itemType;
	private int size;

	public ItemStack(ItemType itemType, int size) {
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
	 * Setter method.
	 *
	 * @param size the size
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
}
