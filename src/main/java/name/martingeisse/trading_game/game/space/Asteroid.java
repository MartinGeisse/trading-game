package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.action.actions.MiningAction;
import name.martingeisse.trading_game.game.action.actions.MiningYield;
import name.martingeisse.trading_game.game.definition.MiningYieldInfo;
import name.martingeisse.trading_game.game.item.ImmutableItemStacks;

/**
 *
 */
public final class Asteroid extends StaticSpaceObject {

	private final MiningYieldInfo yieldInfo;
	private long yieldCapacity;

	public Asteroid(MiningYieldInfo yieldInfo, long yieldCapacity) {
		this.yieldInfo = yieldInfo;
		this.yieldCapacity = yieldCapacity;
	}

	/**
	 * Estimates the mass of the yield from the specified rock amount.
	 */
	public int estimateYieldMass(long minedRockAmount, boolean respectRemainingCapacity) {
		if (respectRemainingCapacity) {
			if (yieldCapacity == 0) {
				return 0;
			}
			if (yieldCapacity < minedRockAmount) {
				minedRockAmount = yieldCapacity;
			}
		}
		ImmutableItemStacks yield = yieldInfo.determineYield(minedRockAmount);
		return yield == null ? 0 : yield.getMass();
	}

	/**
	 * Obtains mining yield from this asteroid for the specified amount of rock mined, constrained by the specified
	 * amount of cargo space left.
	 */
	public MiningYield obtainYield(long minedRockAmount, int availableCargoMass) {
		if (yieldCapacity == 0) {
			return new MiningYield(null, true, false);
		}
		boolean depleted = (yieldCapacity < minedRockAmount);
		if (depleted) {
			minedRockAmount = yieldCapacity;
		}
		ImmutableItemStacks yield = yieldInfo.determineYield(minedRockAmount);
		boolean cargoExhausted = availableCargoMass < yield.getMass();
		if (cargoExhausted) {
			long adjustedMinedRockAmount = minedRockAmount * availableCargoMass / yield.getMass();
			if (adjustedMinedRockAmount < minedRockAmount) {
				depleted = false;
			}
			yield = yield.reduceToMass(availableCargoMass);
			minedRockAmount = adjustedMinedRockAmount;
		}
		yieldCapacity -= minedRockAmount;
		return new MiningYield(yield, depleted, cargoExhausted);
	}

	@Override
	public ImmutableList<Action> getActionsFor(Player player) {
		return ImmutableList.of(getMoveToPositionActionFor(player), new MiningAction(this, player));
	}

	/**
	 * Getter method.
	 *
	 * @return the yieldCapacity
	 */
	public long getYieldCapacity() {
		return yieldCapacity;
	}

}
