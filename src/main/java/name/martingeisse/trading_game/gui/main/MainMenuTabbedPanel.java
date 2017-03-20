package name.martingeisse.trading_game.gui.main;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Adds custom markup to AjaxTabbedPanel.
 */
public class MainMenuTabbedPanel<T extends ITab> extends AjaxTabbedPanel<T> {

	public MainMenuTabbedPanel(String id, List<T> tabs) {
		super(id, tabs);
	}

	public MainMenuTabbedPanel(String id, List<T> tabs, IModel<Integer> model) {
		super(id, tabs, model);
	}

}
