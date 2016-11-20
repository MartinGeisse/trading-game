package name.martingeisse.trading_game.game.action;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.item.ItemType;

/**
 *
 */
public final class CreateRedPixelAction extends PlayerAction {

	public static final String DISPLAY_NAME = "create red pixel";

	public CreateRedPixelAction(Player player) {
		super(player, 100);
	}

	@Override
	public void finish() {
		getPlayer().getInventory().add(ItemType.RED_PIXEL);
	}

	@Override
	public String toString() {
		return DISPLAY_NAME;
	}

}
