package name.martingeisse.trading_game.game.event;

import com.querydsl.sql.RelationalPathBase;

/**
 * An event that affects a single database table.
 * <p>
 * Subclasses may further describe the changes to that table, but they cannot indicate changes outside that table.
 */
public class DatabaseTableEvent extends GameEvent {

	private final RelationalPathBase<?> path;

	/**
	 * @param path the database path of the table that has changed
	 */
	public DatabaseTableEvent(RelationalPathBase<?> path) {
		this.path = path;
	}

	/**
	 * Getter method.
	 *
	 * @return the path
	 */
	public RelationalPathBase<?> getPath() {
		return path;
	}

	/**
	 * @return the class of the database rows of the table that has changed.
	 */
	public Class<?> getRowClass() {
		return getPath().getType();
	}

}
