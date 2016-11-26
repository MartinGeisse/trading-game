package name.martingeisse.trading_game.game.crafting;

import name.martingeisse.trading_game.game.item.FixedInventory;

/**
 *
 */
public interface CraftingRecipe {

	/**
	 * Getter method.
	 *
	 * @return the requiredProgressPoints
	 */
	public int getRequiredProgressPoints();

	/**
	 * Getter method.
	 *
	 * @return the billOfMaterials
	 */
	public FixedInventory getBillOfMaterials();

	/**
	 * Getter method.
	 *
	 * @return the products
	 */
	public FixedInventory getProducts();

}
