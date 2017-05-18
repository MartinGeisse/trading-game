package name.martingeisse.trading_game.game.action.actions;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.definition.GameConstants;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.Asteroid;
import name.martingeisse.trading_game.game.space.GeometryUtil;

/**
 *
 */
public final class MiningAction extends ContinuousAction {

	private final GameEventEmitter gameEventEmitter;
	private final Asteroid asteroid;
	private final Player player;
	private final long miningSpeed = GameConstants.BASE_MINING_SPEED; // in the future, this will be modified by upgrades and skills

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public MiningAction(@JacksonInject GameEventEmitter gameEventEmitter,
						@JsonProperty(value = "asteroid", required = true) Asteroid asteroid,
						@JsonProperty(value = "player", required = true) Player player) {
		this.gameEventEmitter = gameEventEmitter;
		this.asteroid = asteroid;
		this.player = player;
	}

	/**
	 * Getter method.
	 *
	 * @return the asteroid
	 */
	public Asteroid getAsteroid() {
		return asteroid;
	}

	/**
	 * Getter method.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	@Override
	public Action getPrerequisite() {
		if (GeometryUtil.isAtSamePosition(player.getShip(), asteroid)) {
			return null;
		} else {
			return new MoveToPositionAction(player, asteroid.getX(), asteroid.getY());
		}
	}

	@Override
	public final Integer getRemainingTime() {

		int remainingTimeForYieldCapacity;
		{
			long remainingYieldCapacity = (int) asteroid.getYieldCapacity();
			remainingTimeForYieldCapacity = (int) (remainingYieldCapacity / miningSpeed);
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
	public final Action.Status tick() {
		int remainingCargoMass = getRemainingCargoMass();
		if (remainingCargoMass <= 0) {
			return Action.Status.FINISHED;
		}
		MiningYield yield = asteroid.obtainYield(miningSpeed, remainingCargoMass);
		if (yield.getItems() != null) {
			player.getInventory().add(player.getId(), yield.getItems());
		}
		if (yield.isDepleted() || yield.isCargoExhausted()) {
			return Action.Status.FINISHED;
		} else {
			return Action.Status.RUNNING;
		}
	}

	private int getRemainingCargoMass() {
		return player.getMaximumCargoMass() - player.getInventory().getMass(null);
	}

	@Override
	public String getName() {
		return "mine ores from " + asteroid.getName();
	}

}
