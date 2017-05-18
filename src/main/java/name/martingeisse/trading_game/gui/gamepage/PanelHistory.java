package name.martingeisse.trading_game.gui.gamepage;

import org.apache.wicket.markup.html.WebMarkupContainer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This object is part of the GamePage and stores current and previously visible panels to support in-page back button
 * navigation (via the Javascript history API).
 * <p>
 * The purpose of this class itself is only to store the panels.
 */
public class PanelHistory implements Serializable {

	private final List<WebMarkupContainer> panels = new ArrayList<>();

	/**
	 * Adds a panel to this history, assigns an ID, and returns the ID.
	 */
	public int addPanel(WebMarkupContainer panel) {
		int id = panels.size();
		panels.add(panel);
		return id;
	}

	/**
	 * Gets a panel by ID. Returns null if not found.
	 */
	public WebMarkupContainer getPanel(int id) {
		if (id < 0 || id >= panels.size()) {
			return null;
		}
		return panels.get(id);
	}

}
