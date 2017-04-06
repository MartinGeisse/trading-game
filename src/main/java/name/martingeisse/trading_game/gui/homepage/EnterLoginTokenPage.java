package name.martingeisse.trading_game.gui.homepage;

import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerLoginRepository;
import name.martingeisse.trading_game.gui.gamepage.GamePage;
import name.martingeisse.trading_game.platform.wicket.LoginCookieUtil;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import name.martingeisse.trading_game.platform.wicket.MyWicketSession;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import name.martingeisse.wicket.simpleform.SimpleFormPanel;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;

/**
 *
 */
public class EnterLoginTokenPage extends AbstractPage {

	private String loginToken;

	public EnterLoginTokenPage() {
		SimpleFormPanel<EnterLoginTokenPage> formPanel = new SimpleFormPanel<EnterLoginTokenPage>("formPanel", Model.of(this)) {
			@Override
			protected void onSubmit() {
				Player selectedPlayer;
				try {
					selectedPlayer = MyWicketApplication.get().getDependency(PlayerLoginRepository.class).getPlayerByLoginToken(loginToken);
				} catch (IllegalArgumentException e) {
					FormComponent<?> formComponent = (FormComponent<?>)get("form:formBlocks:1:decoratedBody:textField");
					formComponent.error("invalid login token");
					return;
				}
				Player existingPlayer = MyWicketSession.get().getPlayer();
				if (existingPlayer == null) {
					MyWicketSession.get().setPlayerId(selectedPlayer.getId());
					LoginCookieUtil.sendCookie(loginToken);
					setResponsePage(GamePage.class);
				} else {
					setResponsePage(new LoginOverwriteWarningPage(existingPlayer.getId(), selectedPlayer.getId()));
				}
			}
		};
		formPanel.prepareDecorator().withLabel("Login Token").withModel("loginToken").withRequiredness(true).addTextField();
		formPanel.prepareSpecialFormBlock().withText("Log In").addSubmitButton();
		add(formPanel);
	}

	/**
	 * Getter method.
	 *
	 * @return the loginToken
	 */
	public String getLoginToken() {
		return loginToken;
	}

	/**
	 * Setter method.
	 *
	 * @param loginToken the loginToken
	 */
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

}
