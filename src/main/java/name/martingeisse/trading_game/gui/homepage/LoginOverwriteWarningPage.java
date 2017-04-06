package name.martingeisse.trading_game.gui.homepage;

import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.gui.gamepage.GamePage;
import name.martingeisse.trading_game.platform.wicket.LoginCookieUtil;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import name.martingeisse.trading_game.platform.wicket.MyWicketSession;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

/**
 *
 */
public class LoginOverwriteWarningPage extends AbstractPage {

	private final PlayerFromIdModel oldPlayerModel;
	private final PlayerFromIdModel newPlayerModel;

	public LoginOverwriteWarningPage(long oldPlayerId, long newPlayerId) {
		this.oldPlayerModel = new PlayerFromIdModel(oldPlayerId);
		this.newPlayerModel = new PlayerFromIdModel(newPlayerId);

		add(new Label("oldName", new PropertyModel<>(oldPlayerModel, "name")));
		add(new Label("newName", new PropertyModel<>(newPlayerModel, "name")));
		add(new NotBoundToEmailWarningPanel("notBoundToEmailWarning", oldPlayerModel));
		add(new Link<Void>("loginLink") {
			@Override
			public void onClick() {
				MyWicketSession.get().setPlayerId(newPlayerModel.getPlayerId());
				LoginCookieUtil.sendCookie(newPlayerModel.getObject().getLoginToken());
				setResponsePage(GamePage.class);
			}
		}.add(new Label("newName", new PropertyModel<>(newPlayerModel, "name"))));
		add(new BookmarkablePageLink<>("backLink", HomePage.class));
	}

	private static class PlayerFromIdModel extends LoadableDetachableModel<Player> {
		private final long playerId;

		public PlayerFromIdModel(long playerId) {
			this.playerId = playerId;
		}

		public long getPlayerId() {
			return playerId;
		}

		@Override
		protected Player load() {
			return MyWicketApplication.get().getDependency(EntityProvider.class).getPlayer(playerId);
		}

	}
}
