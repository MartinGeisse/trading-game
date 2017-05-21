package name.martingeisse.trading_game.game.action.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.GeometryUtil;
import name.martingeisse.trading_game.game.space.PositionProvider;

/**
 * In the long run, this action shouldn't take the player. Currently it does both to obtain the player's ship to
 * move as well as the movement speed from the player itself. It would be nicer to obtain the player from the context
 * (e.g. per-run JacksonInject).
 * <p>
 * Might also take a SpaceObject (ID) instead of x,y to follow a target object (unclear though what to do when reaching it)
 */
public final class MoveToPositionAction extends ContinuousAction implements PositionProvider {

	private final Player player;
	private final long x;
	private final long y;

	/**
	 *
	 */
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public MoveToPositionAction(
			@JsonProperty(value = "player", required = true) Player player,
			@JsonProperty(value = "x", required = true) long x,
			@JsonProperty(value = "y", required = true) long y) {
		this.player = player;
		this.x = x;
		this.y = y;
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

	@JsonIgnore
	public long getSpeed() {
		return player.getShipMovementSpeed();
	}

	@Override
	public Integer getRemainingTime() {
		return GeometryUtil.getMovementTime(player.getShip(), this, getSpeed());
	}

	@Override
	public Status tick() {
		GeometryUtil.moveSpaceObjectTowards(player.getShip(), this, getSpeed());
		return GeometryUtil.isAtSamePosition(player.getShip(), this) ? Status.FINISHED : Status.RUNNING;
	}

	@Override
	public String getName() {
		return "move " + player.getShip().getName() + " to " + x + ", " + y;
	}

}
