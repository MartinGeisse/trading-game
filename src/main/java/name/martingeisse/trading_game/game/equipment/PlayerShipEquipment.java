package name.martingeisse.trading_game.game.equipment;

import com.mysema.commons.lang.CloseableIterator;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.PlayerShipEquipmentSlotRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerShipEquipmentSlotRow;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the whole equipment of a player ship. Equipment is managed as a set of typed slots
 * (backed by the database) which contain items matching the slot type.
 */
public final class PlayerShipEquipment {

	private final PostgresService postgresService;
	private final long playerShipId;

	PlayerShipEquipment(PostgresService postgresService, long playerShipId) {
		this.postgresService = postgresService;
		this.playerShipId = playerShipId;
	}

	/**
	 * Gets a mapping of slot type to item type for all slots.
	 */
	public Map<PlayerShipEquipmentSlotType, ItemType> getAllSlots() {
		Map<PlayerShipEquipmentSlotType, ItemType> result = new HashMap<>();
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerShipEquipmentSlotRow qs = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
			try (CloseableIterator<PlayerShipEquipmentSlotRow> iterator = connection.query().select(qs).from(qs).where(qs.spaceObjectBaseDataId.eq(playerShipId)).iterate()){
				while (iterator.hasNext()) {
					PlayerShipEquipmentSlotRow row = iterator.next();
					result.put(row.getSlotType(), row.getItemType());
				}
			}
		}
		return result;
	}

	/**
	 * Gets the item type for a single slot, specified by slot type.
	 */
	public ItemType getSlotItem(PlayerShipEquipmentSlotType slotType) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerShipEquipmentSlotRow qs = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
			return connection.query().select(qs.itemType).from(qs).where(qs.spaceObjectBaseDataId.eq(playerShipId), qs.slotType.eq(slotType).fetchFirst();)

		}

	}

	/**
	 * Puts the specified item type into the corresponding slot. Note that this does not remove the item from whereever
	 * it comes from -- this must be done by the caller!
	 * <p>
	 * Throws an {@link IllegalStateException} if the slot is already used.
	 */
	public void equip(ItemType itemType) {

	}

	/**
	 * Removes the item in the specified slot. Note that this does not add the item to the player's inventory or
	 * anywhere else -- this must be done by the caller!
	 * <p>
	 * Throws an {@link IllegalStateException} if the slot is empty.
	 */
	public void unequip(PlayerShipEquipmentSlotType slotType) {

	}

}
