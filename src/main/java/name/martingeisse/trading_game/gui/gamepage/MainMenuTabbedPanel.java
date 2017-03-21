package name.martingeisse.trading_game.gui.gamepage;

import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import java.util.List;
import java.util.function.Function;

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

	public static void replaceTabPanel(Component anchor, Function<String, WebMarkupContainer> panelFactory, AjaxRequestTarget ajaxRequestTarget) {
		ParameterUtil.ensureNotNull(anchor, "anchor");
		ParameterUtil.ensureNotNull(panelFactory, "panelFactory");
		ParameterUtil.ensureNotNull(ajaxRequestTarget, "ajaxRequestTarget");

		MainMenuTabbedPanel mainMenuTabbedPanel = anchor.findParent(MainMenuTabbedPanel.class);
		if (mainMenuTabbedPanel == null) {
			throw new RuntimeException("component isn't contained in the MainMenuTabbedPanel");
		}
		WebMarkupContainer newPanel = panelFactory.apply(TabbedPanel.TAB_PANEL_ID);
		if (newPanel != null) {
			mainMenuTabbedPanel.replace(newPanel);
			ajaxRequestTarget.add(mainMenuTabbedPanel);
		}
	}

}
