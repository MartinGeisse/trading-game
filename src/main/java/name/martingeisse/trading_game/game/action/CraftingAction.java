package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.crafting.CraftingRecipe;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.FixedItemStack;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;

/**
 *
 */
public class CraftingAction extends PlayerAction {

	private final CraftingRecipe recipe;

	public CraftingAction(Player player, CraftingRecipe recipe) {
		super(player, recipe.getRequiredProgressPoints());
		this.recipe = recipe;
	}

	@Override
	public boolean onStart() {
		try {
			getPlayer().reserveActionItems(recipe.getBillOfMaterials());
			return true;
		} catch (NotEnoughItemsException e) {
			return false;
		}
	}

	@Override
	public void onCancel() {
		getPlayer().putBackActionItems();
	}

	@Override
	public void onFinish() {
		getPlayer().consumeActionItems();
		getPlayer().getInventory().add(recipe.getProducts());
	}

	@Override
	public String toString() {
		return getDefaultText(recipe);
	}

	public static String getDefaultText(CraftingRecipe recipe) {
		FixedInventory products = recipe.getProducts();
		if (products.getItemStacks().size() == 1) {
			FixedItemStack stack = products.getItemStacks().get(0);
			if (stack.getSize() == 1) {
				return "craft " + stack.getItemType();
			} else {
				return "craft " + stack.getSize() + ' ' + stack.getItemType() + 's';
			}
		} else {
			return recipe.toString();
		}
	}

}
