
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

import javax.annotation.Resource;

/**
 *
 */
public class OpenLayersPage extends AbstractApplicationPage {

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
