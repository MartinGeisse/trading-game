package name.martingeisse.trading_game.game.action.actions;

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

	public UnequipAction(Player player, PlayerShipEquipmentSlotType slotType) {
		this.player = ParameterUtil.ensureNotNull(player, "player");
		this.slotType = ParameterUtil.ensureNotNull(slotType, "slotType");
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
		player.getInventory().add(itemType);
		return true;
	}

	@Override
	public String getName() {
		return "unequip item at slot " + slotType;
	}

}
