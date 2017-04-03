package name.martingeisse.trading_game.game.action.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;
import name.martingeisse.trading_game.game.player.Player;

/**
 *
 */
public final class EquipAction extends ImmediateAction {

	private final Player player;
	private final ItemType itemType;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public EquipAction(
			@JsonProperty(value = "player", required = true) Player player,
			@JsonProperty(value = "itemType", required = true) ItemType itemType) {
		this.player = ParameterUtil.ensureNotNull(player, "player");
		this.itemType = ParameterUtil.ensureNotNull(itemType, "itemType");
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
	 * @return the itemType
	 */
	public ItemType getItemType() {
		return itemType;
	}

	@Override
	public Action getPrerequisite() {
		return null;
	}

	@Override
	protected boolean onExecute() {
		// TODO transaction
		try {
			player.getInventory().remove(player.getId(), itemType);
		} catch (NotEnoughItemsException e) {
			return false;
		}
		try {
			player.getEquipment().equip(itemType);
		} catch (RuntimeException e) {
			player.getInventory().add(player.getId(), itemType);
			return false;
		}
		return true;
	}

	@Override
	public String getName() {
		return "equip " + itemType.getName();
	}

}
