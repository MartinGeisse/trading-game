package name.martingeisse.trading_game.game.item;

import name.martingeisse.trading_game.game.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a collection of item stacks. This class is used for players' inventory, containers, NPC, etc. Different subclasses
 * may be used for specific cases.
 */
public class Inventory {

	private final List<ItemStack> itemStacks = new ArrayList<>();

	/**
	 * Getter method.
	 *
	 * @return the itemStacks
	 */
	public List<ItemStack> getItemStacks() {
		return itemStacks;
	}
}
