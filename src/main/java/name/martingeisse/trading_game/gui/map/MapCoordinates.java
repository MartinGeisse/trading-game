package name.martingeisse.trading_game.gui.map;

/**
 *
 */
public final class MapCoordinates {

	public static final int HEAT_MAP_ZOOM_THRESHOLD = 50;
	public static final double COORDINATE_SCALE = 1000;

	public static double gamePositionToMap(long gamePosition) {
		return 128 + ((double)gamePosition) / COORDINATE_SCALE;
	}

	public static double gameDistanceToMap(long gameDistance) {
		return ((double)gameDistance) / COORDINATE_SCALE;
	}

	public static long mapPositionToGame(double mapPosition) {
		return (long)((mapPosition - 128) * COORDINATE_SCALE);
	}

	public static long mapDistanceToGame(double mapDistance) {
		return (long)(mapDistance * COORDINATE_SCALE);
	}

}
