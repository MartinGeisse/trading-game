/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.CreateRedPixelAction;
import name.martingeisse.trading_game.game.action.PlayerAction;
import name.martingeisse.trading_game.game.action.PlayerActionProgress;
import name.martingeisse.trading_game.game.item.ItemStack;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.model.PropertyModel;
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


		add(new Link<Void>("createRedPixelLink") {
			@Override
			public void onClick() {
				Player player = game.getPlayer();
				player.scheduleAction(new CreateRedPixelAction(player));
			}
		});

		add(new Label("currentAction", new PropertyModel<>(game, "player.actionProgress.action")) {
			@Override
			public boolean isVisible() {
				return (getDefaultModelObject() != null);
			}
		});
		add(new WebComponent("currentActionProgressBar") {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				PlayerActionProgress actionProgress = game.getPlayer().getActionProgress();
				if (actionProgress != null) {
					tag.put("style", "width: " + actionProgress.getProgressPoints() * 100 / actionProgress.getAction().getRequiredProgressPoints() + "%");
				}
			}
		});
		add(new Link<Void>("cancelCurrentActionLink") {
			@Override
			public void onClick() {
				game.getPlayer().cancelCurrentAction();
			}
		});

		add(new ListView<PlayerAction>("pendingActions", new PropertyModel<>(game, "player.pendingActions")) {
			@Override
			protected void populateItem(ListItem<PlayerAction> item) {
				item.add(new Label("text", item.getModelObject().toString()));
				item.add(new Link<Void>("cancelLink") {
					@Override
					public void onClick() {
						game.getPlayer().cancelPendingAction(item.getIndex());
					}
				});
			}
		});

		add(new ListView<ItemStack>("itemStacks", game.getPlayer().getInventory().getItemStacks()) {
			@Override
			protected void populateItem(ListItem<ItemStack> item) {
				item.add(new Label("size", "" + item.getModelObject().getSize()));
				item.add(new Label("itemType", "" + item.getModelObject().getItemType()));
			}
		});

	}

}
