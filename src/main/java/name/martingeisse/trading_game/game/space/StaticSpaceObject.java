package name.martingeisse.trading_game.game.space;

/**
 * This kind of object does not change its position or shape. Static space objects will be presented via pre-rendered
 * map tiles.
 */
public abstract class StaticSpaceObject extends SpaceObject {

	/**
	 * The radius of a bounding circle that contains static space objects of any size.
	 */
	public static final long BOUNDING_RADIUS = 5000;

}
