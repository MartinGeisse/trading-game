package name.martingeisse.trading_game.game.cqrs;

/**
 *
 */
public interface ProjectionProvider {

	/**
	 * Obtains a projection using its key.
	 */
	public <T> T getProjection(ProjectionKey<T> key);

}
