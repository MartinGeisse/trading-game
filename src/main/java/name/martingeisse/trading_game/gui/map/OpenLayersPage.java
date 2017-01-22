package name.martingeisse.trading_game.gui.map;

import name.martingeisse.trading_game.gui.openlayers.OpenLayers;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

/**
 * Possible "Source" objects:
 * XYZ
 * OSM (with custom URL) (extends XYZ) --> just XYZ with "OSM defaults" for the options -> no advantage over XYZ,
 * TileDebug (purely client-side debugging helper)
 * <p>
 * UTFGrid etc. to describe interactivity
 */
public class OpenLayersPage extends AbstractPage {

	public OpenLayersPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		OpenLayers.renderHead(response);
		// ResourceReference mapTileReference = new SharedResourceReference("MapTile");
		ResourceReference mapTileReference = new SharedResourceReference("ComputedTile");
		response.render(JavaScriptHeaderItem.forScript("mapTileBaseUrl = '" + getAbsoluteUrlFor(mapTileReference) + "';", null));
	}

	private String getAbsoluteUrlFor(ResourceReference reference) {
		return getRequestCycle().getUrlRenderer().renderFullUrl(Url.parse(urlFor(reference, null)));
	}

}
