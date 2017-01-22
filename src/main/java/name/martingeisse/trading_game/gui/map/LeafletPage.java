package name.martingeisse.trading_game.gui.map;

import name.martingeisse.trading_game.gui.leaflet.Leaflet;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

/**
 *
 */
public class LeafletPage extends AbstractPage {

	public LeafletPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		Leaflet.renderHead(response);
		ResourceReference mapTileReference = new SharedResourceReference("MapTile");
		// ResourceReference mapTileReference = new SharedResourceReference("ComputedTile");
		response.render(JavaScriptHeaderItem.forScript("mapTileBaseUrl = '" + getAbsoluteUrlFor(mapTileReference) + "';", null));
	}

	private String getAbsoluteUrlFor(ResourceReference reference) {
		return getRequestCycle().getUrlRenderer().renderFullUrl(Url.parse(urlFor(reference, null)));
	}

}
