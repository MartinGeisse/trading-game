/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import java.io.Serializable;

/**
 * This class represents rows from table 'InventorySlot'.
 */
public class InventorySlot implements Serializable {

    /**
     * Constructor.
     */
    public InventorySlot() {
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
     * the quantity
     */
    private Integer quantity;

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
     * Getter method for the quantity.
     * 
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Setter method for the quantity.
     * 
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{InventorySlot. id = " + id + ", inventoryId = " + inventoryId + ", quantity = " + quantity + "}";
    }

}

