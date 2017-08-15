package name.martingeisse.trading_game.gui.vanity;

import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;

/**
 *
 */
public class VanityDummyPanel extends AlphaUsersOnlyPanel {

	public VanityDummyPanel(String id) {
		super(id);
	}

	@Override
	public String getHeadline() {
		return "Vanity";
	}

	@Override
	public String getDescription() {
		return "Leave a lasting impression on the universe. Or be forgotten tomorrow.";
	}

}
