package name.martingeisse.trading_game.gui.gamepage;

import name.martingeisse.trading_game.common.util.contract.ParameterUtil;
import name.martingeisse.wicket.serializable.SerializableFunction;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Provides static methods to interact with the navigation features of the GUI.
 */
public final class GuiNavigationUtil {

	// prevent instantiation
	private GuiNavigationUtil() {
	}

	/**
	 * Sets the currently visible panel, creating a new history entry. Does not change which tab is shown as active.
	 *
	 * The anchor component must be a component that is part of the hierarchy of the GamePage. It is used to locate
	 * the tab panel and modify it.
	 *
	 * The panelFactory creates the new component to show. It is a factory instead of the component itself so the
	 * wicketId can be passed to it.
	 *
	 * The ajaxRequestTarget is used to update the page client-side.
	 */
	public static void setPanel(Component anchor, SerializableFunction<String, WebMarkupContainer> panelFactory, AjaxRequestTarget ajaxRequestTarget) {
		ParameterUtil.ensureNotNull(anchor, "anchor");
		ParameterUtil.ensureNotNull(panelFactory, "panelFactory");
		ParameterUtil.ensureNotNull(ajaxRequestTarget, "ajaxRequestTarget");

		GamePage gamePage = (GamePage)anchor.getPage();
		MainMenuTabbedPanel mainMenuTabbedPanel = gamePage.getMainMenuTabbedPanel();
		WebMarkupContainer newPanel = panelFactory.apply(TabbedPanel.TAB_PANEL_ID);
		if (newPanel != null) {
			mainMenuTabbedPanel.replace(newPanel);
			ajaxRequestTarget.add(mainMenuTabbedPanel);
		}
	}

}
