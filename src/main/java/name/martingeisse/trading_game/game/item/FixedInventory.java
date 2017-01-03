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

	public static final FixedInventory EMPTY = new FixedInventory(ImmutableList.of());

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

	public static FixedInventory from(FixedItemStack stack1) {
		return new FixedInventory(ImmutableList.of(stack1));
	}

	public static FixedInventory from(FixedItemStack stack1, FixedItemStack stack2) {
		return new FixedInventory(ImmutableList.of(stack1, stack2));
	}

	public static FixedInventory from(FixedItemStack stack1, FixedItemStack stack2, FixedItemStack stack3) {
		return new FixedInventory(ImmutableList.of(stack1, stack2, stack3));
	}

	public static FixedInventory from(ItemType itemType1, int amount1) {
		return from(new FixedItemStack(itemType1, amount1));
	}

	public static FixedInventory from(ItemType itemType1, int amount1, ItemType itemType2, int amount2) {
		return from(new FixedItemStack(itemType1, amount1), new FixedItemStack(itemType2, amount2));
	}

	public static FixedInventory from(ItemType itemType1, int amount1, ItemType itemType2, int amount2, ItemType itemType3, int amount3) {
		return from(new FixedItemStack(itemType1, amount1), new FixedItemStack(itemType2, amount2), new FixedItemStack(itemType3, amount3));
	}

	/**
	 * Getter method.
	 *
	 * @return the itemStacks
	 */
	public ImmutableList<FixedItemStack> getItemStacks() {
		return itemStacks;
	}

	/**
	 * Calculates the total mass of this inventory.
	 *
	 * @return the mass
	 */
	public int getMass() {
		int result = 0;
		for (FixedItemStack itemStack : itemStacks) {
			result += itemStack.getMass();
		}
		return result;
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

	/**
	 * Checks whether this object is valid for use as a bill of materials.
	 * <p>
	 * This ensures that no two stacks in this object have the same item type.
	 */
	public boolean isValidBillOfMaterials() {
		for (FixedItemStack stack1 : itemStacks) {
			boolean found = false;
			for (FixedItemStack stack2 : itemStacks) {
				if (!found) {
					if (stack1 == stack2) {
						found = true;
					}
				} else {
					if (stack1.getItemType() == stack2.getItemType()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public FixedInventory scale(double factor) {
		List<FixedItemStack> newStacks = new ArrayList<>();
		for (FixedItemStack oldStack : itemStacks) {
			newStacks.add(oldStack.scale(factor));
		}
		return new FixedInventory(ImmutableList.copyOf(newStacks));
	}

	public FixedInventory reduceToMass(int mass) {
		return this; // TODO
	}

}
