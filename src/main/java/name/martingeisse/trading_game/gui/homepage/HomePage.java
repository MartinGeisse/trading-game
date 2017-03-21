package name.martingeisse.trading_game.gui.homepage;

import name.martingeisse.trading_game.gui.gamepage.GamePage;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;

/**
 *
 */
public class HomePage extends AbstractPage {

	public HomePage() {
		add(new StatelessLink<Void>("createPlayerLink") {
			@Override
			public void onClick() {
				// TODO
				setResponsePage(GamePage.class);
			}
		});
		add(new StatelessLink<Void>("continueImplicitLink") {
			@Override
			public void onClick() {
				// TODO
				setResponsePage(GamePage.class);
			}
		}.add(new Label("name", "TODO")));
		add(new StatelessLink<Void>("continueExplicitLink") {
			@Override
			public void onClick() {
				// TODO
				setResponsePage(GamePage.class);
			}
		});
	}
}
