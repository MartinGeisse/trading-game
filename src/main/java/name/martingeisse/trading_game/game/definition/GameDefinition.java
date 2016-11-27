package name.martingeisse.trading_game.game.definition;

import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.action.ContextFreeActionDefinition;
import name.martingeisse.trading_game.game.action.CraftingAction;
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

	private final ImmutableList<ContextFreeActionDefinition> contextFreeActionDefinitions;

	/**
	 *
	 */
	public GameDefinition() {

		ItemType redPixelItemType = new ItemType("red pixel", "red_pixel.png");
		ItemType redPixelAssemblyItemType = new ItemType("red pixel assembly", "red_pixel_assembly.png");

		CraftingRecipe redPixelCraftingRecipe = new FixedCraftingRecipe(100, FixedInventory.EMPTY, redPixelItemType);
		CraftingRecipe redPixelAssemblyCraftingRecipe = new FixedCraftingRecipe(60, FixedInventory.from(redPixelItemType, 5), redPixelAssemblyItemType); // TODO 300

		contextFreeActionDefinitions = ImmutableList.of(
			new ContextFreeActionDefinition("Create red pixel", p -> new CraftingAction(p, redPixelCraftingRecipe)),
			new ContextFreeActionDefinition("Create red pixel assembly", p -> new CraftingAction(p, redPixelAssemblyCraftingRecipe))
		);

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
