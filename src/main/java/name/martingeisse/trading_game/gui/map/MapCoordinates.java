package name.martingeisse.trading_game.gui.map;

/**
 *
 */
public final class MapCoordinates {

	public static final int HEAT_MAP_ZOOM_THRESHOLD = 5;
	public static final double COORDINATE_SCALE = 100000;

	public static double gamePositionToMapX(long gamePosition) {
		return 128 + ((double)gamePosition) / COORDINATE_SCALE;
	}

	public static double gamePositionToMapY(long gamePosition) {
		return 128 - ((double)gamePosition) / COORDINATE_SCALE;
	}

	public static double gameDistanceToMap(long gameDistance) {
		return ((double)gameDistance) / COORDINATE_SCALE;
	}

	public static long mapPositionToGameX(double mapPosition) {
		return (long)((mapPosition - 128) * COORDINATE_SCALE);
	}

	public static long mapPositionToGameY(double mapPosition) {
		return (long)((128 - mapPosition) * COORDINATE_SCALE);
	}

	public static long mapDistanceToGame(double mapDistance) {
		return (long)(mapDistance * COORDINATE_SCALE);
	}

}
