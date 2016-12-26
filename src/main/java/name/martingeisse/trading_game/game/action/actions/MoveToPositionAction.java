package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionExecution;
import name.martingeisse.trading_game.game.action.ProgressSnapshot;
import name.martingeisse.trading_game.game.space.SpaceObject;

import java.util.function.Supplier;

/**
 *
 */
public final class MoveToPositionAction implements Action {

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
	public Integer getTotalTime() {
		return null;
	}

	@Override
	public ActionExecution startExecution() {
		return new ActionExecution() {

			@Override
			public String getName() {
				return "move " + spaceObject.getName() + " to " + x + ", " + y;
			}

			@Override
			public ProgressSnapshot getProgress() {
				return null;
			}

			@Override
			public Integer getRemainingTime() {
				double dx = x - spaceObject.getX();
				double dy = y - spaceObject.getY();
				double norm = Math.sqrt(dx * dx + dy * dy);
				double time = norm / speedProvider.get();
				return (time > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)time);
			}

			@Override
			public void tick() {
				double dx = x - spaceObject.getX();
				double dy = y - spaceObject.getY();
				double norm = Math.sqrt(dx * dx + dy * dy);
				double speed = speedProvider.get();
				if (norm <= speed) {
					spaceObject.setX(x);
					spaceObject.setY(y);
				} else {
					double factor = speed / norm;
					spaceObject.setX(spaceObject.getX() + Math.round(dx * factor));
					spaceObject.setY(spaceObject.getY() + Math.round(dy * factor));
				}
			}

			@Override
			public void cancel() {
			}

			@Override
			public boolean isFinishable() {
				return spaceObject.getX() == x && spaceObject.getY() == y;
			}

			@Override
			public void finish() {
			}

		};
	}

	@Override
	public String toString() {
		return "move to " + x + ", " + y;
	}

}
