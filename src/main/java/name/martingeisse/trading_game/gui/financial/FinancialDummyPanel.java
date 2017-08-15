package name.martingeisse.trading_game.gui.financial;

import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;

/**
 *
 */
public class FinancialDummyPanel extends AlphaUsersOnlyPanel {

	public FinancialDummyPanel(String id) {
		super(id);
	}

	@Override
	public String getHeadline() {
		return "Financial";
	}

	@Override
	public String getDescription() {
		return "Here you can borrow or lend money, trade company shares, and engage in other financial business.";
	}


}
