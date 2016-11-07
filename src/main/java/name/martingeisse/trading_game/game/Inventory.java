package name.martingeisse.trading_game.game;

import name.martingeisse.trading_game.game.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a collection of items. This class is used for players' inventory, containers, NPC, etc. Different subclasses
 * may be used for specific cases.
 */
public class Inventory {

	private final List<Item> items = new ArrayList<>();

	/**
	 * Getter method.
	 *
	 * @return the items
	 */
	public List<Item> getItems() {
		return items;
	}

}
