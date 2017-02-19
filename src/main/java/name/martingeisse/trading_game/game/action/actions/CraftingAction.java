package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.crafting.CraftingRecipe;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.item.ImmutableItemStack;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;

/**
 * An action backed by a {@link CraftingRecipe}.
 */
public final class CraftingAction extends FixedEffortAction {

	private final Player player;
	private final CraftingRecipe recipe;
	private final String customName;

	public CraftingAction(Player player, CraftingRecipe recipe) {
		this(player, recipe, null);
	}

	public CraftingAction(Player player, CraftingRecipe recipe, String customName) {
		this.player = player;
		this.recipe = recipe;
		this.customName = customName;
	}

	@Override
	public Action getPrerequisite() {
		return null;
	}

	@Override
	public int getTotalRequiredProgressPoints() {
		return recipe.getRequiredProgressPoints();
	}

	@Override
	public AbstractExecution startExecution() {
		try {
			player.getInventory().removeBillOfMaterials(recipe.getBillOfMaterials());
			return new Execution();
		} catch (NotEnoughItemsException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return (customName == null ? getDefaultText(recipe) : customName);
	}

	/**
	 * Builds the default name for a crafting action with the specified recipe.
	 */
	public static String getDefaultText(CraftingRecipe recipe) {
		ImmutableItemStacks products = recipe.getYield();
		if (products.getStacks().size() == 1) {
			ImmutableItemStack stack = products.getStacks().get(0);
			if (stack.getSize() == 1) {
				return "craft " + stack.getItemType();
			} else {
				return "craft " + stack.getSize() + ' ' + stack.getItemType() + 's';
			}
		} else {
			return recipe.toString();
		}
	}

	public final class Execution extends AbstractExecution {

		@Override
		public String getName() {
			return CraftingAction.this.toString();
		}

		@Override
		public void cancel() {
			player.getInventory().add(recipe.getBillOfMaterials());
		}

		@Override
		protected void onFinish() {
			player.getInventory().add(recipe.getYield());
		}

	}

}
