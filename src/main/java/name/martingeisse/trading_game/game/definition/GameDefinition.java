package name.martingeisse.trading_game.game.definition;

import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import jdk.nashorn.internal.ir.annotations.Immutable;
import name.martingeisse.trading_game.game.action.ContextFreeActionDefinition;
import name.martingeisse.trading_game.game.action.CraftingAction;
import name.martingeisse.trading_game.game.crafting.CraftingRecipe;
import name.martingeisse.trading_game.game.crafting.FixedCraftingRecipe;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.FixedItemStack;
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
		ItemType redPixelLineItemType = new ItemType("red pixel line", "no_icon.png");
		ItemType redPixelGlueItemType = new ItemType("red pixel glue", "no_icon.png");

		ItemType pixelAxeItemType = new ItemType("pixel axe", "no_icon.png");
		ItemType pixelHoeItemType = new ItemType("pixel hoe", "no_icon.png");
		ItemType pixelHammerItemType = new ItemType("pixel hammer", "no_icon.png");
		ItemType pixelPickaxeItemType = new ItemType("pixel pickaxe", "no_icon.png");

		CraftingRecipe redPixelCraftingRecipe = new FixedCraftingRecipe(100, FixedInventory.EMPTY, redPixelItemType);
		CraftingRecipe redPixelAssemblyCraftingRecipe = new FixedCraftingRecipe(300, FixedInventory.from(redPixelItemType, 5), redPixelAssemblyItemType);
		CraftingRecipe redPixelLineCraftingRecipe = new FixedCraftingRecipe(300, FixedInventory.from(redPixelItemType, 10), redPixelLineItemType);
		CraftingRecipe redPixelGlueCraftingRecipe = new FixedCraftingRecipe(50, FixedInventory.from(redPixelItemType, 5), redPixelGlueItemType);

		FixedInventory pixelToolBillOfMaterials = new FixedInventory(ImmutableList.of(
			new FixedItemStack(redPixelLineItemType, 3),
			new FixedItemStack(redPixelAssemblyItemType, 5),
			new FixedItemStack(redPixelItemType, 10),
			new FixedItemStack(redPixelGlueItemType, 1)
		));
		CraftingRecipe pixelAxeCraftingRecipe = new FixedCraftingRecipe(1000, pixelToolBillOfMaterials, pixelAxeItemType);
		CraftingRecipe pixelHoeCraftingRecipe = new FixedCraftingRecipe(1000, pixelToolBillOfMaterials, pixelHoeItemType);
		CraftingRecipe pixelHammerCraftingRecipe = new FixedCraftingRecipe(1000, pixelToolBillOfMaterials, pixelHammerItemType);
		CraftingRecipe pixelPickaxeCraftingRecipe = new FixedCraftingRecipe(1000, pixelToolBillOfMaterials, pixelPickaxeItemType);

		contextFreeActionDefinitions = ImmutableList.of(
			new ContextFreeActionDefinition(redPixelCraftingRecipe),
			new ContextFreeActionDefinition(redPixelAssemblyCraftingRecipe),
			new ContextFreeActionDefinition(redPixelLineCraftingRecipe),
			new ContextFreeActionDefinition(redPixelGlueCraftingRecipe),

			new ContextFreeActionDefinition(pixelAxeCraftingRecipe),
			new ContextFreeActionDefinition(pixelHoeCraftingRecipe),
			new ContextFreeActionDefinition(pixelHammerCraftingRecipe),
			new ContextFreeActionDefinition(pixelPickaxeCraftingRecipe)
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
