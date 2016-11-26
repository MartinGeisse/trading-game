package name.martingeisse.trading_game.game.definition;

import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.crafting.CraftingRecipe;

import java.util.List;

/**
 * This object defines how the game works on a game-logic level. It is injected into all parts that need such static
 * definitions.
 */
@Singleton
public final class GameDefinition {

	private final ImmutableList<CraftingRecipe> craftingRecipes;

	/**
	 *
	 */
	public GameDefinition() {
		craftingRecipes = ImmutableList.copyOf(CraftingRecipe.values());
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
		return CraftingRecipe.RED_PIXEL_ASSEMBLY;
	}

}
