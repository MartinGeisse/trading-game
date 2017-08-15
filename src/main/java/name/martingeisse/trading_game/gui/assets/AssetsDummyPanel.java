package name.martingeisse.trading_game.gui.assets;

import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;

/**
 *
 */
public class AssetsDummyPanel extends AlphaUsersOnlyPanel {

	public AssetsDummyPanel(String id) {
		super(id);
	}

	@Override
	public String getHeadline() {
		return "Assets";
	}

	@Override
	public String getDescription() {
		return "Here you can manage the storage, manufacturing plants, research sites etc. that you own.";
	}


}
