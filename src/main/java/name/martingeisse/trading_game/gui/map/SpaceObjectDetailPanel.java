package name.martingeisse.trading_game.gui.map;

import name.martingeisse.trading_game.game.space.SpaceObject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * TODO undo tab panel switch on back button
 */
public class SpaceObjectDetailPanel extends Panel {

	public SpaceObjectDetailPanel(String id, IModel<SpaceObject> model) {
		super(id, model);
		add(new Label("name", new PropertyModel<>(model, "name")));
	}

}
