package name.martingeisse.trading_game.game.definition;

import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.action.ContextFreeActionDefinition;
import name.martingeisse.trading_game.game.action.CraftingAction;
import name.martingeisse.trading_game.game.action.CreateRedPixelAction;
import name.martingeisse.trading_game.game.crafting.CraftingRecipe;
import name.martingeisse.trading_game.game.crafting.FixedCraftingRecipe;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.ItemType;

/**
 * This object defines how the game works on a game-logic level. It is injected into all parts that need such static
 * definitions.
 */
@Singleton
public final class GameDefinition {

	private final CraftingRecipe redPixelAssemblyCraftingRecipe;
	private final ImmutableList<CraftingRecipe> craftingRecipes;
	private final ImmutableList<ContextFreeActionDefinition> contextFreeActionDefinitions;

	/**
	 *
	 */
	public GameDefinition() {
		redPixelAssemblyCraftingRecipe = new FixedCraftingRecipe(60, FixedInventory.from(ItemType.RED_PIXEL, 5), ItemType.RED_PIXEL_ASSEMBLY); // TODO 300
		craftingRecipes = ImmutableList.of(redPixelAssemblyCraftingRecipe);
		contextFreeActionDefinitions = ImmutableList.of(
			new ContextFreeActionDefinition("Create red pixel", CreateRedPixelAction::new),
			new ContextFreeActionDefinition("Create red pixel assembly", p -> new CraftingAction(p, getRedPixelAssemblyCraftingRecipe()))
		);
	}

	/**
	 * Getter method.
	 *
	 * @return the craftingRecipes
	 */
	public ImmutableList<CraftingRecipe> getCraftingRecipes() {
		return craftingRecipes;
	}

	// TODO remove
	public CraftingRecipe getRedPixelAssemblyCraftingRecipe() {
		return redPixelAssemblyCraftingRecipe;
	}

	/**
	 * Getter method.
	 *
	 * @return the contextFreeActionDefinitions
	 */
	public ImmutableList<ContextFreeActionDefinition> getContextFreeActionDefinitions() {
		return contextFreeActionDefinitions;
	}

}
