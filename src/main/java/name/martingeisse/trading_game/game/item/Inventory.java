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

	public ItemStack find(ItemType itemType) {
		for (ItemStack itemStack : itemStacks) {
			if (itemStack.getItemType() == itemType) {
				return itemStack;
			}
		}
		return null;
	}

	public int count(ItemType itemType) {
		int result = 0;
		for (ItemStack itemStack : itemStacks) {
			if (itemStack.getItemType() == itemType) {
				result += itemStack.getSize();
			}
		}
		return result;
	}

	public Inventory add(ItemType itemType) {
		add(itemType, 1);
		return this;
	}

	public Inventory add(FixedInventory items) {
		for (FixedItemStack stack : items.getItemStacks()) {
			add(stack);
		}
		return this;
	}

	public Inventory add(FixedItemStack stack) {
		return add(stack.getItemType(), stack.getSize());
	}

	public Inventory add(ItemType itemType, int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount is negative");
		}
		if (amount == 0) {
			return this;
		}
		ItemStack itemStack = find(itemType);
		if (itemStack == null) {
			itemStack = new ItemStack(itemType, amount);
			itemStacks.add(itemStack);
		} else {
			itemStack.add(amount);
		}
		return this;
	}

	public Inventory remove(ItemType itemType) throws NotEnoughItemsException {
		remove(itemType, 1);
		return this;
	}

	public Inventory remove(ItemType itemType, int amount) throws NotEnoughItemsException {

		// first check if there are enough items, so we don't mess up the item stacks if not
		if (count(itemType) < amount) {
			throw new NotEnoughItemsException();
		}

		// there are enough items, so remove stacks (and possibly one partial stack) until the amount is reached
		while (amount > 0) {
			ItemStack itemStack = find(itemType);
			if (itemStack.getSize() > amount) {
				itemStack.remove(amount);
				return this;
			} else if (itemStack.getSize() == amount) {
				itemStacks.remove(itemStack);
				return this;
			} else {
				itemStacks.remove(itemStack);
				amount -= itemStack.getSize();
			}
		}

		return this;
	}

}
