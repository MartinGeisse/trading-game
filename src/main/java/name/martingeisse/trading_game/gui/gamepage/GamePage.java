package name.martingeisse.trading_game.gui.gamepage;

import name.martingeisse.trading_game.gui.inventory.InventorySectionPanel;
import name.martingeisse.trading_game.gui.map.MapSectionPanel;
import name.martingeisse.trading_game.gui.players.PlayerListPanel;
import name.martingeisse.trading_game.gui.self.SelfPlayerPanel;
import name.martingeisse.trading_game.gui.websockets.GameListenerWebSocketBehavior;
import name.martingeisse.trading_game.platform.wicket.LoginCookieUtil;
import name.martingeisse.trading_game.platform.wicket.MyWicketSession;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.WicketWebSocketJQueryResourceReference;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Generics;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.template.PackageTextTemplate;

import java.util.Arrays;
import java.util.Map;

/**
 *
 */
public class GamePage extends AbstractPage {

	private String windowName = null;

	public GamePage(PageParameters pageParameters) {
		super(pageParameters);

		if (getPlayer() == null) {
			MyWicketSession.get().setPlayerId(LoginCookieUtil.getPlayerIdFromCookie());
		}

		add(new AbstractDefaultAjaxBehavior() {

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);

				// this check must be done first so it happens before the websocket is established
				StringBuilder builder = new StringBuilder();
				builder.append("if (!window.name.startsWith('trading-game-')) {\n");
				builder.append("	window.name = 'trading-game-' + Math.random();\n");
				builder.append("}\n");
				CallbackParameter windowNameParameter = CallbackParameter.explicit("windowName");
				builder.append("(");
				builder.append(getCallbackFunction(windowNameParameter));
				builder.append(")(window.name);\n");
				response.render(JavaScriptHeaderItem.forScript(builder, null));

			}

			@Override
			protected void respond(AjaxRequestTarget target) {
				String sentWindowName = getRequest().getQueryParameters().getParameterValue("windowName").toString();
				if (windowName == null) {
					windowName = sentWindowName;
				} else if (!windowName.equals(sentWindowName)) {
					// TODO keep current tab; transfer panel history
					throw new RestartResponseException(getPageClass(), getPageParameters());
				}
				target.appendJavaScript("setupWebsocket()");
			}

		});

		add(new GameListenerWebSocketBehavior());

		ITab mapTab = new PanelClassRecognizingTab(Model.of("Map"), MapSectionPanel.class) {
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new MapSectionPanel(panelId);
			}
		};
		ITab selfPlayerTab = new PanelClassRecognizingTab(Model.of("Player"), SelfPlayerPanel.class) {
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new SelfPlayerPanel(panelId);
			}
		};
		ITab playerListTab = new PanelClassRecognizingTab(Model.of("Players"), PlayerListPanel.class) {
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new PlayerListPanel(panelId);
			}
		};
		ITab inventoryTab = new PanelClassRecognizingTab(Model.of("Inventory"), InventorySectionPanel.class) {
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
					mainMenuTabbedPanel.replacePanelAndRecognize(mainMenuTabbedPanel.getPanelHistory().getPanel(state));
				}
				target.add(mainMenuTabbedPanel);
			}

		});
	}

	MainMenuTabbedPanel getMainMenuTabbedPanel() {
		return (MainMenuTabbedPanel)get("tabbedPanel");
	}

	public static abstract class PanelClassRecognizingTab extends AbstractTab implements MainMenuTabbedPanel.PanelRecognizingTab {

		private final Class<? extends WebMarkupContainer> panelClass;

		public PanelClassRecognizingTab(IModel<String> title, Class<? extends WebMarkupContainer> panelClass) {
			super(title);
			this.panelClass = panelClass;
		}

		@Override
		public boolean isMatchingPanel(WebMarkupContainer panel) {
			return panelClass.isInstance(panel);
		}

	}

}
