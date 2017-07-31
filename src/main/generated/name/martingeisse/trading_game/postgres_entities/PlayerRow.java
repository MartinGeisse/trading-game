/*
 * This file was generated from the database schema.
 */
package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import java.io.Serializable;

/**
 * This class represents rows from table 'Player'.
 */
public class PlayerRow implements Serializable {

	/**
	 * the actionQueueId
	 */
	private Long actionQueueId;
	/**
	 * the emailAddress
	 */
	private String emailAddress;
	/**
	 * the id
	 */
	private Long id;
	/**
	 * the loginToken
	 */
	private String loginToken;
	/**
	 * the money
	 */
	private Long money;
	/**
	 * the name
	 */
	private String name;
	/**
	 * the remainingPlayTime
	 */
	private Long remainingPlayTime;
	/**
	 * the shipId
	 */
	private Long shipId;
	/**
	 * the spentFoldingCredits
	 */
	private Long spentFoldingCredits;

	/**
	 * Constructor.
	 */
	public PlayerRow() {
	}

	/**
	 * Loads the instance with the specified ID.
	 *
	 * @param connection the database connection
	 * @param id         the ID of the instance to load
	 * @return the loaded instance
	 */
	public static PlayerRow loadById(PostgresConnection connection, Long id) {
		QPlayerRow q = QPlayerRow.Player;
		return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();
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
	 * Getter method for the emailAddress.
	 *
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Setter method for the emailAddress.
	 *
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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
	 * Getter method for the loginToken.
	 *
	 * @return the loginToken
	 */
	public String getLoginToken() {
		return loginToken;
	}

	/**
	 * Setter method for the loginToken.
	 *
	 * @param loginToken the loginToken to set
	 */
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	/**
	 * Getter method for the money.
	 *
	 * @return the money
	 */
	public Long getMoney() {
		return money;
	}

	/**
	 * Setter method for the money.
	 *
	 * @param money the money to set
	 */
	public void setMoney(Long money) {
		this.money = money;
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
	 * Getter method for the remainingPlayTime.
	 *
	 * @return the remainingPlayTime
	 */
	public Long getRemainingPlayTime() {
		return remainingPlayTime;
	}

	/**
	 * Setter method for the remainingPlayTime.
	 *
	 * @param remainingPlayTime the remainingPlayTime to set
	 */
	public void setRemainingPlayTime(Long remainingPlayTime) {
		this.remainingPlayTime = remainingPlayTime;
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

	/**
	 * Getter method for the spentFoldingCredits.
	 *
	 * @return the spentFoldingCredits
	 */
	public Long getSpentFoldingCredits() {
		return spentFoldingCredits;
	}

	/**
	 * Setter method for the spentFoldingCredits.
	 *
	 * @param spentFoldingCredits the spentFoldingCredits to set
	 */
	public void setSpentFoldingCredits(Long spentFoldingCredits) {
		this.spentFoldingCredits = spentFoldingCredits;
	}

	/**
	 * Inserts this instance into the database. This object must not have an ID yet.
	 */
	public void insert(PostgresConnection connection) {
		if (id != null) {
			throw new IllegalStateException("this object already has an id: " + id);
		}
		QPlayerRow q = QPlayerRow.Player;
		SQLInsertClause insert = connection.insert(q);
		insert.set(q.actionQueueId, actionQueueId);
		insert.set(q.emailAddress, emailAddress);
		insert.set(q.loginToken, loginToken);
		insert.set(q.money, money);
		insert.set(q.name, name);
		insert.set(q.remainingPlayTime, remainingPlayTime);
		insert.set(q.shipId, shipId);
		insert.set(q.spentFoldingCredits, spentFoldingCredits);
		id = insert.executeWithKey(Long.class);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{PlayerRow. actionQueueId = " + actionQueueId + ", emailAddress = " + emailAddress + ", id = " + id + ", loginToken = " + loginToken + ", money = " + money + ", name = " + name + ", remainingPlayTime = " + remainingPlayTime + ", shipId = " + shipId + ", spentFoldingCredits = " + spentFoldingCredits + "}";
	}

}

