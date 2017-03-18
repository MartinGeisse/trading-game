package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.gui.map.MapSectionPanel;
import name.martingeisse.trading_game.gui.websockets.GameListenerWebSocketBehavior;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 */
public class MainPage extends AbstractPage {

	public MainPage(PageParameters parameters) {
		super(parameters);
		add(new GameListenerWebSocketBehavior());
		add(new MapSectionPanel("mapSectionPanel"));
	}

}
