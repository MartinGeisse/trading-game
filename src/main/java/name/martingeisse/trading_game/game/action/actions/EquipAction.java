package name.martingeisse.trading_game.game.action.actions;

import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.item.NotEnoughItemsException;
import name.martingeisse.trading_game.game.player.Player;

/**
 *
 * TODO serialization
 *
 */
public final class EquipAction extends ImmediateAction {

	private final Player player;
	private final ItemType itemType;

	public EquipAction(Player player, ItemType itemType) {
		this.player = ParameterUtil.ensureNotNull(player, "player");
		this.itemType = ParameterUtil.ensureNotNull(itemType, "itemType");
	}

	@Override
	public Action getPrerequisite() {
		return null;
	}

	@Override
	protected boolean onExecute() {
		// TODO transaction
		try {
			player.getInventory().remove(itemType);
		} catch (NotEnoughItemsException e) {
			return false;
		}
		try {
			player.getEquipment().equip(itemType);
		} catch (RuntimeException e) {
			player.getInventory().add(itemType);
			return false;
		}
		return true;
	}

	@Override
	public String getName() {
		return "equip " + itemType.getName();
	}

}
