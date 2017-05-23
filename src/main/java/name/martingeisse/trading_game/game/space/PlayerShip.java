package name.martingeisse.trading_game.game.space;

import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionQueueEntry;
import name.martingeisse.trading_game.game.action.actions.MoveToPositionAction;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.item.ObjectWithInventory;
import name.martingeisse.trading_game.game.player.Player;

/**
 *
 */
public final class PlayerShip extends DynamicSpaceObject implements ObjectWithInventory {

	private final EntityProvider entityProvider;
	private long inventoryId;

	public PlayerShip(EntityProvider entityProvider) {
		this.entityProvider = entityProvider;
	}

	/**
	 * Setter method.
	 *
	 * @param inventoryId the inventoryId
	 */
	void internalSetInventoryId(long inventoryId) {
		this.inventoryId = inventoryId;
	}

	/**
	 * Getter method.
	 *
	 * @return the inventoryId
	 */
	@Override
	public long getInventoryId() {
		if (inventoryId < 1) {
			throw new IllegalStateException("inventoryId not initialized");
		}
		return inventoryId;
	}

	/**
	 * Getter method.
	 *
	 * @return the inventory
	 */
	@Override
	public Inventory getInventory() {
		return entityProvider.getInventory(getInventoryId());
	}

	@Override
	public long getRadius() {
		return 500;
	}

	@Override
	public MovementInfo getMovementInfo() {
		Player player = entityProvider.getPlayerByShipId(getId());
		if (player != null) {
			ActionQueueEntry actionQueueEntry = player.getActionQueue().getRunningEntry();
			if (actionQueueEntry != null) {
				Action action = actionQueueEntry.getAction();
				if (action instanceof MoveToPositionAction) {
					MoveToPositionAction moveToPositionAction = (MoveToPositionAction)action;
					// this check ensures that no animation is sent to the browser when the ship reaches its destination,
					// since it's the last position update being sent, but movement has not officially finished yet
					if (!moveToPositionAction.isFinishing()) {
						long destX = moveToPositionAction.getX();
						long destY = moveToPositionAction.getY();
						long speed = moveToPositionAction.getSpeed();
						int remainingTime = action.getRemainingTime();
						return new MovementInfo(destX, destY, speed, remainingTime);
					}
				}
			}
		}
		return null;
	}

}
