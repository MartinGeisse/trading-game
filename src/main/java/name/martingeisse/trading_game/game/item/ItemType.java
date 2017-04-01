package name.martingeisse.trading_game.game.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentSlotType;
import name.martingeisse.trading_game.game.player.PlayerAttributeKey;

import java.io.Serializable;
import java.util.Map;

/**
 * Item types may provide code for the item behavior (if any) and are referred to by crafting recipes.
 *
 * Each item stack refers to its item type.
 */
public final class ItemType implements Serializable {

	private final String name;
	private final String icon;
	private final int mass;
	private final PlayerShipEquipmentSlotType playerShipEquipmentSlotType;
	private final ImmutableMap<PlayerAttributeKey, Integer> playerBonus;

	public ItemType(String name, String icon, int mass) {
		this(name, icon, mass, null, null);
	}

	public ItemType(String name, String icon, int mass, PlayerShipEquipmentSlotType playerShipEquipmentSlotType, ImmutableMap<PlayerAttributeKey, Integer> playerBonus) {
		this.name = name;
		this.icon = icon;
		this.mass = mass;
		this.playerShipEquipmentSlotType = playerShipEquipmentSlotType;
		this.playerBonus = playerBonus;
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

	/**
	 * Returns the bonus for the player when this item is equipped.
	 *
	 * @return a mapping of player attribute key to bonus for that attribute, never null map nor null values
	 */
	@JsonIgnore // needed since Jackson gets confused by ImmutableMap, even though it never maps this property
	public ImmutableMap<PlayerAttributeKey, Integer> getPlayerBonus() {
		return (playerBonus == null ? ImmutableMap.of() : playerBonus);
	}

}
