package name.martingeisse.trading_game.game.item;

/**
 * Item types may provide code for the item behavior (if any) and are referred to by crafting recipes.
 *
 * Each item stack refers to its item type.
 */
public final class ItemType {

	private final String name;
	private final String icon;
	private final int mass;

	public ItemType(String name, String icon, int mass) {
		this.name = name;
		this.icon = icon;
		this.mass = mass;
	}

	/**
	 * Getter method.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method.
	 *
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Getter method.
	 *
	 * @return the mass
	 */
	public int getMass() {
		return mass;
	}

	@Override
	public String toString() {
		return name;
	}

}
