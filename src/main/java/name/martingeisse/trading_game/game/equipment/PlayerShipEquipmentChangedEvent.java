package name.martingeisse.trading_game.game.equipment;

import name.martingeisse.trading_game.game.event.GameEvent;

/**
 * Indicates that a player's ship equipment has changed.
 */
public final class PlayerShipEquipmentChangedEvent extends GameEvent {

	private final long playerShipId;

	/**
	 * Constructor.
	 *
	 * @param playerShipId the ID of the ship whose equipment has changed
	 */
	public PlayerShipEquipmentChangedEvent(long playerShipId) {
		this.playerShipId = playerShipId;
	}

	/**
	 * Getter method.
	 *
	 * @return the playerShipId
	 */
	public long getPlayerShipId() {
		return playerShipId;
	}

}
