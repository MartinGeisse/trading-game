package name.martingeisse.trading_game.game.definition;

import name.martingeisse.trading_game.game.item.FixedInventory;

/**
 *
 */
public interface MiningYieldInfo {

	/**
	 * Determines the mining yield from the amount of rock mined.
	 */
	public FixedInventory determineYield(long minedRockAmount);

}
