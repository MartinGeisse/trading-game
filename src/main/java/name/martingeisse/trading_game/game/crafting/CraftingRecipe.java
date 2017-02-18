package name.martingeisse.trading_game.game.crafting;

import name.martingeisse.trading_game.game.item.ImmutableItemStacks;

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
	public ImmutableItemStacks getBillOfMaterials();

	/**
	 * Getter method.
	 *
	 * @return the yield
	 */
	public ImmutableItemStacks getYield();

}
