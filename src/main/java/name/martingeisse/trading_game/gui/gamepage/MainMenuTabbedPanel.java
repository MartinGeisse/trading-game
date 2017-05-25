package name.martingeisse.trading_game.gui.gamepage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

/**
 * Adds custom markup to AjaxTabbedPanel.
 */
class MainMenuTabbedPanel<T extends ITab> extends AjaxTabbedPanel<T> {

	private final PanelHistory panelHistory = new PanelHistory();

	private String feedbackText;

	public MainMenuTabbedPanel(String id, List<T> tabs) {
		super(id, tabs);

		Form<?> feedbackForm = new Form<Void>("feedbackForm") {
			@Override
			protected void onSubmit() {
				System.out.println("*** " + feedbackText);
				feedbackText = null;
			}
		};
		feedbackForm.add(new AjaxFormSubmitBehavior("submit") {

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.setPreventDefault(true);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				target.add(getComponent().get("text"));
			}

		});
		feedbackForm.add(new TextArea<>("text", new PropertyModel<>(this, "feedbackText")).setOutputMarkupId(true));
		add(feedbackForm);
	}

	/**
	 * Getter method.
	 *
	 * @return the feedbackText
	 */
	public String getFeedbackText() {
		return feedbackText;
	}

	/**
	 * Setter method.
	 *
	 * @param feedbackText the feedbackText
	 */
	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	public WebMarkupContainer getCurrentTab() {
		return (WebMarkupContainer) get(TAB_PANEL_ID);
	}

	public PanelHistory getPanelHistory() {
		return panelHistory;
	}

	@Override
	protected void onAjaxUpdate(AjaxRequestTarget target) {
		GuiNavigationUtil.addHistoryEntry(this, getCurrentTab(), target);
	}

	public void replacePanelAndRecognize(WebMarkupContainer panel) {
		if (!panel.getId().equals(TAB_PANEL_ID)) {
			throw new IllegalArgumentException("argument panel has wicket:id " + panel.getId() + ", expected " + TAB_PANEL_ID);
		}

		// find a tab that matches the panel and set it as the selected tab. This will create a panel for that tab,
		// which we throw away immediately and use the provided argument tab instead. Wasteful, but with Wicket's
		// TabbedPanel, that's the easiest way. If no matching tab is found, we stay at the currently selected one
		// and just replace the panel.
		List<? extends ITab> tabs = getTabs();
		for (int i = 0; i < tabs.size(); i++) {
			ITab tab = tabs.get(i);
			if (tab instanceof PanelRecognizingTab) {
				if (((PanelRecognizingTab) tab).isMatchingPanel(panel)) {
					setSelectedTab(i);
					break;
				}
			}
		}

		replace(panel);
	}

	public static interface PanelRecognizingTab {
		public boolean isMatchingPanel(WebMarkupContainer panel);
	}

}
