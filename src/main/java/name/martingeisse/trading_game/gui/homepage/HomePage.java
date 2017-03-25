package name.martingeisse.trading_game.gui.homepage;

import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.gui.gamepage.GamePage;
import name.martingeisse.trading_game.platform.wicket.LoginCookieUtil;
import name.martingeisse.trading_game.platform.wicket.MyWicketSession;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
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
				Player existingPlayer = MyWicketSession.get().getPlayer();
				if (existingPlayer == null) {
					MyWicketSession.get().createPlayer();
					LoginCookieUtil.sendCookie(MyWicketSession.get().getPlayer().getLoginToken());
					setResponsePage(GamePage.class);
				} else {
					setResponsePage(new CreatePlayerOverwriteWarningPage(existingPlayer.getId()));
				}
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
				if (getCurrentPlayer() != null) {
					setResponsePage(GamePage.class);
				}
			}

		}.add(new Label("name", new PropertyModel<>(this, "currentPlayer.name"))));
		add(new BookmarkablePageLink<>("continueExplicitLink", EnterLoginTokenPage.class));
		add(new BookmarkablePageLink<>("resendLoginEmailLink", ResendLoginMailPage.class));
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
