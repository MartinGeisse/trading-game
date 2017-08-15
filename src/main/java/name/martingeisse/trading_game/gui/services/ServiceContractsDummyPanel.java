package name.martingeisse.trading_game.gui.services;

import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;

/**
 *
 */
public class ServiceContractsDummyPanel extends AlphaUsersOnlyPanel {

	public ServiceContractsDummyPanel(String id) {
		super(id);
	}

	@Override
	public String getHeadline() {
		return "Service Contracts";
	}

	@Override
	public String getDescription() {
		return "Service contracts allow you to offer services to other players, or hire other players for services. This	includes storage, transport, manufacturing, and other things.";
	}


}
