package name.martingeisse.trading_game.gui.map;

import name.martingeisse.trading_game.gui.leaflet.Leaflet;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
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
		add(new AbstractDefaultAjaxBehavior() {

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);

				// build callback functions
				{
					StringBuilder builder = new StringBuilder();

					// clicking on the map
					{
						CallbackParameter[] parameters = {
							CallbackParameter.resolved("command", "'click'"),
							CallbackParameter.explicit("x"),
							CallbackParameter.explicit("y"),
						};
						builder.append("sendMapClickCommand = ").append(getCallbackFunction(parameters)).append(';');
					}

					response.render(JavaScriptHeaderItem.forScript(builder, null));
				}
			}

			@Override
			protected void respond(AjaxRequestTarget target) {
				String command = target.getPageParameters().get("command").toString();
				if (command == null) {
					return;
				}
				switch (command) {
					case "click": {
						int x = target.getPageParameters().get("x").toInt();
						int y = target.getPageParameters().get("y").toInt();
						System.out.println("clicked at " + x + ", " + y);
						break;
					}
				}
			}

		});
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
