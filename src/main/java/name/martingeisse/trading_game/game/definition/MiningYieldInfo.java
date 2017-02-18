package name.martingeisse.trading_game.game.definition;

import name.martingeisse.trading_game.game.item.ImmutableItemStacks;

/**
 *
 */
public interface MiningYieldInfo {

	/**
	 * Determines the mining yield from the amount of rock mined. This function may be called for estimation, therefore
	 * it must not change the state of this info object.
	 */
	public ImmutableItemStacks determineYield(long minedRockAmount);

}
