package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.gui.openlayers.OpenLayers;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.markup.head.IHeaderResponse;

/**
 *
 */
public class OpenLayersPage extends AbstractPage {

	public OpenLayersPage() {
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		OpenLayers.renderHead(response);
	}

}
