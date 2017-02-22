package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.space.GeometryUtil;
import name.martingeisse.trading_game.game.space.PositionProvider;
import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.function.Supplier;

/**
 *
 */
public final class MoveToPositionAction extends ContinuousAction implements PositionProvider {

	private final SpaceObject spaceObject;
	private final long x;
	private final long y;
	private final Supplier<Long> speedProvider;

	/**
	 *
	 */
	public MoveToPositionAction(SpaceObject spaceObject, long x, long y, Supplier<Long> speedProvider) {
		this.spaceObject = spaceObject;
		this.x = x;
		this.y = y;
		this.speedProvider = speedProvider;
	}

	@Override
	public long getX() {
		return x;
	}

	@Override
	public long getY() {
		return y;
	}

	@Override
	public Action getPrerequisite() {
		return null;
	}

	@Override
	public Integer getRemainingTime() {
		return GeometryUtil.getMovementTime(spaceObject, this, speedProvider.get());
	}

	@Override
	public Status tick() {
		GeometryUtil.moveSpaceObjectTowards(spaceObject, x, y, speedProvider.get());
		return GeometryUtil.isAtSamePosition(spaceObject, this) ? Status.FINISHED : Status.RUNNING;
	}

	@Override
	public String getName() {
		return "move " + spaceObject.getName() + " to " + x + ", " + y;
	}

}
