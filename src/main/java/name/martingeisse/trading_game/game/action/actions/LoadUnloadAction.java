package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.CannotStartActionException;
import name.martingeisse.trading_game.game.item.ImmutableItemStack;
import name.martingeisse.trading_game.game.item.Inventory;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.GeometryUtil;
import name.martingeisse.trading_game.game.space.SpaceStation;

/**
 *
 */
public final class LoadUnloadAction extends ImmediateAction {

	private final Player player;
	private final SpaceStation spaceStation;
	private final Type type;
	private final ImmutableItemStack items;
	private final int preferredSoruceInventoryIndex;

	public LoadUnloadAction(Player player, SpaceStation spaceStation, Type type, ImmutableItemStack items, int preferredSoruceInventoryIndex) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		if (spaceStation == null) {
			throw new IllegalArgumentException("spaceStation is null");
		}
		if (type == null) {
			throw new IllegalArgumentException("type is null");
		}
		if (items == null) {
			throw new IllegalArgumentException("items is null");
		}
		this.player = player;
		this.spaceStation = spaceStation;
		this.type = type;
		this.items = items;
		this.preferredSoruceInventoryIndex = preferredSoruceInventoryIndex;
	}

	@Override
	public Action getPrerequisite() {
		if (GeometryUtil.isAtSamePosition(player.getShip(), spaceStation)) {
			return null;
		} else {
			return new MoveToPositionAction(player.getShip(), spaceStation.getX(), spaceStation.getY(), player::getShipMovementSpeed);
		}
	}

	@Override
	protected void onExecute() throws CannotStartActionException {
		if (type == Type.LOAD && player.getInventory().getMass() + items.getMass() > player.getMaximumCargoMass()) {
			return;
		}
		Inventory sourceInventory, destinationInventory;
		if (type == Type.LOAD) {
			sourceInventory = spaceStation.getInventory();
			destinationInventory = player.getInventory();
		} else {
			sourceInventory = player.getInventory();
			destinationInventory = spaceStation.getInventory();
		}
		if (!tryRemoveItems(sourceInventory, preferredSoruceInventoryIndex)) {
			if (!tryRemoveItems(sourceInventory, preferredSoruceInventoryIndex - 1)) {
				if (!tryRemoveItems(sourceInventory, preferredSoruceInventoryIndex + 1)) {
					try {
						sourceInventory.remove(items.getItemType(), items.getSize());
					} catch (NotEnoughItemsException e) {
						return;
					}
				}
			}
		}
		destinationInventory.add(items);
	}

	private boolean tryRemoveItems(Inventory sourceInventory, int index) {
		// TODO move this to class Inventory
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
		return true;
	}

	@Override
	public String getName() {
		return type.name().toLowerCase() + " " + items + " at " + spaceStation.getName();
	}

	public enum Type {
		LOAD, UNLOAD;
	}


}
