package name.martingeisse.trading_game.gui.remote_drones;

import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;

/**
 *
 */
public class RemoteDronesDummyPanel extends AlphaUsersOnlyPanel {

	public RemoteDronesDummyPanel(String id) {
		super(id);
	}

	@Override
	public String getHeadline() {
		return "Remote Drones";
	}

	@Override
	public String getDescription() {
		return "Remote drones perform mining, farming, contruction and other tasks on your behalf, somewhere else in the universe without requiring your presence.";
	}


}
