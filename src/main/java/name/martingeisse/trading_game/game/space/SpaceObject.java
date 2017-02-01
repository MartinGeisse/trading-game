package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.actions.MoveToPositionAction;

/**
 *
 */
public abstract class SpaceObject implements PositionProvider {

	public static final String DEFAULT_NAME = "unnamed";

	private long id = -1;
	private String name = DEFAULT_NAME;
	private long x;
	private long y;

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter method.
	 *
	 * @param id the id
	 */
	void setId(long id) {
		if (id < 0) {
			throw new IllegalArgumentException("id < 0");
		}
		if (this.id < 0) {
			this.id = id;
		} else {
			throw new IllegalStateException("id has already been set");
		}
	}

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
	@Override
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
	@Override
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
	 * Whether the tick() function is supported.
	 */
	public boolean supportsTick() {
		return false;
	}

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick() {
		throw new UnsupportedOperationException("tick() not supported");
	}

	/**
	 *
	 */
	public ImmutableList<Action> getActionsFor(Player player) {
		return ImmutableList.of(getMoveToPositionActionFor(player));
	}

	/**
	 * @param player
	 * @return
	 */
	public final Action getMoveToPositionActionFor(Player player) {
		return new MoveToPositionAction(player.getShip(), x, y, player::getShipMovementSpeed);
	}

}
