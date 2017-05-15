package name.martingeisse.trading_game.gui.gamepage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Replaces the current tab panel with an arbitrary panel, but does not change the selected tab.
 */
public abstract class GuiNavigationLink<T> extends AjaxLink<T> {

	public GuiNavigationLink(String id) {
		super(id);
	}

	public GuiNavigationLink(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		GuiNavigationUtil.setPanel(this, this::getPanel, target);
	}

	// may return null to leave the panel unchanged
	protected abstract WebMarkupContainer getPanel(String panelId);

}
