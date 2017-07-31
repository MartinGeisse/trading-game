/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import java.io.Serializable;

/**
 * This class represents rows from table 'CachedPlayerAttribute'.
 */
public class CachedPlayerAttributeRow implements Serializable {

	/**
	 * the id
	 */
	private Long id;
	/**
	 * the key
	 */
	private name.martingeisse.trading_game.game.player.PlayerAttributeKey key;
	/**
	 * the playerId
	 */
	private Long playerId;
	/**
	 * the value
	 */
	private String value;

	/**
	 * Constructor.
	 */
	public CachedPlayerAttributeRow() {
	}

	/**
	 * Loads the instance with the specified ID.
	 *
	 * @param connection the database connection
	 * @param id         the ID of the instance to load
	 * @return the loaded instance
	 */
	public static CachedPlayerAttributeRow loadById(PostgresConnection connection, Long id) {
		QCachedPlayerAttributeRow q = QCachedPlayerAttributeRow.CachedPlayerAttribute;
		return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();
	}

	/**
	 * Getter method for the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter method for the id.
	 *
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter method for the key.
	 *
	 * @return the key
	 */
	public name.martingeisse.trading_game.game.player.PlayerAttributeKey getKey() {
		return key;
	}

	/**
	 * Setter method for the key.
	 *
	 * @param key the key to set
	 */
	public void setKey(name.martingeisse.trading_game.game.player.PlayerAttributeKey key) {
		this.key = key;
	}

	/**
	 * Getter method for the playerId.
	 *
	 * @return the playerId
	 */
	public Long getPlayerId() {
		return playerId;
	}

	/**
	 * Setter method for the playerId.
	 *
	 * @param playerId the playerId to set
	 */
	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	/**
	 * Getter method for the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Setter method for the value.
	 *
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Inserts this instance into the database. This object must not have an ID yet.
	 */
	public void insert(PostgresConnection connection) {
		if (id != null) {
			throw new IllegalStateException("this object already has an id: " + id);
		}
		QCachedPlayerAttributeRow q = QCachedPlayerAttributeRow.CachedPlayerAttribute;
		SQLInsertClause insert = connection.insert(q);
		insert.set(q.key, key);
		insert.set(q.playerId, playerId);
		insert.set(q.value, value);
		id = insert.executeWithKey(Long.class);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{CachedPlayerAttributeRow. id = " + id + ", key = " + key + ", playerId = " + playerId + ", value = " + value + "}";
	}

}

