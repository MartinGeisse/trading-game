/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.CraftingAction;
import name.martingeisse.trading_game.game.action.CreateRedPixelAction;
import name.martingeisse.trading_game.game.action.PlayerAction;
import name.martingeisse.trading_game.game.action.PlayerActionProgress;
import name.martingeisse.trading_game.game.crafting.CraftingRecipe;
import name.martingeisse.trading_game.game.item.ItemStack;
import name.martingeisse.trading_game.gui.item.ItemIcons;
import name.martingeisse.trading_game.gui.wicket.MyWicketApplication;
import name.martingeisse.trading_game.gui.wicket.MyWicketSession;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;

/**
 * This is the main page for controlling the game.
 */
public class MainPage extends AbstractPage {

	/**
	 * Constructor
	 */
	public MainPage() {
		add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));

		add(new Label("playerID", new PropertyModel<>(this, "player.id")));

		add(new Link<Void>("createRedPixelLink") {
			@Override
			public void onClick() {
				Player player = getPlayer();
				player.scheduleAction(new CreateRedPixelAction(player));
			}
		});
		add(new Link<Void>("createRedPixelAssemblyLink") {
			@Override
			public void onClick() {
				Player player = getPlayer();
				player.scheduleAction(new CraftingAction(player, CraftingRecipe.RED_PIXEL_ASSEMBLY));
			}
		});

		add(new Label("currentAction", new PropertyModel<>(this, "player.actionProgress.action")) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getDefaultModelObject() != null);
			}
		});
		add(new WebComponent("currentActionProgressBar") {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				PlayerActionProgress actionProgress = getPlayer().getActionProgress();
				if (actionProgress != null) {
					tag.put("style", "width: " + actionProgress.getProgressPoints() * 100 / actionProgress.getAction().getRequiredProgressPoints() + "%");
				}
			}
		});
		add(new Link<Void>("cancelCurrentActionLink") {
			@Override
			public void onClick() {
				getPlayer().cancelCurrentAction();
			}
		});

		add(new ListView<PlayerAction>("pendingActions", new PropertyModel<>(this, "player.pendingActions")) {
			@Override
			protected void populateItem(ListItem<PlayerAction> item) {
				item.add(new Label("text", item.getModelObject().toString()));
				item.add(new Link<Void>("cancelLink") {
					@Override
					public void onClick() {
						getPlayer().cancelPendingAction(item.getIndex());
					}
				});
			}
		});

		add(new ListView<ItemStack>("itemStacks", getPlayer().getInventory().getItemStacks()) {
			@Override
			protected void populateItem(ListItem<ItemStack> item) {
				item.add(new Label("size", "" + item.getModelObject().getSize()));
				item.add(new Label("itemType", "" + item.getModelObject().getItemType()));
				item.add(new Image("icon", ItemIcons.get(item.getModelObject().getItemType())));
			}
		});

	}

	public Game getGame() {
		return MyWicketApplication.get().getDependency(Game.class);
	}

	public Player getPlayer() {
		return MyWicketSession.get().getPlayer();
	}

}
