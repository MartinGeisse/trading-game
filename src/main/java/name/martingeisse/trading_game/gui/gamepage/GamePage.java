package name.martingeisse.trading_game.gui.gamepage;

import name.martingeisse.trading_game.gui.inventory.InventorySectionPanel;
import name.martingeisse.trading_game.gui.map.MapSectionPanel;
import name.martingeisse.trading_game.gui.players.PlayerListPanel;
import name.martingeisse.trading_game.gui.self.SelfPlayerPanel;
import name.martingeisse.trading_game.gui.websockets.GameListenerWebSocketBehavior;
import name.martingeisse.trading_game.platform.wicket.LoginCookieUtil;
import name.martingeisse.trading_game.platform.wicket.MyWicketSession;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Arrays;

/**
 *
 */
public class GamePage extends AbstractPage {

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
	}

}
