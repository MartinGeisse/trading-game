package name.martingeisse.trading_game.gui.self;

import name.martingeisse.trading_game.gui.websockets.GuiGameEventListener;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;

/**
 * Shows the player's own stats.
 */
public class SelfPlayerPanel extends AbstractPanel implements GuiGameEventListener {

	public SelfPlayerPanel(String id) {
		super(id);
	}

}
