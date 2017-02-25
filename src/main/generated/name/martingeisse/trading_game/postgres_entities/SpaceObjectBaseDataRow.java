/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import java.io.Serializable;

/**
 * This class represents rows from table 'SpaceObjectBaseData'.
 */
public class SpaceObjectBaseDataRow implements Serializable {

    /**
     * Constructor.
     */
    public SpaceObjectBaseDataRow() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the inventoryId
     */
    private Long inventoryId;

    /**
     * the longField1
     */
    private Long longField1;

    /**
     * the name
     */
    private String name;

    /**
     * the position
     */
    private org.postgresql.geometric.PGpoint position;

    /**
     * the type
     */
    private name.martingeisse.trading_game.game.space.SpaceObjectType type;

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
     * Getter method for the inventoryId.
     * 
     * @return the inventoryId
     */
    public Long getInventoryId() {
        return inventoryId;
    }

    /**
     * Setter method for the inventoryId.
     * 
     * @param inventoryId the inventoryId to set
     */
    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    /**
     * Getter method for the longField1.
     * 
     * @return the longField1
     */
    public Long getLongField1() {
        return longField1;
    }

    /**
     * Setter method for the longField1.
     * 
     * @param longField1 the longField1 to set
     */
    public void setLongField1(Long longField1) {
        this.longField1 = longField1;
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
     * Getter method for the position.
     * 
     * @return the position
     */
    public org.postgresql.geometric.PGpoint getPosition() {
        return position;
    }

    /**
     * Setter method for the position.
     * 
     * @param position the position to set
     */
    public void setPosition(org.postgresql.geometric.PGpoint position) {
        this.position = position;
    }

    /**
     * Getter method for the type.
     * 
     * @return the type
     */
    public name.martingeisse.trading_game.game.space.SpaceObjectType getType() {
        return type;
    }

    /**
     * Setter method for the type.
     * 
     * @param type the type to set
     */
    public void setType(name.martingeisse.trading_game.game.space.SpaceObjectType type) {
        this.type = type;
    }

    /**
     * Loads the instance with the specified ID.
     * 
     * @param connection the database connection
     * @param id the ID of the instance to load
     * @return the loaded instance
     */
    public static SpaceObjectBaseDataRow loadById(PostgresConnection connection, Long id) {
        QSpaceObjectBaseDataRow q = QSpaceObjectBaseDataRow.SpaceObjectBaseData;
        return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();
    }

    /**
     * Inserts this instance into the database. This object must not have an ID yet.
     */
    public void insert(PostgresConnection connection) {
        if (id != null) {
        	throw new IllegalStateException("this object already has an id: " + id);
        }
        QSpaceObjectBaseDataRow q = QSpaceObjectBaseDataRow.SpaceObjectBaseData;
        SQLInsertClause insert = connection.insert(q);
        insert.set(q.inventoryId, inventoryId);
        insert.set(q.longField1, longField1);
        insert.set(q.name, name);
        insert.set(q.position, position);
        insert.set(q.type, type);
        id = insert.executeWithKey(Long.class);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{SpaceObjectBaseDataRow. id = " + id + ", inventoryId = " + inventoryId + ", longField1 = " + longField1 + ", name = " + name + ", position = " + position + ", type = " + type + "}";
    }

}

