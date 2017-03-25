package name.martingeisse.trading_game.gui.map;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.action.actions.EquipAction;
import name.martingeisse.trading_game.game.action.actions.LoadUnloadAction;
import name.martingeisse.trading_game.game.action.actions.UnequipAction;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentChangedEvent;
import name.martingeisse.trading_game.game.equipment.SlotInfo;
import name.martingeisse.trading_game.game.event.GameEvent;
import name.martingeisse.trading_game.game.event.GameEventBatch;
import name.martingeisse.trading_game.game.item.ImmutableItemStack;
import name.martingeisse.trading_game.game.item.InventoryChangedEvent;
import name.martingeisse.trading_game.game.item.ObjectWithInventory;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.*;
import name.martingeisse.trading_game.gui.item.ItemIcons;
import name.martingeisse.trading_game.gui.map.leaflet.D3;
import name.martingeisse.trading_game.gui.map.leaflet.Leaflet;
import name.martingeisse.trading_game.gui.map.leaflet.LeafletD3SvgOverlay;
import name.martingeisse.trading_game.gui.websockets.GuiGameEventListener;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

import java.util.Comparator;
import java.util.List;

/**
 * Encapsulates the actual map view and the properties box.
 *
 * TODO remove unneeded methods and game event listeners
 */
public class MapSectionPanel extends AbstractPanel implements GuiGameEventListener {

	private static final Comparator<SpaceObject> playerShipsLowPriorityComparator = (a, b) -> {
		boolean a2 = (a instanceof PlayerShip);
		boolean b2 = (b instanceof PlayerShip);
		return a2 ? (b2 ? 0 : -1) : (b2 ? 1 : 0);
	};

	private long selectedSpaceObjectId = -1;

	public MapSectionPanel(String id) {
		super(id);

		WebMarkupContainer propertiesBox = new WebMarkupContainer("propertiesBox") {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				String classAttribute = tag.getAttribute("class");
				if (classAttribute != null) {
					classAttribute = classAttribute.replace("hasSelectedSpaceObject", "").trim();
				} else {
					classAttribute = "";
				}
				if (getSelectedSpaceObject() != null) {
					classAttribute = classAttribute + " hasSelectedSpaceObject";
				}
				tag.put("class", classAttribute);
			}
		};
		propertiesBox.setOutputMarkupId(true);
		add(propertiesBox);
		propertiesBox.add(new Label("name", new PropertyModel<>(this, "selectedSpaceObject.name")));
		propertiesBox.add(new Label("type", new PropertyModel<>(this, "selectedSpaceObject.class.simpleName")));
		propertiesBox.add(new Label("x", new PropertyModel<>(this, "selectedSpaceObject.x")));
		propertiesBox.add(new Label("y", new PropertyModel<>(this, "selectedSpaceObject.y")));
		propertiesBox.add(new Label("distance", new PropertyModel<>(this, "selectedSpaceObjectDistance")));
		propertiesBox.add(new ListView<Action>("actions", new PropertyModel<>(this, "selectedSpaceObjectActions")) {
			@Override
			protected void populateItem(ListItem<Action> actionItem) {
				AjaxLink<?> link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						ActionQueue actionQueue = getPlayer().getActionQueue();
						actionQueue.cancelCurrentAction();
						actionQueue.cancelAllPendingActions();
						actionQueue.scheduleAction(actionItem.getModelObject());
					}
				};
				link.add(new Label("name", actionItem.getModelObject().getName()));
				actionItem.add(link);
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
								CallbackParameter.explicit("x"),
								CallbackParameter.explicit("y"),
						};
						builder.append("sendMapMenuCommand = ").append(getCallbackFunction(parameters)).append(';');
					}

					// directly selecting the player's ship
					{
						CallbackParameter[] parameters = {
								CallbackParameter.resolved("command", "'selectOwnShip'"),
								CallbackParameter.explicit("zoom"), // TODO used to size the indicator, but that's wrong anyway
						};
						builder.append("sendSelectOwnShipCommand = ").append(getCallbackFunction(parameters)).append(';');
					}

					// directly selecting any object by ID
					{
						CallbackParameter[] parameters = {
								CallbackParameter.resolved("command", "'selectById'"),
								CallbackParameter.explicit("id"),
								CallbackParameter.explicit("zoom"), // TODO used to size the indicator, but that's wrong anyway
						};
						builder.append("sendSelectByIdCommand = ").append(getCallbackFunction(parameters)).append(';');
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
						SpaceObject spaceObject = getSpace().get(clickX, clickY, radius, playerShipsLowPriorityComparator);
						System.out.println("-> " + spaceObject);
						if (spaceObject != null) {
							selectedSpaceObjectId = spaceObject.getId();
							double indicatorLatitude = MapCoordinates.convertYToLatitude(spaceObject.getY());
							double indicatorLongitude = MapCoordinates.convertXToLongitude(spaceObject.getX());
							double indicatorRadius = MapCoordinates.convertGameDistanceToMapDistance(5000) + 4 * Math.pow(0.5, zoom);
							target.appendJavaScript("changeSpaceObjectSelectionIndicator(" + indicatorLatitude + ", " + indicatorLongitude + ", " + indicatorRadius + ");");
							target.appendJavaScript("setStateCookie('mapSelection', " + selectedSpaceObjectId + ")"); // TODO prevent conversion from long to float in JS
						} else {
							selectedSpaceObjectId = -1;
							target.appendJavaScript("removeSpaceObjectSelectionIndicator();");
							target.appendJavaScript("setStateCookie('mapSelection', null)");
						}
						target.add(propertiesBox);
						if (command.equals("menu")) {
							int x = parameters.getParameterValue("x").toInt();
							int y = parameters.getParameterValue("y").toInt();
							target.appendJavaScript("$('#context-menu').css({display: 'block', left: " + x + ", top: " + y + "});");
							// TODO open the context menu
						}
						break;
					}

					case "selectOwnShip": {
						int zoom = parameters.getParameterValue("zoom").toInt();
						SpaceObject spaceObject = getPlayer().getShip();
						selectedSpaceObjectId = spaceObject.getId();
						double indicatorLatitude = MapCoordinates.convertYToLatitude(spaceObject.getY());
						double indicatorLongitude = MapCoordinates.convertXToLongitude(spaceObject.getX());
						double indicatorRadius = MapCoordinates.convertGameDistanceToMapDistance(5000) + 4 * Math.pow(0.5, zoom);
						target.appendJavaScript("changeSpaceObjectSelectionIndicator(" + indicatorLatitude + ", " + indicatorLongitude + ", " + indicatorRadius + ");");
						target.appendJavaScript("setStateCookie('mapSelection', " + selectedSpaceObjectId + ")"); // TODO prevent conversion from long to float in JS
						target.add(propertiesBox);
						break;
					}

					case "selectById": {
						int zoom = parameters.getParameterValue("zoom").toInt();
						SpaceObject spaceObject;
						try {
							spaceObject = getSpace().get(parameters.getParameterValue("id").toLong());
						} catch (IllegalArgumentException e) {
							spaceObject = null;
						}
						if (spaceObject != null) {
							selectedSpaceObjectId = spaceObject.getId();
							double indicatorLatitude = MapCoordinates.convertYToLatitude(spaceObject.getY());
							double indicatorLongitude = MapCoordinates.convertXToLongitude(spaceObject.getX());
							double indicatorRadius = MapCoordinates.convertGameDistanceToMapDistance(5000) + 4 * Math.pow(0.5, zoom);
							target.appendJavaScript("changeSpaceObjectSelectionIndicator(" + indicatorLatitude + ", " + indicatorLongitude + ", " + indicatorRadius + ");");
							target.appendJavaScript("setStateCookie('mapSelection', " + selectedSpaceObjectId + ")"); // TODO prevent conversion from long to float in JS
							target.add(propertiesBox);
						} else {
							selectedSpaceObjectId = -1;
							target.appendJavaScript("removeSpaceObjectSelectionIndicator();");
							target.appendJavaScript("setStateCookie('mapSelection', null)");
						}
						break;
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
		response.render(new OnDomReadyHeaderItem("initializeMapSectionPanel();") {
			@Override
			public Iterable<?> getRenderTokens() {
				return ImmutableList.of();
			}
		});

	}

	private void buildDynamicSpaceObjectsData(StringBuilder builder) {
		builder.append("dynamicSpaceObjectsData = [\n");
		for (DynamicSpaceObject spaceObject : getSpace().getDynamicSpaceObjects()) {
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
	 * Getter method.
	 *
	 * @return the selectedSpaceObjectId
	 */
	public long getSelectedSpaceObjectId() {
		return selectedSpaceObjectId;
	}

	public SpaceObject getSelectedSpaceObject() {
		return selectedSpaceObjectId == -1 ? null : getSpace().get(selectedSpaceObjectId);
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

	public List<ImmutableItemStack> getRemoteItems() {
		SpaceObject selectedSpaceObject = getSelectedSpaceObject();
		if (selectedSpaceObject instanceof SpaceStation) {
			return ((SpaceStation) selectedSpaceObject).getInventory().getItems().getStacks();
		} else {
			return null;
		}
	}

	public List<ImmutableItemStack> getLocalItems() {
		SpaceObject selectedSpaceObject = getSelectedSpaceObject();
		Player player = getPlayer();
		if (selectedSpaceObject instanceof SpaceStation || selectedSpaceObject == player.getShip()) {
			return player.getInventory().getItems().getStacks();
		} else {
			return null;
		}
	}

	public List<SlotInfo> getEquipmentSlots() {
		return getPlayer().getEquipment().getAllSlots();
	}

	@Override
	public void receiveGameEventBatch(IPartialPageRequestHandler partialPageRequestHandler, GameEventBatch eventBatch) {
		ImmutableList<GameEvent> events = eventBatch.getEvents();

		// analyze events
		boolean anySpaceObjectChangedItsPosition = false;
		boolean propertiesBoxChanged = false;
		for (GameEvent event : events) {
			if (event instanceof SpaceObjectPositionChangedEvent) {
				anySpaceObjectChangedItsPosition = true;
				if (((SpaceObjectPositionChangedEvent) event).getId() == selectedSpaceObjectId) {
					propertiesBoxChanged = true;
				}
			} else if (event instanceof InventoryChangedEvent) {
				SpaceObject selectedSpaceObject = getSelectedSpaceObject();
				if (selectedSpaceObject instanceof ObjectWithInventory) {
					long eventInventoryId = ((InventoryChangedEvent) event).getInventoryId();
					long selectedSpaceObjectInventoryId = ((ObjectWithInventory) selectedSpaceObject).getInventoryId();
					if (eventInventoryId == selectedSpaceObjectInventoryId || eventInventoryId == getPlayer().getInventory().getId()) {
						propertiesBoxChanged = true;
					}
				}
			} else if (event instanceof PlayerShipEquipmentChangedEvent) {
				long playerShipId = ((PlayerShipEquipmentChangedEvent) event).getPlayerShipId();
				if (playerShipId == getPlayer().getShip().getId()) {
					propertiesBoxChanged = true;
				}
			}
		}

		// react to space objects changing their position (e.g. ships)
		if (anySpaceObjectChangedItsPosition) {
			StringBuilder builder = new StringBuilder();
			buildDynamicSpaceObjectsData(builder);
			builder.append("redrawDynamicSpaceObjects();");
			partialPageRequestHandler.appendJavaScript(builder.toString());
		}

		// react to changes in the side bar
		if (propertiesBoxChanged) {
			partialPageRequestHandler.add(get("propertiesBox"));
		}
	}

}
