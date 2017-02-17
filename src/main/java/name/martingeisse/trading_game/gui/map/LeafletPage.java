package name.martingeisse.trading_game.gui.map;

import name.martingeisse.trading_game.game.GameListener;
import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.actions.LoadUnloadAction;
import name.martingeisse.trading_game.game.item.FixedItemStack;
import name.martingeisse.trading_game.game.item.ItemStack;
import name.martingeisse.trading_game.game.space.*;
import name.martingeisse.trading_game.gui.item.ItemIcons;
import name.martingeisse.trading_game.gui.leaflet.D3;
import name.martingeisse.trading_game.gui.leaflet.Leaflet;
import name.martingeisse.trading_game.gui.leaflet.LeafletD3SvgOverlay;
import name.martingeisse.trading_game.gui.websockets.GameListenerWebSocketBehavior;
import name.martingeisse.trading_game.gui.websockets.PushMessageSender;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class LeafletPage extends AbstractPage {

	private static final IWebSocketPushMessage dynamicObjectsChangedPushMessage = new IWebSocketPushMessage() {
	};

	static class SpaceObjectPropertiesChangedPushMessage implements IWebSocketPushMessage {

		private final SpaceObject spaceObject;

		public SpaceObjectPropertiesChangedPushMessage(SpaceObject spaceObject) {
			this.spaceObject = spaceObject;
		}

		public SpaceObject getSpaceObject() {
			return spaceObject;
		}

	};

	private static final Comparator<SpaceObject> playerShipsLowPriorityComparator = (a, b) -> {
		boolean a2 = (a instanceof PlayerShip);
		boolean b2 = (b instanceof PlayerShip);
		return a2 ? (b2 ? 0 : -1) : (b2 ? 1 : 0);
	};

	private long selectedSpaceObjectId = -1;

	public LeafletPage(PageParameters parameters) {
		super(parameters);

		IModel<SpaceObject> selectedSpaceObjectModel = new PropertyModel<>(this, "selectedSpaceObject");

		WebMarkupContainer sidebar = new WebMarkupContainer("sidebar");
		sidebar.setOutputMarkupId(true);
		add(sidebar);
		sidebar.add(new Label("name", new PropertyModel<>(this, "selectedSpaceObject.name")));
		sidebar.add(new Label("type", new PropertyModel<>(this, "selectedSpaceObject.class.simpleName")));
		sidebar.add(new Label("x", new PropertyModel<>(this, "selectedSpaceObject.x")));
		sidebar.add(new Label("y", new PropertyModel<>(this, "selectedSpaceObject.y")));
		sidebar.add(new Label("distance", new PropertyModel<>(this, "selectedSpaceObjectDistance")));
		sidebar.add(new ListView<Action>("actions", new PropertyModel<>(this, "selectedSpaceObjectActions")) {
			@Override
			protected void populateItem(ListItem<Action> actionItem) {
				AjaxLink<?> link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						Player player = getPlayer();
						player.cancelCurrentAction();
						player.cancelAllPendingActions();
						player.scheduleAction(actionItem.getModelObject());
					}
				};
				link.add(new Label("name", actionItem.getModelObject().toString()));
				actionItem.add(link);
			}

		});
		WebMarkupContainer remoteItemsContainer = new WebMarkupContainer("remoteItemsContainer") {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getRemoteItems() != null);
			}
		};
		sidebar.add(remoteItemsContainer);
		remoteItemsContainer.add(new ListView<ItemStack>("itemStacks", new PropertyModel<>(this, "remoteItems")) {
			@Override
			protected void populateItem(ListItem<ItemStack> item) {
				item.add(new Label("size", "" + item.getModelObject().getSize()));
				item.add(new Label("itemType", "" + item.getModelObject().getItemType()));
				item.add(new Image("icon", ItemIcons.get(item.getModelObject().getItemType())));
				item.add(new AjaxLink<Void>("loadLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						Player player = getPlayer();
						SpaceStation spaceStation = (SpaceStation)getSelectedSpaceObject();
						FixedItemStack itemsToLoad = new FixedItemStack(item.getModelObject().getItemType(), item.getModelObject().getSize());
						player.cancelCurrentAction();
						player.cancelAllPendingActions();
						player.scheduleAction(new LoadUnloadAction(player, spaceStation, LoadUnloadAction.Type.LOAD, itemsToLoad, item.getIndex()));
					}
				});
			}
		});
		WebMarkupContainer localItemsContainer = new WebMarkupContainer("localItemsContainer") {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getLocalItems() != null);
			}
		};
		sidebar.add(localItemsContainer);
		localItemsContainer.add(new ListView<ItemStack>("itemStacks", new PropertyModel<>(this, "localItems")) {
			@Override
			protected void populateItem(ListItem<ItemStack> item) {
				item.add(new Label("size", "" + item.getModelObject().getSize()));
				item.add(new Label("itemType", "" + item.getModelObject().getItemType()));
				item.add(new Image("icon", ItemIcons.get(item.getModelObject().getItemType())));
				item.add(new AjaxLink<Void>("unloadLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						Player player = getPlayer();
						SpaceStation spaceStation = (SpaceStation)getSelectedSpaceObject();
						FixedItemStack itemsToLoad = new FixedItemStack(item.getModelObject().getItemType(), item.getModelObject().getSize());
						player.cancelCurrentAction();
						player.cancelAllPendingActions();
						player.scheduleAction(new LoadUnloadAction(player, spaceStation, LoadUnloadAction.Type.UNLOAD, itemsToLoad, item.getIndex()));
					}
				});
			}
		});

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

					// directly selecting the player's ship
					{
						CallbackParameter[] parameters = {
							CallbackParameter.resolved("command", "'selectOwnShip'"),
							CallbackParameter.explicit("zoom"),
						};
						builder.append("sendSelectOwnShipCommand = ").append(getCallbackFunction(parameters)).append(';');
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
						SpaceObject spaceObject = getGame().getSpace().get(clickX, clickY, radius, playerShipsLowPriorityComparator);
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

					case "selectOwnShip": {
						int zoom = parameters.getParameterValue("zoom").toInt();
						SpaceObject spaceObject = getPlayer().getShip();
						System.out.println("-> " + spaceObject);
						selectedSpaceObjectId = spaceObject.getId();
						double indicatorLatitude = MapCoordinates.convertYToLatitude(spaceObject.getY());
						double indicatorLongitude = MapCoordinates.convertXToLongitude(spaceObject.getX());
						double indicatorRadius = MapCoordinates.convertGameDistanceToMapDistance(5000) + 4 * Math.pow(0.5, zoom);
						target.appendJavaScript("changeSpaceObjectSelectionIndicator(" + indicatorLatitude + ", " + indicatorLongitude + ", " + indicatorRadius + ");");
						target.add(sidebar);
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
				} else if (message instanceof SpaceObjectPropertiesChangedPushMessage) {
					SpaceObjectPropertiesChangedPushMessage typedMessage = (SpaceObjectPropertiesChangedPushMessage)message;
					if (typedMessage.getSpaceObject() == getPlayer().getShip() || typedMessage.getSpaceObject() == getSelectedSpaceObject()) {
						handler.add(sidebar);
					}
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
			builder.append(", r: ").append(MapCoordinates.convertGameDistanceToMapDistance(500));
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
			if (sender == null) {
				throw new IllegalArgumentException("sender is null");
			}
			this.sender = sender;
		}

		@Override
		public void onDynamicSpaceObjectsChanged() {
			sender.send(dynamicObjectsChangedPushMessage);
		}

		@Override
		public void onSpaceObjectPropertiesChanged(SpaceObject spaceObject) {
			sender.send(new SpaceObjectPropertiesChangedPushMessage(spaceObject));
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

	public Double getSelectedSpaceObjectDistance() {
		SpaceObject selectedSpaceObject = getSelectedSpaceObject();
		if (selectedSpaceObject == null) {
			return null;
		} else {
			return GeometryUtil.getDistance(getPlayer().getShip(), selectedSpaceObject);
		}
	}

	public List<Action> getSelectedSpaceObjectActions() {
		SpaceObject selectedSpaceObject = getSelectedSpaceObject();
		if (selectedSpaceObject == null) {
			return null;
		} else {
			return selectedSpaceObject.getActionsFor(getPlayer());
		}
	}

	public List<ItemStack> getRemoteItems() {
		SpaceObject selectedSpaceObject = getSelectedSpaceObject();
		if (selectedSpaceObject instanceof SpaceStation) {
			return ((SpaceStation)selectedSpaceObject).getInventory().getItemStacks();
		} else {
			return null;
		}
	}

	public List<ItemStack> getLocalItems() {
		SpaceObject selectedSpaceObject = getSelectedSpaceObject();
		Player player = getPlayer();
		if (selectedSpaceObject instanceof SpaceStation || selectedSpaceObject == player.getShip()) {
			return player.getInventory().getItemStacks();
		} else {
			return null;
		}
	}

}
