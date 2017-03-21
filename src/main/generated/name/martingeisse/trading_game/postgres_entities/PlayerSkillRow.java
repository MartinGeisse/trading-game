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
	 * the playerId
	 */
	private Long playerId;

	/**
	 * the skillType
	 */
	private String skillType;

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
	 * Getter method for the skillType.
	 *
	 * @return the skillType
	 */
	public String getSkillType() {
		return skillType;
	}

	/**
	 * Setter method for the skillType.
	 *
	 * @param skillType the skillType to set
	 */
	public void setSkillType(String skillType) {
		this.skillType = skillType;
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
		insert.set(q.playerId, playerId);
		insert.set(q.skillType, skillType);
		id = insert.executeWithKey(Long.class);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{PlayerSkillRow. id = " + id + ", playerId = " + playerId + ", skillType = " + skillType + "}";
	}

}

