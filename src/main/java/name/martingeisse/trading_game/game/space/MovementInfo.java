package name.martingeisse.trading_game.game.space;

/**
 * Immutable class that is used by {@link DynamicSpaceObject} to give a report about its movement.
 */
public final class MovementInfo {

	private final long destinationX;
	private final long destinationY;
	private final long currentSpeed;
	private final int remainingTime;

	public MovementInfo(long destinationX, long destinationY, long currentSpeed, int remainingTime) {
		this.destinationX = destinationX;
		this.destinationY = destinationY;
		this.currentSpeed = currentSpeed;
		this.remainingTime = remainingTime;
	}

	/**
	 * Getter method.
	 *
	 * @return the destinationX
	 */
	public long getDestinationX() {
		return destinationX;
	}

	/**
	 * Getter method.
	 *
	 * @return the destinationY
	 */
	public long getDestinationY() {
		return destinationY;
	}

	/**
	 * Getter method.
	 *
	 * @return the currentSpeed
	 */
	public long getCurrentSpeed() {
		return currentSpeed;
	}

	/**
	 * Getter method.
	 *
	 * @return the remainingTime
	 */
	public int getRemainingTime() {
		return remainingTime;
	}

}
