package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.crafting.CraftingRecipe;
import name.martingeisse.trading_game.game.item.FixedInventory;

import java.util.function.Function;

/**
 * Defines a "context free" action, i.e. an action which a player can perform without specifying any context. For
 * example, many crafting actions performed on the workbench are like this.
 *
 * The billOfMaterials field is used for display purposes. In the future, it might be used to deactivate the action
 * in the UI. In the far future, this may be turned into a more general "precondition" mechanism that has a
 * predicate function and selects a UI panel for explain itself (or introduce a separate mapper for that, so the
 * game logic isn't tied to the UI).
 */
public final class ContextFreeActionDefinition {

	private final String name;
	private final Function<Player, PlayerAction> factory;
	private final FixedInventory billOfMaterials;

	public ContextFreeActionDefinition(String name, Function<Player, PlayerAction> factory) {
		this.name = name;
		this.factory = factory;
		this.billOfMaterials = null;
	}

	public ContextFreeActionDefinition(String name, Function<Player, PlayerAction> factory, FixedInventory billOfMaterials) {
		this.name = name;
		this.factory = factory;
		this.billOfMaterials = billOfMaterials;
	}

	public ContextFreeActionDefinition(CraftingRecipe recipe) {
		this.name = CraftingAction.getDefaultText(recipe);
		this.factory = player -> new CraftingAction(player, recipe);
		this.billOfMaterials = recipe.getBillOfMaterials();
	}

	public ContextFreeActionDefinition(String name, CraftingRecipe recipe) {
		this.name = name;
		this.factory = player -> new CraftingAction(player, recipe, name);
		this.billOfMaterials = recipe.getBillOfMaterials();
	}

	/**
	 * Getter method.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method.
	 *
	 * @return the factory
	 */
	public Function<Player, PlayerAction> getFactory() {
		return factory;
	}

	/**
	 * Getter method.
	 *
	 * @return the billOfMaterials
	 */
	public FixedInventory getBillOfMaterials() {
		return billOfMaterials;
	}

}
