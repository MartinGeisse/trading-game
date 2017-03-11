package name.martingeisse.trading_game.game.item;

import com.fasterxml.jackson.annotation.JsonValue;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentSlotType;

/**
 * Item types may provide code for the item behavior (if any) and are referred to by crafting recipes.
 *
 * Each item stack refers to its item type.
 */
public final class ItemType {

	private final String name;
	private final String icon;
	private final int mass;
	private final PlayerShipEquipmentSlotType playerShipEquipmentSlotType;

	public ItemType(String name, String icon, int mass) {
		this(name, icon, mass, null);
	}

	public ItemType(String name, String icon, int mass, PlayerShipEquipmentSlotType playerShipEquipmentSlotType) {
		this.name = name;
		this.icon = icon;
		this.mass = mass;
		this.playerShipEquipmentSlotType = playerShipEquipmentSlotType;
	}

	/**
	 * Getter method.
	 *
	 * @return the name
	 */
	@JsonValue
	public String getName() {
		return name;
	}

	/**
	 * Getter method.
	 *
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Getter method.
	 *
	 * @return the mass
	 */
	public int getMass() {
		return mass;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Getter method.
	 *
	 * @return the playerShipEquipmentSlotType
	 */
	public PlayerShipEquipmentSlotType getPlayerShipEquipmentSlotType() {
		return playerShipEquipmentSlotType;
	}

}
