package name.martingeisse.trading_game.game.equipment;

import com.google.inject.Injector;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.QueryException;
import name.martingeisse.trading_game.common.database.DatabaseUtil;
import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.item.ItemTypeSerializer;
import name.martingeisse.trading_game.platform.application.CommandLineApplicationBootstrapper;
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
	private final ItemTypeSerializer itemTypeSerializer;
	private final long playerShipId;

	PlayerShipEquipment(PostgresService postgresService, ItemTypeSerializer itemTypeSerializer, long playerShipId) {
		this.postgresService = ParameterUtil.ensureNotNull(postgresService, "postgresService");
		this.itemTypeSerializer = ParameterUtil.ensureNotNull(itemTypeSerializer, "itemTypeSerializer");
		this.playerShipId = ParameterUtil.ensurePositive(playerShipId, "playerShipId");
	}

	/**
	 * Gets a mapping of slot type to item type for all slots.
	 */
	public Map<PlayerShipEquipmentSlotType, ItemType> getAllSlots() {
		Map<PlayerShipEquipmentSlotType, ItemType> result = new HashMap<>();
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerShipEquipmentSlotRow qs = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
			try (CloseableIterator<PlayerShipEquipmentSlotRow> iterator = connection.query().select(qs).from(qs).where(qs.spaceObjectBaseDataId.eq(playerShipId)).orderBy(qs.slotType.asc()).iterate()) {
				while (iterator.hasNext()) {
					PlayerShipEquipmentSlotRow row = iterator.next();
					result.put(row.getSlotType(), itemTypeSerializer.deserializeItemType(row.getItemType()));
				}
			}
		}
		return result;
	}

	/**
	 * Gets the item type for a single slot, specified by slot type.
	 */
	public ItemType getSlotItem(PlayerShipEquipmentSlotType slotType) {
		ParameterUtil.ensureNotNull(slotType, "slotType");
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerShipEquipmentSlotRow qs = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
			return itemTypeSerializer.deserializeItemType(connection.query().select(qs.itemType).from(qs).where(qs.spaceObjectBaseDataId.eq(playerShipId), qs.slotType.eq(slotType)).fetchFirst());
		}
	}

	/**
	 * Puts the specified item type into the corresponding slot. Note that this does not remove the item from whereever
	 * it comes from -- this must be done by the caller!
	 * <p>
	 * Throws an {@link IllegalStateException} if the slot is already used.
	 */
	public void equip(ItemType itemType) {
		PlayerShipEquipmentSlotType slotType = itemType.getPlayerShipEquipmentSlotType();
		if (slotType == null) {
			throw new RuntimeException("cannot equip item: " + itemType);
		}
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerShipEquipmentSlotRow qs = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
			String serializedItemType = itemTypeSerializer.serializeItemType(itemType);
			try {
				connection.insert(qs).set(qs.spaceObjectBaseDataId, playerShipId).set(qs.slotType, slotType).set(qs.itemType, serializedItemType).execute();
			} catch (QueryException e) {
				if (DatabaseUtil.isDuplicateKeyViolation(e)) {
					throw new RuntimeException("this player already has an item of type " + slotType + " equipped");
				} else {
					throw e;
				}
			}
		}
	}

	/**
	 * Removes the item in the specified slot. Note that this does not add the item to the player's inventory or
	 * anywhere else -- this must be done by the caller!
	 * <p>
	 * Throws an {@link IllegalStateException} if the slot is empty.
	 */
	public void unequip(PlayerShipEquipmentSlotType slotType) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QPlayerShipEquipmentSlotRow qs = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
			if (connection.delete(qs).where(qs.spaceObjectBaseDataId.eq(playerShipId), qs.slotType.eq(slotType)).execute() == 0) {
				throw new RuntimeException("this player has no item of type " + slotType + " equipped");
			}
		}
	}

}
