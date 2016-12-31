package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.definition.GameConstants;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.space.Asteroid;
import name.martingeisse.trading_game.game.space.GeometryUtil;
import name.martingeisse.trading_game.game.space.PlayerShip;

/**
 *
 */
public final class MiningAction extends ContinuousAction {

	private final Asteroid asteroid;
	private final Player player;
	private final long miningSpeed = GameConstants.BASE_MINING_SPEED; // in the future, this will be modified by upgrades and skills

	public MiningAction(Asteroid asteroid, Player player) {
		this.asteroid = asteroid;
		this.player = player;
	}

	@Override
	public Action getPrerequisite() {
		if (GeometryUtil.isAtSamePosition(player.getShip(), asteroid)) {
			return null;
		} else {
			return new MoveToPositionAction(player.getShip(), asteroid.getX(), asteroid.getY(), player::getShipMovementSpeed);
		}
	}

	@Override
	protected final Integer getRemainingTime() {
		// TODO check remaining cargo space in the player's ship
		return (int)(asteroid.getYieldCapacity() / miningSpeed);
	}

	@Override
	public final boolean isFinishable() {
		// TODO check remaining cargo space in the player's ship
		return asteroid.getYieldCapacity() == 0;
	}

	@Override
	public final void tick() {
		FixedInventory determinedYield = asteroid.obtainYield(miningSpeed);
		if (determinedYield != null) {
			// TODO reduced by what is left as well as inventory space; put back the rest
			FixedInventory actualYield = determinedYield;
			player.getShip().getInventory().add(actualYield);
		}
	}

	@Override
	public String toString() {
		return "mine ores from " + asteroid.getName();
	}

}
