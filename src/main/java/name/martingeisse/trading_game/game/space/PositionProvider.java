package name.martingeisse.trading_game.game.space;

/**
 * Can be implemented by arbitrary objects (even non-persistent helper objects) to provide a position, e.g. for
 * methods of {@link GeometryUtil}.
 */
public interface PositionProvider {
	public long getX();
	public long getY();
}
