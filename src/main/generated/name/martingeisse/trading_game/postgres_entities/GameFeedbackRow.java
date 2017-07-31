/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import java.io.Serializable;

/**
 * This class represents rows from table 'GameFeedback'.
 */
public class GameFeedbackRow implements Serializable {

	/**
	 * the context
	 */
	private name.martingeisse.trading_game.tools.codegen.PostgresJsonb context;
	/**
	 * the id
	 */
	private Long id;
	/**
	 * the playerId
	 */
	private Long playerId;
	/**
	 * the sessionId
	 */
	private String sessionId;
	/**
	 * the text
	 */
	private String text;
	/**
	 * the timestamp
	 */
	private org.joda.time.DateTime timestamp;

	/**
	 * Constructor.
	 */
	public GameFeedbackRow() {
	}

	/**
	 * Loads the instance with the specified ID.
	 *
	 * @param connection the database connection
	 * @param id         the ID of the instance to load
	 * @return the loaded instance
	 */
	public static GameFeedbackRow loadById(PostgresConnection connection, Long id) {
		QGameFeedbackRow q = QGameFeedbackRow.GameFeedback;
		return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();
	}

	/**
	 * Getter method for the context.
	 *
	 * @return the context
	 */
	public name.martingeisse.trading_game.tools.codegen.PostgresJsonb getContext() {
		return context;
	}

	/**
	 * Setter method for the context.
	 *
	 * @param context the context to set
	 */
	public void setContext(name.martingeisse.trading_game.tools.codegen.PostgresJsonb context) {
		this.context = context;
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
	 * Getter method for the sessionId.
	 *
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Setter method for the sessionId.
	 *
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Getter method for the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter method for the text.
	 *
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Getter method for the timestamp.
	 *
	 * @return the timestamp
	 */
	public org.joda.time.DateTime getTimestamp() {
		return timestamp;
	}

	/**
	 * Setter method for the timestamp.
	 *
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(org.joda.time.DateTime timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Inserts this instance into the database. This object must not have an ID yet.
	 */
	public void insert(PostgresConnection connection) {
		if (id != null) {
			throw new IllegalStateException("this object already has an id: " + id);
		}
		QGameFeedbackRow q = QGameFeedbackRow.GameFeedback;
		SQLInsertClause insert = connection.insert(q);
		insert.set(q.context, context);
		insert.set(q.playerId, playerId);
		insert.set(q.sessionId, sessionId);
		insert.set(q.text, text);
		insert.set(q.timestamp, timestamp);
		id = insert.executeWithKey(Long.class);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{GameFeedbackRow. context = " + context + ", id = " + id + ", playerId = " + playerId + ", sessionId = " + sessionId + ", text = " + text + ", timestamp = " + timestamp + "}";
	}

}

