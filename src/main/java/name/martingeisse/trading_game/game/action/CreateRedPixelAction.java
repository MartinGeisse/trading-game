package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.item.ItemType;

/**
 *
 */
public final class CreateRedPixelAction extends PlayerAction {

	public CreateRedPixelAction(Player player) {
		super(player, 100);
	}

	@Override
	public void finish() {
		getPlayer().getInventory().add(ItemType.RED_PIXEL);
	}

}
