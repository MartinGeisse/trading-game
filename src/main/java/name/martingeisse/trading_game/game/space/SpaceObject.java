package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;
import jdk.nashorn.internal.ir.annotations.Immutable;
import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.MoveToPositionAction;
import name.martingeisse.trading_game.game.action.PlayerAction;

import java.util.List;

/**
 *
 */
public abstract class SpaceObject {

	public static final String DEFAULT_NAME = "unnamed";

	private String name = DEFAULT_NAME;
	private long x;
	private long y;

	/**
	 * Getter method.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = (name == null ? DEFAULT_NAME : name);
	}

	/**
	 * Getter method.
	 *
	 * @return the x
	 */
	public long getX() {
		return x;
	}

	/**
	 * Setter method.
	 *
	 * @param x the x
	 */
	public void setX(long x) {
		this.x = x;
	}

	/**
	 * Getter method.
	 *
	 * @return the y
	 */
	public long getY() {
		return y;
	}

	/**
	 * Setter method.
	 *
	 * @param y the y
	 */
	public void setY(long y) {
		this.y = y;
	}

	/**
	 * Advances game logic.
	 */
	public void tick() {
	}

	/**
	 *
	 */
	public ImmutableList<PlayerAction> getActionsFor(Player player) {
		return ImmutableList.of(getMoveToPositionActionFor(player));
	}

	/**
	 * @param player
	 * @return
	 */
	public final PlayerAction getMoveToPositionActionFor(Player player) {
		return new MoveToPositionAction(player, x, y);
	}

}
