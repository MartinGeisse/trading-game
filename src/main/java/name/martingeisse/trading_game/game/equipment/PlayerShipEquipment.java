package name.martingeisse.trading_game.game.equipment;

import com.google.common.collect.ImmutableList;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.QueryException;
import name.martingeisse.trading_game.common.database.DatabaseUtil;
import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.PlayerShipEquipmentSlotRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerShipEquipmentSlotRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the whole equipment of a player ship. Equipment is managed as a set of typed slots
 * (backed by the database) which contain items matching the slot type.
 */
public final class PlayerShipEquipment {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;
	private final EntityProvider entityProvider;
	private final GameEventEmitter gameEventEmitter;
	private final long playerShipId;

	public PlayerShipEquipment(PostgresContextService postgresContextService, JacksonService jacksonService, EntityProvider entityProvider, GameEventEmitter gameEventEmitter, long playerShipId) {
		this.postgresContextService = ParameterUtil.ensureNotNull(postgresContextService, "postgresContextService");
		this.jacksonService = ParameterUtil.ensureNotNull(jacksonService, "jacksonService");
		this.entityProvider = entityProvider;
		this.gameEventEmitter = gameEventEmitter;
		this.playerShipId = ParameterUtil.ensurePositive(playerShipId, "playerShipId");
	}

	/**
	 * Gets a list of all slots.
	 */
	public ImmutableList<SlotInfo> getAllSlots() {
		List<SlotInfo> result = new ArrayList<>();
		QPlayerShipEquipmentSlotRow qs = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
		try (CloseableIterator<PlayerShipEquipmentSlotRow> iterator = postgresContextService.select(qs).from(qs).where(qs.spaceObjectBaseDataId.eq(playerShipId)).orderBy(qs.slotType.asc()).iterate()) {
			while (iterator.hasNext()) {
				PlayerShipEquipmentSlotRow row = iterator.next();
				result.add(new SlotInfo(row.getSlotType(), jacksonService.deserialize(row.getItemType(), ItemType.class)));
			}
		}
		return ImmutableList.copyOf(result);
	}

	/**
	 * Gets the item type for a single slot, specified by slot type.
	 */
	public ItemType getSlotItem(PlayerShipEquipmentSlotType slotType) {
		ParameterUtil.ensureNotNull(slotType, "slotType");
		QPlayerShipEquipmentSlotRow qs = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
		return jacksonService.deserialize(postgresContextService.select(qs.itemType).from(qs).where(qs.spaceObjectBaseDataId.eq(playerShipId), qs.slotType.eq(slotType)).fetchFirst(), ItemType.class);
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
		QPlayerShipEquipmentSlotRow qs = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
		String serializedItemType = jacksonService.serialize(itemType);
		try {
			postgresContextService.insert(qs).set(qs.spaceObjectBaseDataId, playerShipId).set(qs.slotType, slotType).set(qs.itemType, serializedItemType).execute();
		} catch (QueryException e) {
			if (DatabaseUtil.isDuplicateKeyViolation(e)) {
				throw new RuntimeException("this player already has an item of type " + slotType + " equipped");
			} else {
				throw e;
			}
		}
		updateAttributes();
		gameEventEmitter.emit(new PlayerShipEquipmentChangedEvent(playerShipId));
	}

	/**
	 * Removes the item in the specified slot. Note that this does not add the item to the player's inventory or
	 * anywhere else -- this must be done by the caller!
	 * <p>
	 * Throws an {@link IllegalStateException} if the slot is empty.
	 */
	public void unequip(PlayerShipEquipmentSlotType slotType) {
		QPlayerShipEquipmentSlotRow qs = QPlayerShipEquipmentSlotRow.PlayerShipEquipmentSlot;
		if (postgresContextService.delete(qs).where(qs.spaceObjectBaseDataId.eq(playerShipId), qs.slotType.eq(slotType)).execute() == 0) {
			throw new RuntimeException("this player has no item of type " + slotType + " equipped");
		}
		updateAttributes();
		gameEventEmitter.emit(new PlayerShipEquipmentChangedEvent(playerShipId));
	}

	private void updateAttributes() {
		entityProvider.getPlayerByShipId(playerShipId).updateAttributes();
	}

}
