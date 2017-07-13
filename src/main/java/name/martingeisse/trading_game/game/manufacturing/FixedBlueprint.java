package name.martingeisse.trading_game.game.manufacturing;

import name.martingeisse.trading_game.game.item.ImmutableItemStacks;
import name.martingeisse.trading_game.game.item.ItemType;

/**
 *
 */
public final class FixedBlueprint implements Blueprint {

	private final String name;
	private final int requiredProgressPoints;
	private final ImmutableItemStacks billOfMaterials;
	private final ImmutableItemStacks yield;

	public FixedBlueprint(String name, int requiredProgressPoints, ImmutableItemStacks billOfMaterials, ImmutableItemStacks yield) {
		this.name = name;
		this.requiredProgressPoints = requiredProgressPoints;
		this.billOfMaterials = billOfMaterials;
		this.yield = yield;
	}

	public FixedBlueprint(String name, int requiredProgressPoints, ImmutableItemStacks billOfMaterials, ItemType yield) {
		this.name = name;
		this.requiredProgressPoints = requiredProgressPoints;
		this.billOfMaterials = billOfMaterials;
		this.yield = ImmutableItemStacks.from(yield, 1);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getRequiredProgressPoints() {
		return requiredProgressPoints;
	}

	@Override
	public ImmutableItemStacks getBillOfMaterials() {
		return billOfMaterials;
	}

	@Override
	public ImmutableItemStacks getYield() {
		return yield;
	}

}
