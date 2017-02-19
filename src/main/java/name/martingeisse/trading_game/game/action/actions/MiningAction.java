package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.definition.GameConstants;
import name.martingeisse.trading_game.game.space.Asteroid;
import name.martingeisse.trading_game.game.space.GeometryUtil;

/**
 *
 */
public final class MiningAction extends ContinuousAction {

	private final Asteroid asteroid;
	private final Player player;
	private final long miningSpeed = GameConstants.BASE_MINING_SPEED; // in the future, this will be modified by upgrades and skills
	private boolean cannotContinue = false;

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

		int remainingTimeForYieldCapacity;
		{
			long remainingYieldCapacity = (int)asteroid.getYieldCapacity();
			remainingTimeForYieldCapacity = (int)(remainingYieldCapacity / miningSpeed);
		}

		int remainingTimeForCargo;
		{
			// We don't respect the remaining capacity here since, if the capacity is almost depleted, then the above
			// "time for yield capacity" will reflect that. The "remaining time for cargo" will be pointless in that
			// case (so just keep it large enough to be irrelevant in the .min() call below) since it is based on the
			// assumption that mining can continue for some time, which is not the case if the capacity is depleted.
			int estimatedYieldMass = asteroid.estimateYieldMass(miningSpeed, false);
			int remainingCargoMass = getRemainingCargoMass();
			remainingTimeForCargo = remainingCargoMass / estimatedYieldMass;
			System.out.println("+ " + remainingCargoMass + " / " + estimatedYieldMass);
		}

		System.out.println("* " + remainingTimeForYieldCapacity + " / " + remainingTimeForCargo);
		return Math.min(remainingTimeForYieldCapacity, remainingTimeForCargo);
	}

	@Override
	public final boolean isFinishable() {
		return cannotContinue;
	}

	@Override
	public final void tick() {
		MiningYield yield = asteroid.obtainYield(miningSpeed, getRemainingCargoMass());
		if (yield.isDepleted() || yield.isCargoExhausted()) {
			cannotContinue = true;
		}
		if (yield.getItems() != null) {
			player.getInventory().add(yield.getItems());
			player.getGame().getListeners().onSpaceObjectPropertiesChanged(player.getShip());
		}
	}

	private int getRemainingCargoMass() {
		return player.getMaximumCargoMass() - player.getInventory().getMass();
	}

	@Override
	public String toString() {
		return "mine ores from " + asteroid.getName();
	}

}
