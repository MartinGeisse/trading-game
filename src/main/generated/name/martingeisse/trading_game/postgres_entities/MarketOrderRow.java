/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import java.io.Serializable;

/**
 * This class represents rows from table 'MarketOrder'.
 */
public class MarketOrderRow implements Serializable {

	/**
	 * the id
	 */
	private Long id;
	/**
	 * the itemType
	 */
	private String itemType;
	/**
	 * the locationSpaceObjectBaseDataId
	 */
	private Long locationSpaceObjectBaseDataId;
	/**
	 * the principalPlayerId
	 */
	private Long principalPlayerId;
	/**
	 * the quantity
	 */
	private Integer quantity;
	/**
	 * the type
	 */
	private name.martingeisse.trading_game.game.market.MarketOrderType type;
	/**
	 * the unitPrice
	 */
	private Long unitPrice;

	/**
	 * Constructor.
	 */
	public MarketOrderRow() {
	}

	/**
	 * Loads the instance with the specified ID.
	 *
	 * @param connection the database connection
	 * @param id         the ID of the instance to load
	 * @return the loaded instance
	 */
	public static MarketOrderRow loadById(PostgresConnection connection, Long id) {
		QMarketOrderRow q = QMarketOrderRow.MarketOrder;
		return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();
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
	 * Getter method for the locationSpaceObjectBaseDataId.
	 *
	 * @return the locationSpaceObjectBaseDataId
	 */
	public Long getLocationSpaceObjectBaseDataId() {
		return locationSpaceObjectBaseDataId;
	}

	/**
	 * Setter method for the locationSpaceObjectBaseDataId.
	 *
	 * @param locationSpaceObjectBaseDataId the locationSpaceObjectBaseDataId to set
	 */
	public void setLocationSpaceObjectBaseDataId(Long locationSpaceObjectBaseDataId) {
		this.locationSpaceObjectBaseDataId = locationSpaceObjectBaseDataId;
	}

	/**
	 * Getter method for the principalPlayerId.
	 *
	 * @return the principalPlayerId
	 */
	public Long getPrincipalPlayerId() {
		return principalPlayerId;
	}

	/**
	 * Setter method for the principalPlayerId.
	 *
	 * @param principalPlayerId the principalPlayerId to set
	 */
	public void setPrincipalPlayerId(Long principalPlayerId) {
		this.principalPlayerId = principalPlayerId;
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
	 * Getter method for the type.
	 *
	 * @return the type
	 */
	public name.martingeisse.trading_game.game.market.MarketOrderType getType() {
		return type;
	}

	/**
	 * Setter method for the type.
	 *
	 * @param type the type to set
	 */
	public void setType(name.martingeisse.trading_game.game.market.MarketOrderType type) {
		this.type = type;
	}

	/**
	 * Getter method for the unitPrice.
	 *
	 * @return the unitPrice
	 */
	public Long getUnitPrice() {
		return unitPrice;
	}

	/**
	 * Setter method for the unitPrice.
	 *
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * Inserts this instance into the database. This object must not have an ID yet.
	 */
	public void insert(PostgresConnection connection) {
		if (id != null) {
			throw new IllegalStateException("this object already has an id: " + id);
		}
		QMarketOrderRow q = QMarketOrderRow.MarketOrder;
		SQLInsertClause insert = connection.insert(q);
		insert.set(q.itemType, itemType);
		insert.set(q.locationSpaceObjectBaseDataId, locationSpaceObjectBaseDataId);
		insert.set(q.principalPlayerId, principalPlayerId);
		insert.set(q.quantity, quantity);
		insert.set(q.type, type);
		insert.set(q.unitPrice, unitPrice);
		id = insert.executeWithKey(Long.class);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{MarketOrderRow. id = " + id + ", itemType = " + itemType + ", locationSpaceObjectBaseDataId = " + locationSpaceObjectBaseDataId + ", principalPlayerId = " + principalPlayerId + ", quantity = " + quantity + ", type = " + type + ", unitPrice = " + unitPrice + "}";
	}

}

