package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.game.generate.StarNaming;
import name.martingeisse.trading_game.game.generate.StarPlacement;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.resource.JQueryResourceReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class MapPage extends AbstractPage {

	private final List<MapObject> mapObjects = new ArrayList<>();

	private String testText;

	public MapPage() {

		// test data TODO remove
		for (Pair<Long, Long> starPosition : StarPlacement.compute(100, 200, 2, 3000)) {
			long x = starPosition.getLeft();
			long y = starPosition.getRight();
			String name = StarNaming.compute();
			mapObjects.add(new MapObject(x, y, name));
		}

		// map
		WebMarkupContainer map = new WebMarkupContainer("map");
		add(map);
		map.add(new ListView<MapObject>("mapObjects", mapObjects) {
			@Override
			protected void populateItem(ListItem<MapObject> item) {
				MapObject mapObject = item.getModelObject();
				// Note: currently only square objects are supported since percentage-based negative y-margins use the
				// *width* (not height) as their calculation basis!
				item.add(new AttributeModifier("style", "transform: translate(" + mapObject.getX() + "px, " + mapObject.getY() + "px); width: 10px; height: 10px;"));
				item.add(new Label("objectIdentifier", "" + item.getIndex()));
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
				target.add(MapPage.this.get("rightSidebar"));
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
