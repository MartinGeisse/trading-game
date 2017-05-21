package name.martingeisse.trading_game.game.space;

/**
 * A space object that can change its position or shape. Dynamic space objects are presented as individual graphical
 * objects on an overlay layer, and updated via websockets.
 */
public abstract class DynamicSpaceObject extends SpaceObject {

	/**
	 * Returns information about the movement of this object, or null if not moving.
	 */
	public abstract MovementInfo getMovementInfo();

}
