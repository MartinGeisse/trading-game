package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.resource.JQueryResourceReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 */
public class MapPage extends AbstractPage {

	private final List<MapObject> mapObjects = new ArrayList<>();

	public MapPage() {
		add(new ListView<MapObject>("mapObjects", mapObjects) {
			@Override
			protected void populateItem(ListItem<MapObject> item) {
				MapObject mapObject = item.getModelObject();
				item.add(new AttributeModifier("style", "transform: translate(" + mapObject.getX() + "px, " + mapObject.getY() + "px);"));
				item.add(new Label("text", "" + mapObject.getX() + "," + mapObject.getY()));
			}
		});
		Random random = new Random();
		for (int i=0; i<100; i++) {
			mapObjects.add(new MapObject(random.nextLong() % 1000, random.nextLong() % 1000));
		}
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(new PriorityHeaderItem(JavaScriptHeaderItem.forReference(JQueryResourceReference.get())));
		super.renderHead(response);
	}

	private static class MapObject {

		private final long x;
		private final long y;

		public MapObject(long x, long y) {
			this.x = x;
			this.y = y;
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

	}

}
