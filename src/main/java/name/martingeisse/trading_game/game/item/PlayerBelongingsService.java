package name.martingeisse.trading_game.game.item;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.querydsl.core.Tuple;
import name.martingeisse.trading_game.common.util.CollectionUtils;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.QInventorySlotRow;

import java.util.List;

/**
 * Allows to access cross-inventory belongings of a player.
 */
@Singleton
public class PlayerBelongingsService {

	private final PostgresService postgresService;
	private final JacksonService jacksonService;

	@Inject
	public PlayerBelongingsService(PostgresService postgresService, JacksonService jacksonService) {
		this.postgresService = postgresService;
		this.jacksonService = jacksonService;
	}

	/**
	 * Returns a nested list of all the items that belong to the specified player, no matter in which inventory they
	 * are stored.
	 *
	 * The returned outer list is newly created.
	 */
	public List<InventoryEntry> getBelongingsForPlayerId(long playerId) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QInventorySlotRow qr = QInventorySlotRow.InventorySlot;
			List<Tuple> inventorySlotRows = connection.query().select(qr.inventoryId, qr.itemType, qr.quantity).from(qr).where(qr.playerId.eq(playerId)).fetch();
			List<InventoryEntry> inventoryEntries = CollectionUtils.createGroups(inventorySlotRows, r -> r.get(qr.inventoryId), (inventoryId, slotRows) -> {
				ImmutableList<ImmutableItemStack> stacks = ImmutableList.copyOf(CollectionUtils.map(slotRows, slotRow -> {
					return new ImmutableItemStack(jacksonService.deserialize(slotRow.get(qr.itemType), ItemType.class), slotRow.get(qr.quantity));
				}));
				return new InventoryEntry(inventoryId, new ImmutableItemStacks(stacks));
			});
			return inventoryEntries;
		}
	}

	/**
	 * Represents the items that are stored in a single inventory and owned by the player specified in the query.
	 */
	public static class InventoryEntry {

		private final long inventoryId;
		private final ImmutableItemStacks itemStacks;

		public InventoryEntry(long inventoryId, ImmutableItemStacks itemStacks) {
			this.inventoryId = inventoryId;
			this.itemStacks = itemStacks;
		}

		/**
		 * Getter method.
		 *
		 * @return the inventoryId
		 */
		public long getInventoryId() {
			return inventoryId;
		}

		/**
		 * Getter method.
		 *
		 * @return the itemStacks
		 */
		public ImmutableItemStacks getItemStacks() {
			return itemStacks;
		}

	}

}
