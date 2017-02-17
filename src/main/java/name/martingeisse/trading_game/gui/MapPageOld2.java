package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.resource.JQueryResourceReference;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MapPageOld2 extends AbstractPage {

	private final List<MapObject> mapObjects = new ArrayList<>();

	private String testText;

	public MapPageOld2() {

		// map
		WebMarkupContainer map = new WebMarkupContainer("map");
		add(map);
		map.add(new ListView<MapObject>("mapObjects", mapObjects) {
			@Override
			protected void populateItem(ListItem<MapObject> item) {
				MapObject mapObject = item.getModelObject();

				// place the object on the map
				item.add(new AttributeModifier("style", "transform: translate(" + mapObject.getX() + "px, " + mapObject.getY() + "px)"));

				// fill the object contents
				// Note: currently only square objects are supported since percentage-based negative y-margins use the
				// *width* (not height) as their calculation basis!
				item.add(new WebComponent("visual") {
					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.put("style", "width: 10px; height: 10px;");
					}
				});
				item.add(new Label("objectIdentifier", "" + item.getIndex()));
				item.add(new Label("name", mapObject.getName()));

			}
		});
		map.add(new AbstractDefaultAjaxBehavior() {

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				String script = "onSelectMapObject = " + getCallbackFunction(CallbackParameter.explicit("objectIdentifier"));
				response.render(JavaScriptHeaderItem.forScript(script, null));
			}

			@Override
			protected void respond(AjaxRequestTarget target) {
				String objectIdentifier = getRequest().getRequestParameters().getParameterValue("objectIdentifier").toString().trim();
				int index = Integer.parseInt(objectIdentifier);
				MapObject mapObject = mapObjects.get(index);
				testText = mapObject.getName() + " at " + mapObject.getX() + ", " + mapObject.getY();
				target.add(MapPageOld2.this.get("rightSidebar"));
			}

		});

		// right sidebar
		WebMarkupContainer rightSidebar = new WebMarkupContainer("rightSidebar");
		rightSidebar.setOutputMarkupId(true);
		add(rightSidebar);
		rightSidebar.add(new Label("test", new PropertyModel<>(this, "testText")));

	}

	/**
	 * Getter method.
	 *
	 * @return the testText
	 */
	public String getTestText() {
		return testText;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(new PriorityHeaderItem(JavaScriptHeaderItem.forReference(JQueryResourceReference.get())));
		super.renderHead(response);
	}

	private static class MapObject {

		private final long x;
		private final long y;
		private final String name;

		public MapObject(long x, long y, String name) {
			this.x = x;
			this.y = y;
			this.name = name;
		}

		/**
		 * Getter method.
		 *
		 * @return the x
		 */
		public long getX() {
			return x;
		}

		/**
		 * Getter method.
		 *
		 * @return the y
		 */
		public long getY() {
			return y;
		}

		/**
		 * Getter method.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}

	}

}
