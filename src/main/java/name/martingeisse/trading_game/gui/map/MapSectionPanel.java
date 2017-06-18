package name.martingeisse.trading_game.gui.map;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.event.GameEvent;
import name.martingeisse.trading_game.game.event.GameEventBatch;
import name.martingeisse.trading_game.game.item.InventoryChangedEvent;
import name.martingeisse.trading_game.game.item.ObjectWithInventory;
import name.martingeisse.trading_game.game.space.*;
import name.martingeisse.trading_game.gui.gamepage.GuiNavigationLink;
import name.martingeisse.trading_game.gui.websockets.GuiGameEventListener;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import name.martingeisse.wicket.bootstrap.GlyphiconComponent;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.List;

/**
 * Encapsulates the actual map view and the properties box.
 */
public class MapSectionPanel extends AbstractPanel implements GuiGameEventListener {

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
				link.add(new GlyphiconComponent("icon", actionItem.getModel()) {
					@Override
					protected String getGlyphiconIdentifier() {
						return ((Action)getDefaultModelObject()).getGlyphiconName();
					}
				});
				actionItem.add(link);
			}
		});
		propertiesBox.add(new GuiNavigationLink("spaceObjectDetailLink") {
			@Override
			protected WebMarkupContainer getPanel(String panelId) {
				return new SpaceObjectDetailPanel(panelId, new PropertyModel<>(MapSectionPanel.this, "selectedSpaceObject"));
			}
		});

		add(new AbstractDefaultAjaxBehavior() {

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);

				// build callback functions
				{
					StringBuilder builder = new StringBuilder();

					// directly selecting any object by ID
					{
						CallbackParameter[] parameters = {
								CallbackParameter.resolved("command", "'select'"),
								CallbackParameter.explicit("id"),
						};
						builder.append("sendSelectCommand = ").append(getCallbackFunction(parameters)).append(';');
					}

					// unselect the currently selected object
					{
						CallbackParameter[] parameters = {
								CallbackParameter.resolved("command", "'unselect'")
						};
						builder.append("sendUnselectCommand = ").append(getCallbackFunction(parameters)).append(';');
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

					case "select": {
						long idToSelect = parameters.getParameterValue("id").toLong();
						try {
							if (getSpace().get(idToSelect) == null) {
								idToSelect = -1;
							}
						} catch (IllegalArgumentException e) {
							idToSelect = -1;
						}
						selectedSpaceObjectId = idToSelect;
						target.add(get("propertiesBox"));
						break;
					}

					case "unselect": {
						selectedSpaceObjectId = -1;
						target.add(get("propertiesBox"));
						break;
					}

				}
			}

		});

	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		// include AwesomeMap JS library
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(MapSectionPanel.class, "general.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(MapSectionPanel.class, "AwesomeMap.js")));

		// render initialization script
		StringBuilder builder = new StringBuilder();
		builder.append("playerShipId = '").append(getPlayer().getShip().getId()).append("';");
		buildStaticSpaceObjectsData(builder);
		buildDynamicSpaceObjectsData(builder);
		builder.append("initializeMapSectionPanel();");
		response.render(JavaScriptHeaderItem.forScript(builder.toString(), null));

	}

	private void buildStaticSpaceObjectsData(StringBuilder builder) {
		builder.append("staticSpaceObjects = {\n");
		boolean first = true;
		for (StaticSpaceObject spaceObject : getSpace().getStaticSpaceObjects()) {
			if (first) {
				first = false;
			} else {
				builder.append(',');
			}
			buildSpaceObjectData(builder, spaceObject, null, "#cccccc");
		}
		builder.append("};\n");
	}

	private void buildDynamicSpaceObjectsData(StringBuilder builder) {
		builder.append("dynamicSpaceObjects = {\n");
		boolean first = true;
		for (DynamicSpaceObject spaceObject : getSpace().getDynamicSpaceObjects()) {
			if (first) {
				first = false;
			} else {
				builder.append(',');
			}
			buildSpaceObjectData(builder, spaceObject, spaceObject.getMovementInfo(), spaceObject instanceof PlayerShip ? "#00ffff" : "#0000ff");
		}
		builder.append("};\n");
	}

	private void buildSpaceObjectData(StringBuilder builder, SpaceObject spaceObject, MovementInfo movementInfo, String colorCode) {
		builder.append('\'').append(spaceObject.getId()).append("': {");
		builder.append("id: '").append(spaceObject.getId()).append('\'');
		builder.append(", x: ").append(MapCoordinates.convertXToLongitude(spaceObject.getX()));
		builder.append(", y: ").append(MapCoordinates.convertYToLatitude(spaceObject.getY()));
		builder.append(", r: ").append(MapCoordinates.convertGameDistanceToMapDistance(spaceObject.getRadius()));
		builder.append(", c: '").append(colorCode).append('\'');
		if (movementInfo != null) {
			builder.append(", x2: ").append(MapCoordinates.convertXToLongitude(movementInfo.getDestinationX()));
			builder.append(", y2: ").append(MapCoordinates.convertYToLatitude(movementInfo.getDestinationY()));
			builder.append(", t: ").append(movementInfo.getRemainingTime() + 1); // usually too low by 1 due to rounding errors
		}
		builder.append("}");
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
			}
		}

		// react to space objects changing their position (e.g. ships)
		if (anySpaceObjectChangedItsPosition) {
			StringBuilder builder = new StringBuilder();
			buildDynamicSpaceObjectsData(builder);
			builder.append("updateDynamicSpaceObjectsOnMap();\n");
			partialPageRequestHandler.appendJavaScript(builder.toString());
		}

		// react to changes in the side bar
		if (propertiesBoxChanged) {
			partialPageRequestHandler.add(get("propertiesBox"));
		}
	}

}
