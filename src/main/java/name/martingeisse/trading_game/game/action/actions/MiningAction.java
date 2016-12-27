package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.space.Asteroid;

/**
 *
 */
public final class MiningAction extends ContinuousAction {

	private final Asteroid asteroid;
	private final Inventory inventory;

	public MiningAction(Asteroid asteroid, Inventory inventory) {
		this.asteroid = asteroid;
		this.inventory = inventory;
	}

	@Override
	protected final Integer getRemainingTime() {
		return null; // TODO check remaining ores in the asteroid; remaining cargo space in the player's ship
	}

	@Override
	public final boolean isFinishable() {
		return false; // TODO check remaining ores in the asteroid; remaining cargo space in the player's ship
	}

	@Override
	public final void tick() {
		FixedInventory determinedYield = asteroid.determineYieldForTick();
		FixedInventory actualYield = determinedYield; // reduced by what is left as well as inventory space
		inventory.add(actualYield);
	}

	@Override
	public String toString() {
		return "mine ores from " + asteroid.getName();
	}

}
