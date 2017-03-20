package name.martingeisse.trading_game.gui.main;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 *
 */
public abstract class TabPanelReplacementLink<T> extends AjaxLink<T> {

	public TabPanelReplacementLink(String id) {
		super(id);
	}

	public TabPanelReplacementLink(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		MainMenuTabbedPanel.replaceTabPanel(this, this::getPanel, target);
	}

	// may return null to leave the panel unchanged
	protected abstract WebMarkupContainer getPanel(String panelId);

}
