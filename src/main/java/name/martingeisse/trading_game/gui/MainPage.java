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
 * This is the main page that displays the game. All other game pages are usually displayed on JS-controlled popup
 * windows, iframes, etc. on the main page, although on the Wicket side they are treated as full-fledged pages. This
 * simplifies the handling of those pages as opposed to a single huge stateful page.
 *
 * TODO synchronize on the game object during rendering and callbacks
 */
public class MainPage extends AbstractPage {

	private final Game game;

	/**
	 * Constructor
	 */
	public MainPage() {
		this.game = new Game();
		add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));

		add(new Loop("rows", game.getMap().getWidth()) {
			@Override
			protected void populateItem(LoopItem rowItem) {
				rowItem.add(new Loop("cells", game.getMap().getHeight()) {

					@Override
					protected LoopItem newItem(int iteration) {
						if (rowItem.getIndex() == game.getPlayer().getY() && iteration == game.getPlayer().getX()) {
							return new LoopItem(iteration) {
								@Override
								public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
									super.onComponentTagBody(markupStream, openTag);
									getResponse().write("<img src=\"/wicket/resource/name/martingeisse/trading_game/gui/tiles/player.png\" />");
								}
							};
						} else {
							return super.newItem(iteration);
						}
					}

					@Override
					protected void populateItem(LoopItem cellItem) {
					}
				});
			}
		});
	}

}
