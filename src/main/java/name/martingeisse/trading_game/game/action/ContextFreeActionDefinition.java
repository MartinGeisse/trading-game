package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.crafting.CraftingRecipe;

import java.util.function.Function;

/**
 * Defines a "context free" action, i.e. an action which a player can perform without specifying any context. For
 * example, many crafting actions performed on the workbench are like this.
 */
public final class ContextFreeActionDefinition {

	private final String name;
	private final Function<Player, PlayerAction> factory;

	public ContextFreeActionDefinition(String name, Function<Player, PlayerAction> factory) {
		this.name = name;
		this.factory = factory;
	}

	public ContextFreeActionDefinition(CraftingRecipe recipe) {
		this.name = CraftingAction.getDefaultText(recipe);
		this.factory = player -> new CraftingAction(player, recipe);
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
	
}
