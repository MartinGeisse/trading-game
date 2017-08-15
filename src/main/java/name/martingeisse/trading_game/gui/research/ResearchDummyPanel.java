package name.martingeisse.trading_game.gui.research;

import name.martingeisse.trading_game.gui.components.AlphaUsersOnlyPanel;

/**
 *
 */
public class ResearchDummyPanel extends AlphaUsersOnlyPanel {

	public ResearchDummyPanel(String id) {
		super(id);
	}

	@Override
	public String getHeadline() {
		return "Research";
	}

	@Override
	public String getDescription() {
		return "Here you can develop new items to manufacture, optimized versions of existing items, new or improved manufacturing processes, and other things.";
	}


}
