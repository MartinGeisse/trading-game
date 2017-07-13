package name.martingeisse.trading_game.game.manufacturing;

import name.martingeisse.trading_game.game.item.ImmutableItemStacks;

/**
 *
 */
public interface Blueprint {

	public String getName();

	public int getRequiredProgressPoints();

	public ImmutableItemStacks getBillOfMaterials();

	public ImmutableItemStacks getYield();

}
