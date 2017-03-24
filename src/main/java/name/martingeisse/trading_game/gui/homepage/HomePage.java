package name.martingeisse.trading_game.gui.homepage;

import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.gui.gamepage.GamePage;
import name.martingeisse.trading_game.platform.wicket.LoginCookieUtil;
import name.martingeisse.trading_game.platform.wicket.MyWicketSession;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.model.PropertyModel;

/**
 *
 */
public class HomePage extends AbstractPage {

	public HomePage() {
		add(new StatelessLink<Void>("createPlayerLink") {

			@Override
			public void onClick() {
				MyWicketSession.get().createPlayer();
				LoginCookieUtil.sendCookie(MyWicketSession.get().getPlayer().getLoginToken());
				redirectToGame();
			}

		});
		add(new StatelessLink<Void>("continueImplicitLink") {

			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getCurrentPlayer() != null);
			}

			@Override
			public void onClick() {
				redirectToGame();
			}

		}.add(new Label("name", new PropertyModel<>(this, "currentPlayer.name"))));
		add(new StatelessLink<Void>("continueExplicitLink") {

			@Override
			public void onClick() {
				// TODO
				throw new UnsupportedOperationException("not yet implemented");
			}

		});
	}

	private void redirectToGame() {
		if (getCurrentPlayer() == null) {
			throw new IllegalStateException("no current player");
		}
		setResponsePage(GamePage.class);
	}

	public Player getCurrentPlayer() {
		return MyWicketSession.get().getPlayer();
	}

	@Override
	protected void onConfigure() {
		MyWicketSession.get().setPlayerId(LoginCookieUtil.getPlayerIdFromCookie());
		super.onConfigure();
	}

}
