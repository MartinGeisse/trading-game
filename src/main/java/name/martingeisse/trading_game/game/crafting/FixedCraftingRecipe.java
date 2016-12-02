package name.martingeisse.trading_game.game.crafting;

import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.ItemType;

/**
 *
 */
public final class FixedCraftingRecipe implements CraftingRecipe {

	private final int requiredProgressPoints;
	private final FixedInventory billOfMaterials;
	private final FixedInventory yield;

	/**
	 * Constructor.
	 *
	 * @param requiredProgressPoints
	 * @param billOfMaterials
	 * @param yield
	 */
	public FixedCraftingRecipe(int requiredProgressPoints, FixedInventory billOfMaterials, FixedInventory yield) {
		this.requiredProgressPoints = requiredProgressPoints;
		this.billOfMaterials = billOfMaterials;
		this.yield = yield;
	}

	/**
	 * Constructor.
	 *
	 * @param requiredProgressPoints
	 * @param billOfMaterials
	 * @param yield
	 */
	public FixedCraftingRecipe(int requiredProgressPoints, FixedInventory billOfMaterials, ItemType yield) {
		this.requiredProgressPoints = requiredProgressPoints;
		this.billOfMaterials = billOfMaterials;
		this.yield = FixedInventory.from(yield, 1);
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
	 * @return the yield
	 */
	@Override
	public FixedInventory getYield() {
		return yield;
	}

}
