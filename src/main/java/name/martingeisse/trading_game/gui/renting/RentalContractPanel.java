package name.martingeisse.trading_game.gui.renting;

import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;

/**
 *
 */
public class RentalContractPanel extends AlphaUsersOnlyPanel {

	public RentalContractPanel(String id) {
		super(id);
	}

	@Override
	public String getHeadline() {
		return "Rental Contracts";
	}

	@Override
	public String getDescription() {
		return "Here you can rent, or offer for rent, property such as storage or manufacturing plants.";
	}


}
