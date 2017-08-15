package name.martingeisse.trading_game.gui.components;

import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

/**
 *
 */
public abstract class AlphaUsersOnlyPanel extends AbstractPanel {

	public AlphaUsersOnlyPanel(String id) {
		super(id);
		add(new Label("headline", new PropertyModel<>(this, "headline")));
		add(new Label("description", new PropertyModel<>(this, "description")));
	}

	public abstract String getHeadline();
	public abstract String getDescription();

}
