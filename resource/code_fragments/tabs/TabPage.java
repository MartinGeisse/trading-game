
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Arrays;

/**
 *
 */
public class TabPage extends AbstractApplicationPage {

	public TabPage() {
		SimpleTab tab1 = new SimpleTab("Eins", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam cursus risus ante, eget ultrices erat suscipit ut. Vestibulum libero dui, euismod ut suscipit non, cursus non diam. Vestibulum at vulputate sem. Vestibulum ac turpis commodo, venenatis lorem finibus, facilisis eros. Mauris laoreet bibendum diam ut interdum. Nullam mattis sem sed leo suscipit, vitae facilisis nisi congue. Vivamus ac tellus diam. Phasellus ultrices risus eu ligula congue fringilla. Nunc porttitor purus vitae est feugiat congue. Nunc ornare vitae lorem a dictum. Integer mollis tristique nibh, vitae sollicitudin ligula. Praesent consequat, mauris sit amet elementum finibus, justo turpis sollicitudin nisi, a volutpat ipsum lorem vel arcu. Integer egestas accumsan tortor. Vestibulum ligula lorem, luctus et imperdiet eget, tempus sit amet quam. Morbi ultricies est eu sollicitudin dictum.");
		SimpleTab tab2 = new SimpleTab("Zwei", "Proin pharetra aliquam fringilla. Sed et lorem non ante ullamcorper fringilla. Praesent imperdiet cursus turpis, nec iaculis leo. Vestibulum pretium diam eu felis laoreet, ut tristique enim commodo. Duis egestas, velit non mollis luctus, diam libero vehicula dui, in tincidunt arcu dolor vitae nisl. Aliquam id elit quis risus laoreet dictum. Integer rhoncus convallis lectus sit amet suscipit. Nam pellentesque feugiat convallis. Nam tempus accumsan dolor nec finibus. Nunc ac condimentum erat. Suspendisse quis vehicula lectus. Nam blandit sollicitudin lacus, et ullamcorper lectus.");
		SimpleTab tab3 = new SimpleTab("Drei", "Nam dapibus urna sit amet sem hendrerit eleifend. Nullam tincidunt feugiat pretium. Aenean dapibus leo et consequat consequat. Vivamus sollicitudin faucibus tempus. Suspendisse eget arcu quam. Phasellus suscipit accumsan turpis. Aenean mollis fringilla tortor, nec finibus ligula molestie eu. Nulla aliquet ante a tortor accumsan, in semper tellus congue. Sed commodo maximus rutrum.");
		add(new MainMenuTabbedPanel<SimpleTab>("tabbedPanel", Arrays.asList(tab1, tab2, tab3)));
	}

	public class SimpleTab extends AbstractTab {

		private final String title;
		private final String content;

		public SimpleTab(String title, String content) {
			super(Model.of(title));
			this.title = title;
			this.content = content;
		}

		@Override
		public WebMarkupContainer getPanel(String panelId) {
			Fragment fragment = new Fragment(panelId, "textFragment", TabPage.this);
			fragment.add(new Label("text", Model.of(content)));
			return fragment;
		}

	}

}
