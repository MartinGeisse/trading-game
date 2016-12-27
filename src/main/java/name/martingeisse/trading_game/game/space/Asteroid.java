package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.actions.MiningAction;
import name.martingeisse.trading_game.game.item.FixedInventory;

/**
 *
 */
public final class Asteroid extends SpaceObject {

	private final FixedInventory yieldPerTick; // TODO this should have a random component, and the asteroid should
		// keep an inventory of ores left that should define the yield per tick

	public Asteroid(FixedInventory yieldPerTick) {
		this.yieldPerTick = yieldPerTick;
	}

	public FixedInventory determineYieldForTick() {
		return yieldPerTick;
	}

	@Override
	public ImmutableList<Action> getActionsFor(Player player) {
		return ImmutableList.of(getMoveToPositionActionFor(player), new MiningAction(this, player.getInventory()));
	}

}
