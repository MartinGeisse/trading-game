package name.martingeisse.trading_game.gui.gamepage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;

import java.util.List;

/**
 * Adds custom markup to AjaxTabbedPanel.
 */
class MainMenuTabbedPanel<T extends ITab> extends AjaxTabbedPanel<T> {

	private final PanelHistory panelHistory = new PanelHistory();

	public MainMenuTabbedPanel(String id, List<T> tabs) {
		super(id, tabs);
	}

	public WebMarkupContainer getCurrentTab() {
		return (WebMarkupContainer)get(TAB_PANEL_ID);
	}

	public PanelHistory getPanelHistory() {
		return panelHistory;
	}

	@Override
	protected void onAjaxUpdate(AjaxRequestTarget target) {
		GuiNavigationUtil.addHistoryEntry(this, getCurrentTab(), target);
	}

}
