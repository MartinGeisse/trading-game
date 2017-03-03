/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import java.io.Serializable;

/**
 * This class represents rows from table 'PlayerShipEquipmentSlot'.
 */
public class PlayerShipEquipmentSlotRow implements Serializable {

    /**
     * Constructor.
     */
    public PlayerShipEquipmentSlotRow() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the itemType
     */
    private String itemType;

    /**
     * the slotType
     */
    private String slotType;

    /**
     * the spaceObjectBaseDataId
     */
    private Long spaceObjectBaseDataId;

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
     * Getter method for the itemType.
     * 
     * @return the itemType
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Setter method for the itemType.
     * 
     * @param itemType the itemType to set
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * Getter method for the slotType.
     * 
     * @return the slotType
     */
    public String getSlotType() {
        return slotType;
    }

    /**
     * Setter method for the slotType.
     * 
     * @param slotType the slotType to set
     */
    public void setSlotType(String slotType) {
        this.slotType = slotType;
    }

    /**
     * Getter method for the spaceObjectBaseDataId.
     * 
     * @return the spaceObjectBaseDataId
     */
    public Long getSpaceObjectBaseDataId() {
        return spaceObjectBaseDataId;
    }

    /**
     * Setter method for the spaceObjectBaseDataId.
     * 
     * @param spaceObjectBaseDataId the spaceObjectBaseDataId to set
     */
    public void setSpaceObjectBaseDataId(Long spaceObjectBaseDataId) {
        this.spaceObjectBaseDataId = spaceObjectBaseDataId;
    }

    /**
     * Loads the instance with the specified ID.
     * 
     * @param connection the database connection
     * @param id the ID of the instance to load
     * @return the loaded instance
     */
    public static PlayerShipEquipmentSlotRow loadById(PostgresConnection connection, Long id) {
        QPlayerShipEquipmentSlotRow q = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
        return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();
    }

    /**
     * Inserts this instance into the database. This object must not have an ID yet.
     */
    public void insert(PostgresConnection connection) {
        if (id != null) {
        	throw new IllegalStateException("this object already has an id: " + id);
        }
        QPlayerShipEquipmentSlotRow q = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
        SQLInsertClause insert = connection.insert(q);
        insert.set(q.itemType, itemType);
        insert.set(q.slotType, slotType);
        insert.set(q.spaceObjectBaseDataId, spaceObjectBaseDataId);
        id = insert.executeWithKey(Long.class);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PlayerShipEquipmentSlotRow. id = " + id + ", itemType = " + itemType + ", slotType = " + slotType + ", spaceObjectBaseDataId = " + spaceObjectBaseDataId + "}";
    }

}

