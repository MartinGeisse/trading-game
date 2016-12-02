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
	private final String customName;

	public CraftingAction(Player player, CraftingRecipe recipe) {
		this(player, recipe, null);
	}

	public CraftingAction(Player player, CraftingRecipe recipe, String customName) {
		super(player, recipe.getRequiredProgressPoints());
		this.recipe = recipe;
		this.customName = customName;
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
		getPlayer().getInventory().add(recipe.getYield());
	}

	@Override
	public String toString() {
		return (customName == null ? getDefaultText(recipe) : customName);
	}

	public static String getDefaultText(CraftingRecipe recipe) {
		FixedInventory products = recipe.getYield();
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
