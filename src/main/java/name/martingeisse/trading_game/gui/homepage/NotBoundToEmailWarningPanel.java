package name.martingeisse.trading_game.gui.homepage;

import name.martingeisse.trading_game.game.player.Player;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 */
public class NotBoundToEmailWarningPanel extends Panel {

	public NotBoundToEmailWarningPanel(String id, IModel<Player> model) {
		super(id, model);
		add(new Label("name", new PropertyModel<>(model, "name")));
		add(new Label("loginToken", new PropertyModel<>(model, "loginToken")));
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		Player player = (Player) getDefaultModelObject();
		setVisible(player.getEmailAddress() == null);
	}

}
