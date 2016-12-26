package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionExecution;
import name.martingeisse.trading_game.game.space.SpaceObject;

/**
 * TODO implement
 */
public final class MoveToPositionAction implements Action {

	private final SpaceObject spaceObject;
	private final long x;
	private final long y;

	/**
	 *
	 */
	public MoveToPositionAction(SpaceObject spaceObject, long x, long y) {
		this.spaceObject = spaceObject;
		this.x = x;
		this.y = y;
	}

	@Override
	public Integer getTotalTime() {
		return null;
	}

	@Override
	public ActionExecution startExecution() {
		return null;
	}

	@Override
	public String toString() {
		return "move to " + x + ", " + y;
	}

}
