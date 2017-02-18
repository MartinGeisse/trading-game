/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import java.io.Serializable;

/**
 * This class represents rows from table 'SpaceObjectBaseData'.
 */
public class SpaceObjectBaseData implements Serializable {

    /**
     * Constructor.
     */
    public SpaceObjectBaseData() {
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
     * the type
     */
    private name.martingeisse.trading_game.game.space.SpaceObjectType type;

    /**
     * the x
     */
    private Long x;

    /**
     * the y
     */
    private Long y;

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
     * Getter method for the x.
     * 
     * @return the x
     */
    public Long getX() {
        return x;
    }

    /**
     * Setter method for the x.
     * 
     * @param x the x to set
     */
    public void setX(Long x) {
        this.x = x;
    }

    /**
     * Getter method for the y.
     * 
     * @return the y
     */
    public Long getY() {
        return y;
    }

    /**
     * Setter method for the y.
     * 
     * @param y the y to set
     */
    public void setY(Long y) {
        this.y = y;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{SpaceObjectBaseData. id = " + id + ", inventoryId = " + inventoryId + ", longField1 = " + longField1 + ", name = " + name + ", type = " + type + ", x = " + x + ", y = " + y + "}";
    }

}

