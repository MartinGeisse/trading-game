package name.martingeisse.trading_game.game.item;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Similar to an {@link Inventory}, but contains a fixed list of {@link FixedItemStack}s and is thus totally immutable.
 * Besides an inventory that is not meant to be changed, this class may also be used as a "bill of materials" for
 * crafting recipes.
 */
public final class FixedInventory {

	private final ImmutableList<FixedItemStack> itemStacks;

	public FixedInventory(ImmutableList<FixedItemStack> itemStacks) {
		this.itemStacks = itemStacks;
	}

	public static FixedInventory fromInventory(Inventory inventory) {
		return fromItemStacks(inventory.getItemStacks());
	}

	public static FixedInventory fromItemStacks(Collection<ItemStack> itemStacks) {
		List<FixedItemStack> fixedItemStacks = new ArrayList<>();
		for (ItemStack itemStack : itemStacks) {
			fixedItemStacks.add(FixedItemStack.fromItemStack(itemStack));
		}
		return fromFixedItemStacks(fixedItemStacks);

	}

	public static FixedInventory fromFixedItemStacks(Collection<FixedItemStack> itemStacks) {
		return new FixedInventory(ImmutableList.copyOf(itemStacks));
	}

	/**
	 * Getter method.
	 *
	 * @return the itemStacks
	 */
	public ImmutableList<FixedItemStack> getItemStacks() {
		return itemStacks;
	}

	public int count(ItemType itemType) {
		int result = 0;
		for (FixedItemStack itemStack : itemStacks) {
			if (itemStack.getItemType() == itemType) {
				result += itemStack.getSize();
			}
		}
		return result;
	}

}
