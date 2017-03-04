package name.martingeisse.trading_game.game.equipment;

import name.martingeisse.trading_game.game.item.ItemType;

import java.util.Map;

/**
 *
 */
public final class SlotInfo {

	private final PlayerShipEquipmentSlotType playerShipEquipmentSlotType;
	private final ItemType itemType;

	/**
	 * Constructor.
	 *
	 * @param playerShipEquipmentSlotType
	 * @param itemType
	 */
	SlotInfo(PlayerShipEquipmentSlotType playerShipEquipmentSlotType, ItemType itemType) {
		this.playerShipEquipmentSlotType = playerShipEquipmentSlotType;
		this.itemType = itemType;
	}

	/**
	 * Getter method.
	 *
	 * @return the playerShipEquipmentSlotType
	 */
	public PlayerShipEquipmentSlotType getPlayerShipEquipmentSlotType() {
		return playerShipEquipmentSlotType;
	}

	/**
	 * Getter method.
	 *
	 * @return the itemType
	 */
	public ItemType getItemType() {
		return itemType;
	}

}
