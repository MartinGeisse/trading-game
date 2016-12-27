package name.martingeisse.trading_game.game.action.actions;

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
	protected Integer getRemainingTime() {
		return null; // TODO check remaining ores in the asteroid; remaining cargo space in the player's ship
	}

	@Override
	public boolean isFinishable() {
		return false; // TODO check remaining ores in the asteroid; remaining cargo space in the player's ship
	}

	@Override
	public void tick() {
		// TODO mine
	}

}
