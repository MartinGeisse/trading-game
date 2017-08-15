package name.martingeisse.trading_game.gui.manufacturing;

import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;

/**
 *
 */
public class ManufacturingSectionDummyPanel extends AlphaUsersOnlyPanel {

	public ManufacturingSectionDummyPanel(String id) {
		super(id);
	}

	@Override
	public String getHeadline() {
		return "Manufacturing";
	}

	@Override
	public String getDescription() {
		return "Here you can produce items from raw materials. The raw materials must be mined from asteroids or bought on the market. You'll also need a manufacturing plant which you can rent at a space station.";
	}

}
