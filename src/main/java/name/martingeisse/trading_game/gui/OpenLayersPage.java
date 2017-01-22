package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.gui.openlayers.OpenLayers;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * Possible "Source" objects:
 * XYZ
 * OSM (with custom URL) (extends XYZ) --> just XYZ with "OSM defaults" for the options -> no advantage over XYZ,
 * TileDebug (purely client-side debugging helper)
 *
 * UTFGrid etc. to describe interactivity
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
