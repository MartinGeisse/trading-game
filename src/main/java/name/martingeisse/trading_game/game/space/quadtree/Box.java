package name.martingeisse.trading_game.game.space.quadtree;

/**
 * Simple axis-aligned coordinate box. The min coordinates are inclusive, the max
 * coordinates are exclusive.
 */
public final class Box {

	private final long minX;
	private final long minY;
	private final long maxX;
	private final long maxY;

	public Box(long minX, long minY, long maxX, long maxY) {
		this(minX, minY, maxX, maxY, false);
	}

	private Box(long minX, long minY, long maxX, long maxY, boolean convertNegativeSizeToEmpty) {
		if (minX > maxX) {
			if (convertNegativeSizeToEmpty) {
				minX = maxX;
			} else {
				throw new IllegalArgumentException("minX > maxX");
			}
		}
		if (minY > maxY) {
			if (convertNegativeSizeToEmpty) {
				minY = maxY;
			} else {
				throw new IllegalArgumentException("minY > maxY");
			}
		}
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	/**
	 * Getter method.
	 *
	 * @return the minX
	 */
	public long getMinX() {
		return minX;
	}

	/**
	 * Getter method.
	 *
	 * @return the minY
	 */
	public long getMinY() {
		return minY;
	}

	/**
	 * Getter method.
	 *
	 * @return the maxX
	 */
	public long getMaxX() {
		return maxX;
	}

	/**
	 * Getter method.
	 *
	 * @return the maxY
	 */
	public long getMaxY() {
		return maxY;
	}

	/**
	 * Checks whether this is an empty box.
	 *
	 * @return true if empty, false if nonempty
	 */
	public boolean isEmpty() {
		return (minX == maxX || minY == maxY);
	}

	/**
	 * Checks whether the specified X coordinate is part of this box.
	 *
	 * @param x the X coordinate to check
	 * @return true if contained, false if not
	 */
	public boolean containsX(long x) {
		return (x >= minX && x < maxX);
	}

	/**
	 * Checks whether the specified y coordinate is part of this box.
	 *
	 * @param y the Y coordinate to check
	 * @return true if contained, false if not
	 */
	public boolean containsY(long y) {
		return (y >= minY && y < maxY);
	}

	/**
	 * Checks whether the specified point is part of this box.
	 *
	 * @param x the X coordinate of the point to check
	 * @param y the Y coordinate of the point to check
	 * @return true if contained, false if not
	 */
	public boolean contains(long x, long y) {
		return containsX(x) && containsY(y);
	}

	/**
	 * Checks if the specified X coordinate is a lower bound for this box.
	 *
	 * @param x the X coordinate to check (inclusive)
	 * @return true if it is a lower bound, false if contained or upper bound
	 */
	public boolean isLowerBoundedByX(long x) {
		return minX >= x;
	}

	/**
	 * Checks if the specified X coordinate is an upper bound for this box.
	 *
	 * @param x the X coordinate to check (exclusive)
	 * @return true if it is an upper bound, false if contained or lower bound
	 */
	public boolean isUpperBoundedByX(long x) {
		return maxX <= x;
	}

	/**
	 * Checks if the specified Y coordinate is a lower bound for this box.
	 *
	 * @param y the Y coordinate to check (inclusive)
	 * @return true if it is a lower bound, false if contained or upper bound
	 */
	public boolean isLowerBoundedByY(long y) {
		return minY >= y;
	}

	/**
	 * Checks if the specified Y coordinate is an upper bound for this box.
	 *
	 * @param y the Y coordinate to check (exclusive)
	 * @return true if it is an upper bound, false if contained or lower bound
	 */
	public boolean isUpperBoundedByY(long y) {
		return maxY <= y;
	}

	/**
	 * Computes the union of this box and the specified other box.
	 *
	 * @param other the other box
	 * @return the union
	 */
	public Box union(Box other) {
		return new Box(Math.min(minX, other.minX), Math.min(minY, other.minY), Math.max(maxX, other.maxX), Math.max(maxY, other.maxY));
	}

	/**
	 * Computes the intersection of this box and the specified other box.
	 *
	 * @param other the other box
	 * @return the intersection
	 */
	public Box intersection(Box other) {
		return new Box(Math.max(minX, other.minX), Math.max(minY, other.minY), Math.min(maxX, other.maxX), Math.min(maxY, other.maxY));
	}

}
