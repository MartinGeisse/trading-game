package name.martingeisse.trading_game.gui.manufacturing;

import name.martingeisse.trading_game.game.action.actions.ManufacturingAction;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.game.item.ImmutableItemStack;
import name.martingeisse.trading_game.game.manufacturing.Blueprint;
import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 *
 */
public class ManufacturingSectionDummyPanel extends AlphaUsersOnlyPanel {

	public ManufacturingSectionDummyPanel(String id) {
		super(id);
	}

}
