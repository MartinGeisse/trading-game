package name.martingeisse.trading_game.game.crafting;

import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.item.ItemType;

/**
 *
 */
public final class FixedCraftingRecipe implements CraftingRecipe {

	private final int requiredProgressPoints;
	private final ImmutableItemStacks billOfMaterials;
	private final ImmutableItemStacks yield;

	/**
	 * Constructor.
	 *
	 * @param requiredProgressPoints
	 * @param billOfMaterials
	 * @param yield
	 */
	public FixedCraftingRecipe(int requiredProgressPoints, ImmutableItemStacks billOfMaterials, ImmutableItemStacks yield) {
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
	public FixedCraftingRecipe(int requiredProgressPoints, ImmutableItemStacks billOfMaterials, ItemType yield) {
		this.requiredProgressPoints = requiredProgressPoints;
		this.billOfMaterials = billOfMaterials;
		this.yield = ImmutableItemStacks.from(yield, 1);
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
	public ImmutableItemStacks getBillOfMaterials() {
		return billOfMaterials;
	}

	/**
	 * Getter method.
	 *
	 * @return the yield
	 */
	@Override
	public ImmutableItemStacks getYield() {
		return yield;
	}

}
