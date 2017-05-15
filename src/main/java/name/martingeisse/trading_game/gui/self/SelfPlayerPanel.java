package name.martingeisse.trading_game.gui.self;

import name.martingeisse.trading_game.gui.gamepage.GuiNavigationLink;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Shows the player's own stats.
 */
public class SelfPlayerPanel extends AbstractPanel {

	public SelfPlayerPanel(String id) {
		super(id);
		add(new Label("name", new PropertyModel<>(this, "player.name")));
		add(new Label("money", new PropertyModel<>(this, "player.money")));
		add(new GuiNavigationLink<Void>("renameLink") {
			@Override
			protected Panel getPanel(String panelId) {
				return new RenamePlayerPanel(panelId);
			}
		});
		add(new GuiNavigationLink<Void>("bindLink") {
			@Override
			protected Panel getPanel(String panelId) {
				return new BindPlayerToEmailAddressPanel(panelId);
			}
		});
	}

}
