/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import java.io.Serializable;

/**
 * This class represents rows from table 'InventorySlot'.
 */
public class InventorySlotRow implements Serializable {

	/**
	 * Constructor.
	 */
	public InventorySlotRow() {
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
	 * the itemType
	 */
	private String itemType;

	/**
	 * the playerId
	 */
	private Long playerId;

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

	/**
	 * Loads the instance with the specified ID.
	 *
	 * @param connection the database connection
	 * @param id         the ID of the instance to load
	 * @return the loaded instance
	 */
	public static InventorySlotRow loadById(PostgresConnection connection, Long id) {
		QInventorySlotRow q = QInventorySlotRow.InventorySlot;
		return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();
	}

	/**
	 * Inserts this instance into the database. This object must not have an ID yet.
	 */
	public void insert(PostgresConnection connection) {
		if (id != null) {
			throw new IllegalStateException("this object already has an id: " + id);
		}
		QInventorySlotRow q = QInventorySlotRow.InventorySlot;
		SQLInsertClause insert = connection.insert(q);
		insert.set(q.inventoryId, inventoryId);
		insert.set(q.itemType, itemType);
		insert.set(q.playerId, playerId);
		insert.set(q.quantity, quantity);
		id = insert.executeWithKey(Long.class);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{InventorySlotRow. id = " + id + ", inventoryId = " + inventoryId + ", itemType = " + itemType + ", playerId = " + playerId + ", quantity = " + quantity + "}";
	}

}

