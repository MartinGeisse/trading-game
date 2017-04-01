package name.martingeisse.trading_game.game.item;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * An immutable list of immutable item stacks.
 *
 * Among other purposes, this class may be used as a "bill of materials" for crafting recipes IF valid according
 * to {@link #isValidBillOfMaterials()}. This method ensures that no two item stacks with the same item type exist
 * in this object.
 */
public final class ImmutableItemStacks implements Iterable<ImmutableItemStack>, Serializable {

	public static final ImmutableItemStacks EMPTY = new ImmutableItemStacks(ImmutableList.of());

	private final ImmutableList<ImmutableItemStack> stacks;

	public ImmutableItemStacks(ImmutableList<ImmutableItemStack> stacks) {
		this.stacks = stacks;
	}

	public static ImmutableItemStacks fromFixedItemStacks(Collection<ImmutableItemStack> itemStacks) {
		return new ImmutableItemStacks(ImmutableList.copyOf(itemStacks));
	}

	public static ImmutableItemStacks from(ImmutableItemStack stack1) {
		return new ImmutableItemStacks(ImmutableList.of(stack1));
	}

	public static ImmutableItemStacks from(ImmutableItemStack stack1, ImmutableItemStack stack2) {
		return new ImmutableItemStacks(ImmutableList.of(stack1, stack2));
	}

	public static ImmutableItemStacks from(ImmutableItemStack stack1, ImmutableItemStack stack2, ImmutableItemStack stack3) {
		return new ImmutableItemStacks(ImmutableList.of(stack1, stack2, stack3));
	}

	public static ImmutableItemStacks from(ItemType itemType1, int amount1) {
		return from(new ImmutableItemStack(itemType1, amount1));
	}

	public static ImmutableItemStacks from(ItemType itemType1, int amount1, ItemType itemType2, int amount2) {
		return from(new ImmutableItemStack(itemType1, amount1), new ImmutableItemStack(itemType2, amount2));
	}

	public static ImmutableItemStacks from(ItemType itemType1, int amount1, ItemType itemType2, int amount2, ItemType itemType3, int amount3) {
		return from(new ImmutableItemStack(itemType1, amount1), new ImmutableItemStack(itemType2, amount2), new ImmutableItemStack(itemType3, amount3));
	}

	/**
	 * Getter method.
	 *
	 * @return the stacks
	 */
	public ImmutableList<ImmutableItemStack> getStacks() {
		return stacks;
	}

	@Override
	public Iterator<ImmutableItemStack> iterator() {
		return stacks.iterator();
	}

	/**
	 * Calculates the total mass of all stacks.
	 *
	 * @return the mass
	 */
	public int getMass() {
		int result = 0;
		for (ImmutableItemStack itemStack : stacks) {
			result += itemStack.getMass();
		}
		return result;
	}

	public int count(ItemType itemType) {
		int result = 0;
		for (ImmutableItemStack itemStack : stacks) {
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
		for (ImmutableItemStack stack1 : stacks) {
			boolean found = false;
			for (ImmutableItemStack stack2 : stacks) {
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

	public ImmutableItemStacks scale(double factor) {
		List<ImmutableItemStack> newStacks = new ArrayList<>();
		for (ImmutableItemStack oldStack : stacks) {
			newStacks.add(oldStack.scale(factor));
		}
		return new ImmutableItemStacks(ImmutableList.copyOf(newStacks));
	}

	/**
	 * Returns an instance that is a reduced version of this one, with at most the specified mass. Specifically,
	 *
	 * - the returned instance has at most the specified target mass
	 * - the returned instance does not contain items (type or per-type quantity) which this object does not contain
	 * - as few items as possible have been removed, though this is a soft rule since the value of different item types
	 *   cannot be compared
	 */
	public ImmutableItemStacks reduceToMass(int targetMass) {
		if (targetMass < 0) {
			throw new IllegalArgumentException("invalid target mass: " + targetMass);
		}
		// this is very simple, but for now it's ok
		List<ImmutableItemStack> validStacks = new ArrayList<>();
		int validStacksMass = 0;
		for (ImmutableItemStack stack : stacks) {
			int maxStackSize = (targetMass - validStacksMass) / stack.getItemType().getMass();
			if (maxStackSize == 0) {
				continue;
			}
			if (maxStackSize > stack.getSize()) {
				stack = new ImmutableItemStack(stack.getItemType(), maxStackSize);
			}
			validStacks.add(stack);
			validStacksMass += stack.getMass();
		}
		return new ImmutableItemStacks(ImmutableList.copyOf(validStacks));
	}

}
