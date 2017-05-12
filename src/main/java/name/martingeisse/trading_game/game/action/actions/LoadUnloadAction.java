package name.martingeisse.trading_game.game.action.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import name.martingeisse.trading_game.game.action.Action;
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
	private final int preferredSourceInventoryIndex;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public LoadUnloadAction(
			@JsonProperty(value = "player", required = true) Player player,
			@JsonProperty(value = "spaceStation", required = true) SpaceStation spaceStation,
			@JsonProperty(value = "type", required = true) Type type,
			@JsonProperty(value = "items", required = true) ImmutableItemStack items,
			@JsonProperty(value = "preferredSourceInventoryIndex", required = true) int preferredSourceInventoryIndex) {
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
		this.preferredSourceInventoryIndex = preferredSourceInventoryIndex;
	}

	/**
	 * Getter method.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Getter method.
	 *
	 * @return the spaceStation
	 */
	public SpaceStation getSpaceStation() {
		return spaceStation;
	}

	/**
	 * Getter method.
	 *
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Getter method.
	 *
	 * @return the items
	 */
	public ImmutableItemStack getItems() {
		return items;
	}

	/**
	 * Getter method.
	 *
	 * @return the preferredSourceInventoryIndex
	 */
	public int getPreferredSourceInventoryIndex() {
		return preferredSourceInventoryIndex;
	}

	@Override
	public Action getPrerequisite() {
		if (GeometryUtil.isAtSamePosition(player.getShip(), spaceStation)) {
			return null;
		} else {
			return new MoveToPositionAction(player, spaceStation.getX(), spaceStation.getY());
		}
	}

	@Override
	protected boolean onExecute() {
		if (type == Type.LOAD && player.getInventory().getMass(player.getId()) + items.getMass() > player.getMaximumCargoMass()) {
			return false;
		}
		Inventory sourceInventory, destinationInventory;
		if (type == Type.LOAD) {
			sourceInventory = spaceStation.getInventory();
			destinationInventory = player.getInventory();
		} else {
			sourceInventory = player.getInventory();
			destinationInventory = spaceStation.getInventory();
		}
		try {
			sourceInventory.remove(player.getId(), items.getItemType(), items.getSize(), preferredSourceInventoryIndex);
		} catch (NotEnoughItemsException e) {
			return false;
		}
		destinationInventory.add(player.getId(), items);
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
