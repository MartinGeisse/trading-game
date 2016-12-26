package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.actions.CraftingAction;
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
 *
 * TODO remove. In the future, all actions are selected from a context.
 */
public final class ContextFreeActionDefinition {

	private final String name;
	private final Function<Player, Action> factory;
	private final FixedInventory billOfMaterials;
	private final FixedInventory yield;

	public ContextFreeActionDefinition(String name, Function<Player, Action> factory) {
		this.name = name;
		this.factory = factory;
		this.billOfMaterials = null;
		this.yield = null;
	}

	public ContextFreeActionDefinition(String name, Function<Player, Action> factory, FixedInventory billOfMaterials, FixedInventory yield) {
		this.name = name;
		this.factory = factory;
		this.billOfMaterials = billOfMaterials;
		this.yield = yield;
	}

	public ContextFreeActionDefinition(CraftingRecipe recipe) {
		this.name = CraftingAction.getDefaultText(recipe);
		this.factory = player -> new CraftingAction(player, recipe);
		this.billOfMaterials = recipe.getBillOfMaterials();
		this.yield = recipe.getYield();
	}

	public ContextFreeActionDefinition(String name, CraftingRecipe recipe) {
		this.name = name;
		this.factory = player -> new CraftingAction(player, recipe, name);
		this.billOfMaterials = recipe.getBillOfMaterials();
		this.yield = recipe.getYield();
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
	public Function<Player, Action> getFactory() {
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

	/**
	 * Getter method.
	 *
	 * @return the yield
	 */
	public FixedInventory getYield() {
		return yield;
	}

	@Override
	public String toString() {
		return name;
	}

}
