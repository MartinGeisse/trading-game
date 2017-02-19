/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main page for controlling the game.
 */
public class PlayerListPage extends AbstractPage {

	private transient List<Player> players;

	/**
	 * Constructor
	 */
	public PlayerListPage() {
		add(new ListView<Player>("players", new PropertyModel<>(this, "players")) {
			@Override
			protected void populateItem(ListItem<Player> item) {
				item.add(new Label("name", item.getModelObject().getName()));
			}
		});
	}

	/**
	 * Getter method.
	 *
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	@Override
	protected void onBeforeRender() {
		players = new ArrayList<>(getGame().getPlayers());
		super.onBeforeRender();
	}

}
