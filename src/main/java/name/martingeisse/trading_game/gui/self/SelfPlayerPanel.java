package name.martingeisse.trading_game.gui.self;

import name.martingeisse.trading_game.gui.main.TabPanelReplacementLink;
import name.martingeisse.trading_game.gui.websockets.GuiGameEventListener;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Shows the player's own stats.
 */
public class SelfPlayerPanel extends AbstractPanel implements GuiGameEventListener {

	public SelfPlayerPanel(String id) {
		super(id);
		add(new Label("name", new PropertyModel<>(this, "player.name")));
		add(new TabPanelReplacementLink<Void>("renameLink") {
			@Override
			protected Panel getPanel(String panelId) {
				return new RenamePlayerPanel(panelId);
			}
		});
	}

}
