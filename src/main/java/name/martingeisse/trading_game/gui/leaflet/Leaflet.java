package name.martingeisse.trading_game.gui.leaflet;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 *
 */
public final class Leaflet {

	public static final JavaScriptResourceReference JAVASCRIPT_RESOURCE_REFERENCE = new JavaScriptResourceReference(Leaflet.class, "leaflet.js");

	public static final CssResourceReference CSS_RESOURCE_REFERENCE = new CssResourceReference(Leaflet.class, "leaflet.css");

	public static void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(JAVASCRIPT_RESOURCE_REFERENCE));
		response.render(CssHeaderItem.forReference(CSS_RESOURCE_REFERENCE));
	}

}
