/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import java.io.Serializable;

/**
 * This class represents rows from table 'Player'.
 */
public class Player implements Serializable {

    /**
     * Constructor.
     */
    public Player() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the name
     */
    private String name;

    /**
     * the shipId
     */
    private Long shipId;

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
     * Getter method for the shipId.
     * 
     * @return the shipId
     */
    public Long getShipId() {
        return shipId;
    }

    /**
     * Setter method for the shipId.
     * 
     * @param shipId the shipId to set
     */
    public void setShipId(Long shipId) {
        this.shipId = shipId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Player. id = " + id + ", name = " + name + ", shipId = " + shipId + "}";
    }

}

