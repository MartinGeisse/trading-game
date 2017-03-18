package name.martingeisse.trading_game.gui.map.leaflet;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 *
 */
public class LeafletD3SvgOverlay {

	public static final JavaScriptResourceReference JAVASCRIPT_RESOURCE_REFERENCE = new JavaScriptResourceReference(LeafletD3SvgOverlay.class, "LeafletD3SvgOverlay.js");

	public static void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(JAVASCRIPT_RESOURCE_REFERENCE));
	}

}
