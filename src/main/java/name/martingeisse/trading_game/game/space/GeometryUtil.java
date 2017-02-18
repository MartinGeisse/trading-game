package name.martingeisse.trading_game.game.space;

/**
 * Contains helper method for space geometry calculations.
 */
public final class GeometryUtil {

	public static boolean isAtSamePosition(PositionProvider object1, PositionProvider object2) {
		return isAtSamePosition(object1.getX(), object1.getY(), object2.getX(), object2.getY());
	}

	public static boolean isAtSamePosition(long x1, long y1, long x2, long y2) {
		return x1 == x2 && y1 == y2;
	}

	public static double getDistance(PositionProvider object1, PositionProvider object2) {
		return getDistance(object1.getX(), object1.getY(), object2.getX(), object2.getY());
	}

	public static double getDistance(long x1, long y1, long x2, long y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public static int getMovementTime(PositionProvider object1, PositionProvider object2, long speed) {
		return getMovementTime(object1.getX(), object1.getY(), object2.getX(), object2.getY(), speed);
	}

	public static int getMovementTime(long x1, long y1, long x2, long y2, long speed) {
		double time = getDistance(x1, y1, x2, y2) / speed;
		return (time > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) time);
	}

	public static void moveSpaceObjectTowards(SpaceObject spaceObject, PositionProvider destination, long speed) {
		moveSpaceObjectTowards(spaceObject, destination.getX(), destination.getY(), speed);
	}

	public static void moveSpaceObjectTowards(SpaceObject spaceObject, long destinationX, long destinationY, long speed) {
		double dx = destinationX - spaceObject.getX();
		double dy = destinationY - spaceObject.getY();
		double norm = Math.sqrt(dx * dx + dy * dy);
		if (norm <= speed) {
			spaceObject.setPosition(destinationX, destinationY);
		} else {
			double factor = speed / norm;
			spaceObject.setPosition(spaceObject.getX() + Math.round(dx * factor), spaceObject.getY() + Math.round(dy * factor));
		}
	}

}
