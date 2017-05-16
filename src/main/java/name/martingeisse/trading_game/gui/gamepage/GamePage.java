package name.martingeisse.trading_game.gui.gamepage;

import name.martingeisse.trading_game.gui.inventory.InventorySectionPanel;
import name.martingeisse.trading_game.gui.map.MapSectionPanel;
import name.martingeisse.trading_game.gui.players.PlayerListPanel;
import name.martingeisse.trading_game.gui.self.SelfPlayerPanel;
import name.martingeisse.trading_game.gui.websockets.GameListenerWebSocketBehavior;
import name.martingeisse.trading_game.platform.wicket.LoginCookieUtil;
import name.martingeisse.trading_game.platform.wicket.MyWicketSession;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Arrays;

/**
 *
 */
public class GamePage extends AbstractPage {

	private boolean alreadyRendered = false;

	public GamePage(PageParameters pageParameters) {
		super(pageParameters);

		if (getPlayer() == null) {
			MyWicketSession.get().setPlayerId(LoginCookieUtil.getPlayerIdFromCookie());
		}

		add(new GameListenerWebSocketBehavior());

		ITab mapTab = new AbstractTab(Model.of("Map")) {
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new MapSectionPanel(panelId);
			}
		};
		ITab selfPlayerTab = new AbstractTab(Model.of("Player")) {
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new SelfPlayerPanel(panelId);
			}
		};
		ITab playerListTab = new AbstractTab(Model.of("Players")) {
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new PlayerListPanel(panelId);
			}
		};
		ITab inventoryTab = new AbstractTab(Model.of("Inventory")) {
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new InventorySectionPanel(panelId);
			}
		};
		add(new MainMenuTabbedPanel<>("tabbedPanel", Arrays.asList(mapTab, selfPlayerTab, playerListTab, inventoryTab)));

		add(new AjaxEventBehavior("popstate") {

			@Override
			protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				attributes.getDynamicExtraParameters().add("return {state: attrs.event.originalEvent.state};");
			}

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				Integer state = RequestCycle.get().getRequest().getQueryParameters().getParameterValue("state").toOptionalInteger();
				MainMenuTabbedPanel mainMenuTabbedPanel = getMainMenuTabbedPanel();
				if (state == null) {
					mainMenuTabbedPanel.setSelectedTab(0);
				} else {
					// TODO restore "selected tab" marker
					mainMenuTabbedPanel.replace(mainMenuTabbedPanel.getPanelHistory().getPanel(state));
					target.add(mainMenuTabbedPanel);
				}
			}

		});
	}

	@Override
	protected void onBeforeRender() {
		// TODO breaks when backbuttoning to the page before opening the game page first, then forward to the game page
		// again. This creates a new game page which doesn't know the panel history.
		// TODO also breaks reloading since it forgets the current tab panel, and also breaks the panel history.
		if (alreadyRendered) {
			throw new RestartResponseException(getPageClass(), getPageParameters());
		} else {
			alreadyRendered = true;
			super.onBeforeRender();
		}
	}

	MainMenuTabbedPanel getMainMenuTabbedPanel() {
		return (MainMenuTabbedPanel)get("tabbedPanel");
	}

}
