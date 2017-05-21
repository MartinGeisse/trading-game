package name.martingeisse.trading_game.gui.map;

/**
 *
 */
public final class MapCoordinates {

	public static final int HEAT_MAP_ZOOM_THRESHOLD = 5;
	public static final double COORDINATE_SCALE = 100000;

	public static double convertXToLongitude(long x) {
		return 128 + ((double) x) / COORDINATE_SCALE;
	}

	public static double convertDeltaXToDeltaLongitude(long dx) {
		return ((double) dx) / COORDINATE_SCALE;
	}

	public static double convertYToLatitude(long y) {
		return 128 - ((double) y) / COORDINATE_SCALE;
	}

	public static double convertDeltaYToDeltaLatitude(long dy) {
		return -((double) dy) / COORDINATE_SCALE;
	}

	public static double convertGameDistanceToMapDistance(long gameDistance) {
		return ((double) gameDistance) / COORDINATE_SCALE;
	}

	public static long convertLongitudeToX(double longitude) {
		return (long) ((longitude - 128) * COORDINATE_SCALE);
	}

	public static long convertDeltaLongitudeToDeltaX(double deltaLongitude) {
		return (long) (deltaLongitude * COORDINATE_SCALE);
	}

	public static long convertLatitudeToY(double latitude) {
		return (long) ((128 - latitude) * COORDINATE_SCALE);
	}

	public static long convertDeltaLatitudeToDeltaY(double deltaLatitude) {
		return (long) (-deltaLatitude * COORDINATE_SCALE);
	}

	public static long convertMapDistanceToGameDistance(double mapDistance) {
		return (long) (mapDistance * COORDINATE_SCALE);
	}

}
