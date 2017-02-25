package name.martingeisse.trading_game.game.item;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.Path;
import name.martingeisse.trading_game.common.util.WtfException;
import name.martingeisse.trading_game.game.event.GameEventEmitter;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.InventorySlotRow;
import name.martingeisse.trading_game.postgres_entities.QInventorySlotRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds a mutable, persistent collection of item stacks.
 */
public final class Inventory {

	private final PostgresService postgresService;
	private final ItemTypeSerializer itemTypeSerializer;
	private final GameEventEmitter gameEventEmitter;
	private final long id;

	// use InventoryRepository to get an instance of this class
	Inventory(PostgresService postgresService, ItemTypeSerializer itemTypeSerializer, GameEventEmitter gameEventEmitter, long id) {
		this.postgresService = postgresService;
		this.itemTypeSerializer = itemTypeSerializer;
		this.gameEventEmitter = gameEventEmitter;
		this.id = id;
	}

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return all items
	 */
	public ImmutableItemStacks getItems() {
		List<ImmutableItemStack> stacks = new ArrayList<>();
		try (PostgresConnection connection = postgresService.newConnection()) {
			QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
			try (CloseableIterator<InventorySlotRow> iterator = connection.query().select(qs).from(qs).where(qs.inventoryId.eq(id)).iterate()) {
				while (iterator.hasNext()) {
					InventorySlotRow row = iterator.next();
					stacks.add(new ImmutableItemStack(itemTypeSerializer.deserializeItemType(row.getItemType()), row.getQuantity()));
				}
			}
		}
		return ImmutableItemStacks.fromFixedItemStacks(stacks);
	}

	/**
	 * @return the number of item stacks
	 */
	public int getNumberOfStacks() {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
			return (int) connection.query().select(qs).from(qs).where(qs.inventoryId.eq(id)).fetchCount();
		}
	}

	/**
	 * Calculates the total mass of this inventory.
	 *
	 * @return the mass
	 */
	public int getMass() {
		int result = 0;
		for (ImmutableItemStack itemStack : getItems()) {
			result += itemStack.getMass();
		}
		return result;
	}

	/**
	 * Counts the items of the specified type.
	 */
	public int count(ItemType itemType) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
			String serializedItemType = itemTypeSerializer.serializeItemType(itemType);
			return (int) connection.query().select(qs.quantity.sum()).from(qs).where(qs.inventoryId.eq(id), qs.itemType.eq(serializedItemType)).fetchCount();
		}
	}

	public Inventory add(ItemType itemType) {
		add(itemType, 1);
		return this;
	}

	public Inventory add(ImmutableItemStacks items) {
		for (ImmutableItemStack stack : items.getStacks()) {
			add(stack);
		}
		return this;
	}

	public Inventory add(ImmutableItemStack stack) {
		return add(stack.getItemType(), stack.getSize());
	}

	public Inventory add(ItemType itemType, int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("amount is negative");
		}
		if (amount == 0) {
			return this;
		}
		Long slotId = findSlotId(itemType);
		QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
		try (PostgresConnection connection = postgresService.newConnection()) {
			if (slotId == null) {
				String serializedItemType = itemTypeSerializer.serializeItemType(itemType);
				connection.insert(qs).set(qs.inventoryId, id).set(qs.itemType, serializedItemType).set(qs.quantity, amount).execute();
			} else {
				connection.update(qs).set(qs.quantity, qs.quantity.add(amount)).where(qs.id.eq(slotId)).execute();
			}
		}
		gameEventEmitter.emit(new InventoryChangedEvent(id));
		return this;
	}

	public Inventory remove(ItemType itemType) throws NotEnoughItemsException {
		remove(itemType, 1);
		return this;
	}

	public Inventory remove(ItemType itemType, int amount) throws NotEnoughItemsException {

		// first check if there are enough items, so we don't mess up the item stacks if not
		if (count(itemType) < amount) {
			throw new NotEnoughItemsException();
		}

		// there are enough items, so remove stacks (and possibly one partial stack) until the amount is reached
		while (amount > 0) {
			Long slotId = findSlotId(itemType);
			if (slotId == null) {
				throw new WtfException("did not find enough items despite checking first");
			}
			InventorySlotRow slot = findSlot(itemType);
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

	private Long findSlotId(ItemType itemType) {
		return findSlot(itemType, QInventorySlotRow.InventorySlot.id);
	}

	private InventorySlotRow findSlot(ItemType itemType) {
		return findSlot(itemType, QInventorySlotRow.InventorySlot);
	}

	private <T> T findSlot(ItemType itemType, Path<T> path) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			String serializedItemType = itemTypeSerializer.serializeItemType(itemType);
			QInventorySlotRow qs = QInventorySlotRow.InventorySlot;
			return connection.query().select(path).from(qs).where(qs.inventoryId.eq(id), qs.itemType.eq(serializedItemType)).fetchFirst();
		}
	}

	/**
	 * Removes items of multiple types, assuming that the argument is a valid bill of materials according to
	 * {@link ImmutableItemStacks#isValidBillOfMaterials()}. Does not remove anything if any items are missing.
	 *
	 * @throws NotEnoughItemsException if the player doesn't have the required items
	 */
	public void removeBillOfMaterials(ImmutableItemStacks billOfMaterials) throws NotEnoughItemsException {
		for (ImmutableItemStack stack : billOfMaterials.getStacks()) {
			if (count(stack.getItemType()) < stack.getSize()) {
				throw new NotEnoughItemsException();
			}
		}
		for (ImmutableItemStack stack : billOfMaterials.getStacks()) {
			try {
				remove(stack.getItemType(), stack.getSize());
			} catch (NotEnoughItemsException e) {
				throw new RuntimeException("could not remove items for action -- inventory is now inconsistent");
			}
		}
	}

}
