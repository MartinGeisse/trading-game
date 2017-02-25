/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import java.io.Serializable;

/**
 * This class represents rows from table 'PlayerSkillLearningQueueSlot'.
 */
public class PlayerSkillLearningQueueSlotRow implements Serializable {

    /**
     * Constructor.
     */
    public PlayerSkillLearningQueueSlotRow() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the learningOrderIndex
     */
    private Integer learningOrderIndex;

    /**
     * the learningPoints
     */
    private Integer learningPoints;

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
     * @param id the ID of the instance to load
     * @return the loaded instance
     */
    public static PlayerSkillLearningQueueSlotRow loadById(PostgresConnection connection, Long id) {
        QPlayerSkillLearningQueueSlotRow q = QPlayerSkillLearningQueueSlotRow.PlayerSkillLearningQueueSlot;
        return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();
    }

    /**
     * Inserts this instance into the database. This object must not have an ID yet.
     */
    public void insert(PostgresConnection connection) {
        if (id != null) {
        	throw new IllegalStateException("this object already has an id: " + id);
        }
        QPlayerSkillLearningQueueSlotRow q = QPlayerSkillLearningQueueSlotRow.PlayerSkillLearningQueueSlot;
        SQLInsertClause insert = connection.insert(q);
        insert.set(q.learningOrderIndex, learningOrderIndex);
        insert.set(q.learningPoints, learningPoints);
        insert.set(q.playerId, playerId);
        insert.set(q.skillType, skillType);
        id = insert.executeWithKey(Long.class);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PlayerSkillLearningQueueSlotRow. id = " + id + ", learningOrderIndex = " + learningOrderIndex + ", learningPoints = " + learningPoints + ", playerId = " + playerId + ", skillType = " + skillType + "}";
    }

}

