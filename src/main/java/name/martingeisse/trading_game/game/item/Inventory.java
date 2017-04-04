package name.martingeisse.trading_game.game.item;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.postgresql.PostgreSQLQuery;
import name.martingeisse.trading_game.common.util.WtfException;
import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.InventorySlotRow;
import name.martingeisse.trading_game.postgres_entities.QInventorySlotRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a mutable, persistent collection of item stacks.
 * <p>
 * Note: This class should be split: item storage location; items at one location belonging to a single player; mining yield template
 */
public final class Inventory {

	private final PostgresService postgresService;
	private final JacksonService jacksonService;
	private final GameEventEmitter gameEventEmitter;
	private final long id;

	// use InventoryRepository to get an instance of this class
	public Inventory(PostgresService postgresService, JacksonService jacksonService, GameEventEmitter gameEventEmitter, long id) {
		this.postgresService = postgresService;
		this.jacksonService = jacksonService;
		this.gameEventEmitter = gameEventEmitter;
		this.id = id;
	}

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	@JsonValue
	public long getId() {
		return id;
	}

	/**
	 * @return all items
	 */
	public ImmutableItemStacks getItems(Long playerIdFilter) {
		List<ImmutableItemStack> stacks = new ArrayList<>();
		try (PostgresConnection connection = postgresService.newConnection()) {
			QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
			PostgreSQLQuery<InventorySlotRow> query = connection.query().select(qs).from(qs).where(qs.inventoryId.eq(id));
			try (CloseableIterator<InventorySlotRow> iterator = query.iterate()) {
				while (iterator.hasNext()) {
					InventorySlotRow row = iterator.next();
					stacks.add(new ImmutableItemStack(jacksonService.deserialize(row.getItemType(), ItemType.class), row.getQuantity()));
				}
			}
		}
		return ImmutableItemStacks.fromFixedItemStacks(stacks);
	}

	/**
	 * @return the number of item stacks
	 */
	public int getNumberOfStacks(Long playerIdFilter) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
			PostgreSQLQuery<InventorySlotRow> query = connection.query().select(qs).from(qs).where(qs.inventoryId.eq(id));
			if (playerIdFilter != null) {
				query.where(qs.playerId.eq(playerIdFilter));
			}
			return (int) query.fetchCount();
		}
	}

	/**
	 * Calculates the total mass of this inventory.
	 *
	 * @return the mass
	 */
	public int getMass(Long playerIdFilter) {
		int result = 0;
		for (ImmutableItemStack itemStack : getItems(playerIdFilter)) {
			result += itemStack.getMass();
		}
		return result;
	}

	/**
	 * Counts the items of the specified type.
	 */
	public int count(ItemType itemType, Long playerIdFilter) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
			String serializedItemType = jacksonService.serialize(itemType);
			PostgreSQLQuery<Integer> query = connection.query().select(qs.quantity.sum()).from(qs).where(qs.inventoryId.eq(id), qs.itemType.eq(serializedItemType)).groupBy(Expressions.constant(0));
			if (playerIdFilter != null) {
				query.where(qs.playerId.eq(playerIdFilter));
			}
			return query.fetchFirst();
		}
	}

	public Inventory add(long playerId, ItemType itemType) {
		ParameterUtil.ensureNotNull(itemType, "itemType");
		add(playerId, itemType, 1);
		return this;
	}

	public Inventory add(long playerId, ImmutableItemStacks items) {
		ParameterUtil.ensureNotNull(items, "items");
		for (ImmutableItemStack stack : items.getStacks()) {
			add(playerId, stack);
		}
		return this;
	}

	public Inventory add(long playerId, ImmutableItemStack stack) {
		ParameterUtil.ensureNotNull(stack, "stack");
		return add(playerId, stack.getItemType(), stack.getSize());
	}

	public Inventory add(long playerId, ItemType itemType, int amount) {
		ParameterUtil.ensureNotNull(itemType, "itemType");
		ParameterUtil.ensureNotNegative(amount, "amount");
		if (amount == 0) {
			return this;
		}
		Long slotId = findSlotId(playerId, itemType);
		QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
		try (PostgresConnection connection = postgresService.newConnection()) {
			if (slotId == null) {
				String serializedItemType = jacksonService.serialize(itemType);
				connection.insert(qs).set(qs.inventoryId, id).set(qs.playerId, playerId).set(qs.itemType, serializedItemType).set(qs.quantity, amount).execute();
			} else {
				connection.update(qs).set(qs.quantity, qs.quantity.add(amount)).where(qs.id.eq(slotId)).execute();
			}
		}
		gameEventEmitter.emit(new InventoryChangedEvent(id));
		return this;
	}

	public Inventory remove(long playerId, ItemType itemType) throws NotEnoughItemsException {
		ParameterUtil.ensureNotNull(itemType, "itemType");
		remove(playerId, itemType, 1);
		return this;
	}

	public Inventory remove(long playerId, ItemType itemType, int amount, int preferredIndex) throws NotEnoughItemsException {
		ParameterUtil.ensureNotNull(itemType, "itemType");
		ParameterUtil.ensureNotNegative(amount, "amount");
		ParameterUtil.ensureNotNegative(preferredIndex, "preferredIndex");

		// TODO implement. For now, just take the items from any slot with the right item type
		remove(playerId, itemType, amount);
		return this;

//		if (index < 0 || index >= sourceInventory.getItemStacks().size()) {
//			return false;
//		}
//		ItemStack foundItemStack = sourceInventory.getItemStacks().get(index);
//		if (foundItemStack.getItemType() != items.getItemType()) {
//			return false;
//		}
//		if (foundItemStack.getSize() < items.getSize()) {
//			return false;
//		}
//		if (foundItemStack.getSize() == items.getSize()) {
//			sourceInventory.getItemStacks().remove(index);
//			return true;
//		} else {
//			try {
//				foundItemStack.remove(items.getSize());
//			} catch (NotEnoughItemsException e) {
//				throw new WtfException(e);
//			}
//			return true;
//		}

	}

	public Inventory remove(long playerId, ItemType itemType, int amount) throws NotEnoughItemsException {
		ParameterUtil.ensureNotNull(itemType, "itemType");
		ParameterUtil.ensureNotNegative(amount, "amount");

		// first check if there are enough items, so we don't mess up the item stacks if not
		if (count(itemType, playerId) < amount) {
			throw new NotEnoughItemsException();
		}

		// there are enough items, so remove stacks (and possibly one partial stack) until the amount is reached
		while (amount > 0) {
			Long slotId = findSlotId(playerId, itemType);
			if (slotId == null) {
				throw new WtfException("did not find enough items despite checking first");
			}
			InventorySlotRow slot = findSlot(playerId, itemType);
			try (PostgresConnection connection = postgresService.newConnection()) {
				QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
				if (slot.getQuantity() > amount) {
					connection.update(qs).set(qs.quantity, qs.quantity.subtract(amount)).where(qs.id.eq(slot.getId())).execute();
					break;
				} else {
					amount -= slot.getQuantity();
					connection.delete(qs).where(qs.id.eq(slot.getId())).execute();
					if (amount <= 0) {
						break;
					}
				}
			}
		}

		gameEventEmitter.emit(new InventoryChangedEvent(id));
		return this;
	}

	private Long findSlotId(long playerId, ItemType itemType) {
		ParameterUtil.ensureNotNull(itemType, "itemType");
		return findSlot(playerId, itemType, QInventorySlotRow.InventorySlot.id);
	}

	private InventorySlotRow findSlot(long playerId, ItemType itemType) {
		ParameterUtil.ensureNotNull(itemType, "itemType");
		return findSlot(playerId, itemType, QInventorySlotRow.InventorySlot);
	}

	private <T> T findSlot(long playerId, ItemType itemType, Path<T> path) {
		ParameterUtil.ensureNotNull(itemType, "itemType");
		ParameterUtil.ensureNotNull(path, "path");
		try (PostgresConnection connection = postgresService.newConnection()) {
			String serializedItemType = jacksonService.serialize(itemType);
			QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
			PostgreSQLQuery<T> query = connection.query().select(path).from(qs).where(qs.inventoryId.eq(id), qs.itemType.eq(serializedItemType), qs.playerId.eq(playerId));
			return query.fetchFirst();
		}
	}

	/**
	 * Removes items of multiple types, assuming that the argument is a valid bill of materials according to
	 * {@link ImmutableItemStacks#isValidBillOfMaterials()}. Does not remove anything if any items are missing.
	 *
	 * @throws NotEnoughItemsException if the player doesn't have the required items
	 */
	public void removeBillOfMaterials(long playerId, ImmutableItemStacks billOfMaterials) throws NotEnoughItemsException {
		ParameterUtil.ensureNotNull(billOfMaterials, "billOfMaterials");
		for (ImmutableItemStack stack : billOfMaterials.getStacks()) {
			if (count(stack.getItemType(), playerId) < stack.getSize()) {
				throw new NotEnoughItemsException();
			}
		}
		for (ImmutableItemStack stack : billOfMaterials.getStacks()) {
			try {
				remove(playerId, stack.getItemType(), stack.getSize());
			} catch (NotEnoughItemsException e) {
				throw new RuntimeException("could not remove items for action -- inventory is now inconsistent");
			}
		}
	}

}
