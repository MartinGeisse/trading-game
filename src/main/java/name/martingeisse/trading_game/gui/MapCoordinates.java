package name.martingeisse.trading_game.gui;

/**
 *
 */
public final class MapCoordinates {

	public static final int HEAT_MAP_ZOOM_THRESHOLD = 5;
	public static final double COORDINATE_SCALE = 100000;

	public static double convertXToLongitude(long x) {
		return 128 + ((double)x) / COORDINATE_SCALE;
	}

	public static double convertYToLatitude(long y) {
		return 128 - ((double)y) / COORDINATE_SCALE;
	}

	public static double convertGameDistanceToMapDistance(long gameDistance) {
		return ((double)gameDistance) / COORDINATE_SCALE;
	}

	public static long convertLongitudeToX(double mapPosition) {
		return (long)((mapPosition - 128) * COORDINATE_SCALE);
	}

	public static long convertLatitudeToY(double mapPosition) {
		return (long)((128 - mapPosition) * COORDINATE_SCALE);
	}

	public static long convertMapDistanceToGameDistance(double mapDistance) {
		return (long)(mapDistance * COORDINATE_SCALE);
	}

}
