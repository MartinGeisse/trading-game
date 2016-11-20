/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.util.time.Duration;

/**
 * This is the main page for controlling the game.
 */
public class MainPage extends AbstractPage {

	private final Game game;

	/**
	 * Constructor
	 */
	public MainPage() {
		this.game = new Game();
		add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));
	}

}
