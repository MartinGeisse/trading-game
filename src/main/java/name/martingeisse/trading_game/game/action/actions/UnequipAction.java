package name.martingeisse.trading_game.game.action.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentSlotType;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.player.Player;

/**
 * TODO serialization
 */
public final class UnequipAction extends ImmediateAction {

	private final Player player;
	private final PlayerShipEquipmentSlotType slotType;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public UnequipAction(
			@JsonProperty(value = "player", required = true) Player player,
			@JsonProperty(value = "slotType", required = true) PlayerShipEquipmentSlotType slotType) {
		this.player = ParameterUtil.ensureNotNull(player, "player");
		this.slotType = ParameterUtil.ensureNotNull(slotType, "slotType");
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
	 * @return the slotType
	 */
	public PlayerShipEquipmentSlotType getSlotType() {
		return slotType;
	}

	@Override
	public Action getPrerequisite() {
		return null;
	}

	@Override
	protected boolean onExecute() {
		// TODO transaction; max inventory mass
		ItemType itemType = player.getEquipment().getSlotItem(slotType);
		try {
			player.getEquipment().unequip(slotType);
		} catch (RuntimeException e) {
			return false;
		}
		player.getInventory().add(player.getId(), itemType);
		return true;
	}

	@Override
	public String getName() {
		return "unequip item at slot " + slotType;
	}

}
