package name.martingeisse.trading_game.gui.account;

import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;

/**
 *
 */
public class AccountPanel extends AlphaUsersOnlyPanel {

	public AccountPanel(String id) {
		super(id);
	}

	@Override
	public String getHeadline() {
		return "Account";
	}

	@Override
	public String getDescription() {
		return "Here you can manage your game account.";
	}


}
