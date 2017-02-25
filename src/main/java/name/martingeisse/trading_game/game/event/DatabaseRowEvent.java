package name.martingeisse.trading_game.game.event;

import com.querydsl.sql.RelationalPathBase;

/**
 * An event that affects a single database row.
 * <p>
 * Subclasses may further describe the changes to that row, but they cannot indicate changes outside that row.
 */
public class DatabaseRowEvent extends DatabaseTableEvent {

	private final long id;

	/**
	 * @param path the database path of the table that has changed
	 * @param id   the id of the row that has changed
	 */
	public DatabaseRowEvent(RelationalPathBase<?> path, long id) {
		super(path);
		this.id = id;
	}

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

}
