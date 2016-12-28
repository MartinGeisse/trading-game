package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.actions.MiningAction;
import name.martingeisse.trading_game.game.definition.MiningYieldInfo;
import name.martingeisse.trading_game.game.item.FixedInventory;

/**
 *
 */
public final class Asteroid extends SpaceObject {

	private final MiningYieldInfo yieldInfo;
	private long yieldCapacity;

	public Asteroid(MiningYieldInfo yieldInfo, long yieldCapacity) {
		this.yieldInfo = yieldInfo;
		this.yieldCapacity = yieldCapacity;
	}

	public FixedInventory obtainYield(long amount) {
		if (yieldCapacity == 0) {
			return null;
		}
		if (yieldCapacity < amount) {
			amount = yieldCapacity;
		}
		yieldCapacity -= amount;
		return yieldInfo.determineBaseYield(amount);
	}

	@Override
	public ImmutableList<Action> getActionsFor(Player player) {
		return ImmutableList.of(getMoveToPositionActionFor(player), new MiningAction(this, player.getInventory()));
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
