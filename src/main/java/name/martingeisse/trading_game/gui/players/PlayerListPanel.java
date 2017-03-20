package name.martingeisse.trading_game.gui.players;

import name.martingeisse.trading_game.gui.main.TabPanelReplacementLink;
import name.martingeisse.trading_game.gui.self.RenamePlayerPanel;
import name.martingeisse.trading_game.gui.websockets.GuiGameEventListener;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Shows the list of players.
 */
public class PlayerListPanel extends AbstractPanel {

	public PlayerListPanel(String id) {
		super(id);
	}

}
