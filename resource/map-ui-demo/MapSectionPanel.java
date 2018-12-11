
/**
 * Encapsulates the actual map view and the properties box.
 */
public class MapSectionPanel extends AbstractPanel implements GuiGameEventListener {

	public MapSectionPanel(String id) {
		super(id);

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
						break;
					}

					case "unselect": {
						selectedSpaceObjectId = -1;
						break;
					}

				}
			}

		});

	}

	private void buildStaticSpaceObjectsData(StringBuilder builder) {
		if (GuiCache.staticSpaceObjectData == null) {
			StringBuilder subBuilder = new StringBuilder();
			subBuilder.append("staticSpaceObjects = {\n");
			boolean first = true;
			for (StaticSpaceObject spaceObject : getSpace().getStaticSpaceObjects()) {
				if (first) {
					first = false;
				} else {
					subBuilder.append(',');
				}
				buildSpaceObjectData(subBuilder, spaceObject, null);
			}
			subBuilder.append("};\n");
			GuiCache.staticSpaceObjectData = subBuilder.toString();
		}
		builder.append(GuiCache.staticSpaceObjectData);
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
			buildSpaceObjectData(builder, spaceObject, spaceObject.getMovementInfo());
		}
		builder.append("};\n");
	}

	private void buildSpaceObjectData(StringBuilder builder, SpaceObject spaceObject, MovementInfo movementInfo) {

		String renderFunction;
		if (spaceObject.getId() == getPlayer().getShip().getId()) {
			renderFunction = "self";
		} else {
			renderFunction = SPACE_OBJECT_RENDER_MAPPING.get(SpaceObjectType.getType(spaceObject));
		}

		builder.append('\'').append(spaceObject.getId()).append("': {");
		builder.append("id: '").append(spaceObject.getId()).append('\'');
		builder.append(", x: ").append(spaceObject.getX());
		builder.append(", y: ").append(spaceObject.getY());
		builder.append(", r: ").append(spaceObject.getRadius());
		builder.append(", f: '").append(renderFunction).append('\'');
		if (movementInfo != null) {
			builder.append(", x2: ").append(movementInfo.getDestinationX());
			builder.append(", y2: ").append(movementInfo.getDestinationY());
			builder.append(", t: ").append(movementInfo.getRemainingTime() + 1); // usually too low by 1 due to rounding errors
		}
		builder.append("}");
	}

}
