package name.martingeisse.trading_game.game;

import name.martingeisse.trading_game.game.action.PlayerAction;
import name.martingeisse.trading_game.game.action.PlayerActionProgress;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.FixedItemStack;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class Player {

	private final Game game;
	private final String id;
	private String name;
	private final Inventory inventory = new Inventory();
	private final List<PlayerAction> pendingActions = new ArrayList<>();
	private PlayerActionProgress actionProgress;
	private FixedInventory actionItems;

	public Player(Game game, String id) {
		this.game = game;
		this.id = id;
		this.name = "Player " + id;
	}

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter method.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method.
	 *
	 * @param name the name
	 */
	public void setName(String name) throws NameAlreadyUsedException {
		if (name == null) {
			throw new IllegalArgumentException("name cannot be null");
		}
		if (!game.isRenamePossible(this, name)) {
			throw new NameAlreadyUsedException();
		}
		this.name = name;
	}

	/**
	 * Getter method.
	 *
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Getter method.
	 *
	 * @return the pendingActions
	 */
	public List<PlayerAction> getPendingActions() {
		return pendingActions;
	}

	/**
	 * Getter method.
	 *
	 * @return the actionProgress
	 */
	public PlayerActionProgress getActionProgress() {
		return actionProgress;
	}

	/**
	 * Schedules an action to be performed after all currently pending actions.
	 *
	 * @param action the action to schedule
	 */
	public void scheduleAction(PlayerAction action) {
		pendingActions.add(action);
	}

	/**
	 * Cancels the currently executed action (if any).
	 */
	public void cancelCurrentAction() {
		actionProgress.getAction().onCancel();
		actionProgress = null;
	}

	/**
	 * Cancels the pending action at the specified index.
	 *
	 * @param index the index of the action to cancel
	 */
	public void cancelPendingAction(int index) {
		if (index >= 0 && index < pendingActions.size()) {
			pendingActions.remove(index);
		}
	}

	/**
	 * Reserves items from the inventory for an action.
	 *
	 * The BOM must be valid according to {@link FixedInventory#isValidBillOfMaterials()}.
	 *
	 * @throws NotEnoughItemsException if the player doesn't have the required items
	 */
	public void reserveActionItems(FixedInventory billOfMaterials) throws NotEnoughItemsException {
		if (actionItems != null) {
			throw new IllegalStateException("action items already set");
		}
		for (FixedItemStack stack : billOfMaterials.getItemStacks()) {
			if (inventory.count(stack.getItemType()) < stack.getSize()) {
				throw new NotEnoughItemsException();
			}
		}
		for (FixedItemStack stack : billOfMaterials.getItemStacks()) {
			try {
				inventory.remove(stack.getItemType(), stack.getSize());
			} catch (NotEnoughItemsException e) {
				throw new RuntimeException("could not remove items for action -- inventory is not inconsistent");
			}
		}
		this.actionItems = billOfMaterials;
	}

	/**
	 * Puts the current action items back into the inventory.
	 */
	public void putBackActionItems() {
		if (actionItems == null) {
			throw new IllegalStateException("no action items");
		}
		inventory.add(actionItems);
		actionItems = null;
	}

	/**
	 * Consumes the action items.
	 */
	public void consumeActionItems() {
		if (actionItems == null) {
			throw new IllegalStateException("no action items");
		}
		actionItems = null;
	}

	/**
	 * Advances game logic.
	 */
	void tick() {
		while (actionProgress == null && !pendingActions.isEmpty()) {
			PlayerAction action = pendingActions.remove(0);
			if (action.onStart()) {
				actionProgress = new PlayerActionProgress(action);
			}
		}
		if (actionProgress != null) {
			actionProgress.advance(1);
			if (actionProgress.isFinishable()) {
				actionProgress.finish();
				actionProgress = null;
			}
		}
	}

}
