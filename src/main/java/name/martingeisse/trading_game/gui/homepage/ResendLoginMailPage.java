package name.martingeisse.trading_game.gui.homepage;

import com.google.common.collect.ImmutableList;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.player.PlayerRepository;
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
public class ResendLoginMailPage extends AbstractPage {

	private String emailAddress;

	public ResendLoginMailPage() {
		SimpleFormPanel<ResendLoginMailPage> formPanel = new SimpleFormPanel<ResendLoginMailPage>("formPanel", Model.of(this)) {
			@Override
			protected void onSubmit() {
				ImmutableList<String> loginTokens = MyWicketApplication.get().getDependency(PlayerRepository.class).getLoginTokensByEmailAddress(emailAddress);
				System.out.println("* send email to " + emailAddress + " with the following tokens:");
				if (loginTokens.isEmpty()) {
					System.out.println("** message explaining that no player were bound to that address, via email (not on screen!)");
				} else {
					for (String loginToken : loginTokens) {
						System.out.println("** " + loginToken);
					}
				}
				setResponsePage(HomePage.class);
			}
		};
		formPanel.prepareDecorator().withLabel("Email address").withModel("emailAddress").addTextField();
		formPanel.prepareSpecialFormBlock().withText("Send").addSubmitButton();
		add(formPanel);
	}

	/**
	 * Getter method.
	 *
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Setter method.
	 *
	 * @param emailAddress the emailAddress
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
