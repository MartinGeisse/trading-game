package name.martingeisse.trading_game.gui.brokerage;

import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;

/**
 *
 */
public class BrokerageDummyPanel extends AlphaUsersOnlyPanel {

	public BrokerageDummyPanel(String id) {
		super(id);
	}

	@Override
	public String getHeadline() {
		return "Brokerage";
	}

	@Override
	public String getDescription() {
		return "Here you can hire a broker, contact a broker, or act as a broker your self, for goods and services. Good brokers will usually find a better deal on the market and save you some time in the process.";
	}


}
