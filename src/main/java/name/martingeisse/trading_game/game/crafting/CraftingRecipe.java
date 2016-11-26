package name.martingeisse.trading_game.game.crafting;

import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.item.ItemType;

/**
 *
 */
public enum CraftingRecipe {

	RED_PIXEL_ASSEMBLY(60, FixedInventory.from(ItemType.RED_PIXEL, 5), ItemType.RED_PIXEL_ASSEMBLY); // TODO 300

	private final int requiredProgressPoints;
	private final FixedInventory billOfMaterials;
	private final FixedInventory products;

	CraftingRecipe(int requiredProgressPoints, FixedInventory billOfMaterials, FixedInventory products) {
		this.requiredProgressPoints = requiredProgressPoints;
		this.billOfMaterials = billOfMaterials;
		this.products = products;
	}

	CraftingRecipe(int requiredProgressPoints, FixedInventory billOfMaterials, ItemType product) {
		this.requiredProgressPoints = requiredProgressPoints;
		this.billOfMaterials = billOfMaterials;
		this.products = FixedInventory.from(product, 1);
	}

	/**
	 * Getter method.
	 *
	 * @return the requiredProgressPoints
	 */
	public int getRequiredProgressPoints() {
		return requiredProgressPoints;
	}

	/**
	 * Getter method.
	 *
	 * @return the billOfMaterials
	 */
	public FixedInventory getBillOfMaterials() {
		return billOfMaterials;
	}

	/**
	 * Getter method.
	 *
	 * @return the products
	 */
	public FixedInventory getProducts() {
		return products;
	}

}
