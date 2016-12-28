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
		// TODO check remaining cargo space in the player's ship
		// TODO yield capacity vs. time -> not the same, otherwise upgrades cannot improve mining speed
		return (int)asteroid.getYieldCapacity();
	}

	@Override
	public final boolean isFinishable() {
		// TODO check remaining cargo space in the player's ship
		return asteroid.getYieldCapacity() == 0;
	}

	@Override
	public final void tick() {
		FixedInventory determinedYield = asteroid.obtainYieldForTick();
		if (determinedYield != null) {
			// TODO reduced by what is left as well as inventory space; put back the rest
			FixedInventory actualYield = determinedYield;
			inventory.add(actualYield);
		}
	}

	@Override
	public String toString() {
		return "mine ores from " + asteroid.getName();
	}

}
