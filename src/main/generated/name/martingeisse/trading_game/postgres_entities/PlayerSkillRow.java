/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import java.io.Serializable;

/**
 * This class represents rows from table 'PlayerSkill'.
 */
public class PlayerSkillRow implements Serializable {

	/**
	 * Constructor.
	 */
	public PlayerSkillRow() {
	}

	/**
	 * the id
	 */
	private Long id;

	/**
	 * the learningFinished
	 */
	private Boolean learningFinished;

	/**
	 * the learningOrderIndex
	 */
	private Integer learningOrderIndex;

	/**
	 * the learningPoints
	 */
	private Integer learningPoints;

	/**
	 * the name
	 */
	private String name;

	/**
	 * the playerId
	 */
	private Long playerId;

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
	 * Getter method for the learningFinished.
	 *
	 * @return the learningFinished
	 */
	public Boolean getLearningFinished() {
		return learningFinished;
	}

	/**
	 * Setter method for the learningFinished.
	 *
	 * @param learningFinished the learningFinished to set
	 */
	public void setLearningFinished(Boolean learningFinished) {
		this.learningFinished = learningFinished;
	}

	/**
	 * Getter method for the learningOrderIndex.
	 *
	 * @return the learningOrderIndex
	 */
	public Integer getLearningOrderIndex() {
		return learningOrderIndex;
	}

	/**
	 * Setter method for the learningOrderIndex.
	 *
	 * @param learningOrderIndex the learningOrderIndex to set
	 */
	public void setLearningOrderIndex(Integer learningOrderIndex) {
		this.learningOrderIndex = learningOrderIndex;
	}

	/**
	 * Getter method for the learningPoints.
	 *
	 * @return the learningPoints
	 */
	public Integer getLearningPoints() {
		return learningPoints;
	}

	/**
	 * Setter method for the learningPoints.
	 *
	 * @param learningPoints the learningPoints to set
	 */
	public void setLearningPoints(Integer learningPoints) {
		this.learningPoints = learningPoints;
	}

	/**
	 * Getter method for the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Loads the instance with the specified ID.
	 *
	 * @param connection the database connection
	 * @param id         the ID of the instance to load
	 * @return the loaded instance
	 */
	public static PlayerSkillRow loadById(PostgresConnection connection, Long id) {
		QPlayerSkillRow q = QPlayerSkillRow.PlayerSkill;
		return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();
	}

	/**
	 * Inserts this instance into the database. This object must not have an ID yet.
	 */
	public void insert(PostgresConnection connection) {
		if (id != null) {
			throw new IllegalStateException("this object already has an id: " + id);
		}
		QPlayerSkillRow q = QPlayerSkillRow.PlayerSkill;
		SQLInsertClause insert = connection.insert(q);
		insert.set(q.learningFinished, learningFinished);
		insert.set(q.learningOrderIndex, learningOrderIndex);
		insert.set(q.learningPoints, learningPoints);
		insert.set(q.name, name);
		insert.set(q.playerId, playerId);
		id = insert.executeWithKey(Long.class);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{PlayerSkillRow. id = " + id + ", learningFinished = " + learningFinished + ", learningOrderIndex = " + learningOrderIndex + ", learningPoints = " + learningPoints + ", name = " + name + ", playerId = " + playerId + "}";
	}

}

