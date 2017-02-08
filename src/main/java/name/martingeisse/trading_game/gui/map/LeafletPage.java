package name.martingeisse.trading_game.gui.map;

import name.martingeisse.trading_game.game.GameListener;
import name.martingeisse.trading_game.game.space.DynamicSpaceObject;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.gui.leaflet.D3;
import name.martingeisse.trading_game.gui.leaflet.Leaflet;
import name.martingeisse.trading_game.gui.leaflet.LeafletD3SvgOverlay;
import name.martingeisse.trading_game.gui.websockets.GameListenerWebSocketBehavior;
import name.martingeisse.trading_game.gui.websockets.PushMessageSender;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

/**
 *
 */
public class LeafletPage extends AbstractPage {

	private static final IWebSocketPushMessage dynamicObjectsChangedPushMessage = new IWebSocketPushMessage() {
	};

	private long selectedSpaceObjectId = -1;

	public LeafletPage(PageParameters parameters) {
		super(parameters);

		WebMarkupContainer sidebar = new WebMarkupContainer("sidebar");
		sidebar.setOutputMarkupId(true);
		add(sidebar);
		sidebar.add(new Label("name", new PropertyModel<>(this, "selectedSpaceObject.name")));

		add(new AbstractDefaultAjaxBehavior() {

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);

				// build callback functions
				{
					StringBuilder builder = new StringBuilder();

					// left-clicking on the map
					{
						CallbackParameter[] parameters = {
							CallbackParameter.resolved("command", "'click'"),
							CallbackParameter.explicit("latitude"),
							CallbackParameter.explicit("longitude"),
							CallbackParameter.explicit("zoom"),
						};
						builder.append("sendMapClickCommand = ").append(getCallbackFunction(parameters)).append(';');
					}

					// right-clicking (long-pressing on mobile) on the map
					{
						CallbackParameter[] parameters = {
							CallbackParameter.resolved("command", "'menu'"),
							CallbackParameter.explicit("latitude"),
							CallbackParameter.explicit("longitude"),
							CallbackParameter.explicit("zoom"),
						};
						builder.append("sendMapMenuCommand = ").append(getCallbackFunction(parameters)).append(';');
					}

					response.render(JavaScriptHeaderItem.forScript(builder, null));
				}
			}

			@Override
			protected void respond(AjaxRequestTarget target) {
				IRequestParameters parameters = getRequest().getQueryParameters();
				String command = parameters.getParameterValue("command").toString();
				if (command == null) {
					return;
				}
				switch (command) {
					case "click":
					case "menu": {
						double clickLatitude = parameters.getParameterValue("latitude").toDouble();
						double clickLongitude = parameters.getParameterValue("longitude").toDouble();
						int zoom = parameters.getParameterValue("zoom").toInt();
						long clickX = MapCoordinates.convertLongitudeToX(clickLongitude);
						long clickY = MapCoordinates.convertLatitudeToY(clickLatitude);
						long radius = 5000 + (MapCoordinates.convertMapDistanceToGameDistance(10) >> zoom); // 5000 = object radius, plus 10 pixels extra
						SpaceObject spaceObject = getGame().getSpace().get(clickX, clickY, radius);
						System.out.println("-> " + spaceObject);
						if (spaceObject != null) {
							selectedSpaceObjectId = spaceObject.getId();
							double indicatorLatitude = MapCoordinates.convertYToLatitude(spaceObject.getY());
							double indicatorLongitude = MapCoordinates.convertXToLongitude(spaceObject.getX());
							double indicatorRadius = MapCoordinates.convertGameDistanceToMapDistance(5000) + 4 * Math.pow(0.5, zoom);
							target.appendJavaScript("changeSpaceObjectSelectionIndicator(" + indicatorLatitude + ", " + indicatorLongitude + ", " + indicatorRadius + ");");
						} else {
							selectedSpaceObjectId = -1;
						}
						target.add(sidebar);
						if (command.equals("menu")) {
							// TODO open the context menu
						}
						break;
					}
				}
			}

		});

		add(new GameListenerWebSocketBehavior() {

			@Override
			protected GameListener createListener(PushMessageSender pushMessageSender) {
				return new PushGameListener(pushMessageSender);
			}

			@Override
			protected void onPush(WebSocketRequestHandler handler, IWebSocketPushMessage message) {
				if (message == dynamicObjectsChangedPushMessage) {
					StringBuilder builder = new StringBuilder();
					buildDynamicSpaceObjectsData(builder);
					builder.append("redrawDynamicSpaceObjects();");
					handler.appendJavaScript(builder.toString());
				}
			}

		});

	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		// include JS libraries
		Leaflet.renderHead(response);
		D3.renderHead(response);
		LeafletD3SvgOverlay.renderHead(response);

		// render initialization script
		StringBuilder builder = new StringBuilder();
		builder.append("mapTileBaseUrl = '").append(getAbsoluteUrlFor(new SharedResourceReference("MapTile"))).append("';\n");
		buildDynamicSpaceObjectsData(builder);
		response.render(JavaScriptHeaderItem.forScript(builder.toString(), null));

	}

	private void buildDynamicSpaceObjectsData(StringBuilder builder) {
		builder.append("dynamicSpaceObjectsData = [\n");
		for (DynamicSpaceObject spaceObject : getGame().getSpace().getDynamicSpaceObjects()) {
			builder.append("\t{x: ").append(MapCoordinates.convertXToLongitude(spaceObject.getX()));
			builder.append(", y: ").append(MapCoordinates.convertYToLatitude(spaceObject.getY()));
			builder.append(", r: ").append(MapCoordinates.convertGameDistanceToMapDistance(5000));
			builder.append(", c: '").append(spaceObject instanceof PlayerShip ? "#00ffff" : "#0000ff").append("'},");
		}
		builder.append("];\n");
	}

	private String getAbsoluteUrlFor(ResourceReference reference) {
		return getRequestCycle().getUrlRenderer().renderFullUrl(Url.parse(urlFor(reference, null)));
	}

	/**
	 * This object listens to game events and generates push messages whenever the page is interested.
	 */
	private static class PushGameListener implements GameListener {

		private final PushMessageSender sender;

		public PushGameListener(PushMessageSender sender) {
			this.sender = sender;
		}

		@Override
		public void onDynamicSpaceObjectsChanged() {
			sender.send(dynamicObjectsChangedPushMessage);
		}
	}

	/**
	 * Getter method.
	 *
	 * @return the selectedSpaceObjectId
	 */
	public long getSelectedSpaceObjectId() {
		return selectedSpaceObjectId;
	}

	public SpaceObject getSelectedSpaceObject() {
		return getGame().getSpace().get(selectedSpaceObjectId);
	}

}
