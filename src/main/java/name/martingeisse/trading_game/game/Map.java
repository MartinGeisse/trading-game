package name.martingeisse.trading_game.game;

/**
 *
 */
public final class Map {

	private final int width;
	private final int height;
	private final int[] blocks;

	public Map() {
		width = 10;
		height = 10;
		blocks = new int[width * height];
	}

	/**
	 * Getter method.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Getter method.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	public int getBlock(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return 0;
		} else {
			return blocks[y * width + x];
		}
	}

}
