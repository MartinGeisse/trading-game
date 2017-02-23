/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import java.io.Serializable;

/**
 * This class represents rows from table 'ActionQueueSlot'.
 */
public class ActionQueueSlotRow implements Serializable {

    /**
     * Constructor.
     */
    public ActionQueueSlotRow() {
    }

    /**
     * the action
     */
    private name.martingeisse.trading_game.tools.codegen.PostgresJsonb action;

    /**
     * the actionQueueId
     */
    private Long actionQueueId;

    /**
     * the id
     */
    private Long id;

    /**
     * the prerequisite
     */
    private Boolean prerequisite;

    /**
     * the started
     */
    private Boolean started;

    /**
     * Getter method for the action.
     * 
     * @return the action
     */
    public name.martingeisse.trading_game.tools.codegen.PostgresJsonb getAction() {
        return action;
    }

    /**
     * Setter method for the action.
     * 
     * @param action the action to set
     */
    public void setAction(name.martingeisse.trading_game.tools.codegen.PostgresJsonb action) {
        this.action = action;
    }

    /**
     * Getter method for the actionQueueId.
     * 
     * @return the actionQueueId
     */
    public Long getActionQueueId() {
        return actionQueueId;
    }

    /**
     * Setter method for the actionQueueId.
     * 
     * @param actionQueueId the actionQueueId to set
     */
    public void setActionQueueId(Long actionQueueId) {
        this.actionQueueId = actionQueueId;
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
     * Getter method for the prerequisite.
     * 
     * @return the prerequisite
     */
    public Boolean getPrerequisite() {
        return prerequisite;
    }

    /**
     * Setter method for the prerequisite.
     * 
     * @param prerequisite the prerequisite to set
     */
    public void setPrerequisite(Boolean prerequisite) {
        this.prerequisite = prerequisite;
    }

    /**
     * Getter method for the started.
     * 
     * @return the started
     */
    public Boolean getStarted() {
        return started;
    }

    /**
     * Setter method for the started.
     * 
     * @param started the started to set
     */
    public void setStarted(Boolean started) {
        this.started = started;
    }

    /**
     * Loads the instance with the specified ID.
     * 
     * @param connection the database connection
     * @param id the ID of the instance to load
     * @return the loaded instance
     */
    public static ActionQueueSlotRow loadById(PostgresConnection connection, Long id) {
        QActionQueueSlotRow q = QActionQueueSlotRow.ActionQueueSlot;
        return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();
    }

    /**
     * Inserts this instance into the database. This object must not have an ID yet.
     */
    public void insert(PostgresConnection connection) {
        if (id != null) {
        	throw new IllegalStateException("this object already has an id: " + id);
        }
        QActionQueueSlotRow q = QActionQueueSlotRow.ActionQueueSlot;
        SQLInsertClause insert = connection.insert(q);
        insert.set(q.action, action);
        insert.set(q.actionQueueId, actionQueueId);
        insert.set(q.prerequisite, prerequisite);
        insert.set(q.started, started);
        id = insert.executeWithKey(Long.class);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{ActionQueueSlotRow. action = " + action + ", actionQueueId = " + actionQueueId + ", id = " + id + ", prerequisite = " + prerequisite + ", started = " + started + "}";
    }

}

