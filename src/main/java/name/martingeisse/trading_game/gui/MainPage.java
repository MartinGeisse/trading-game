/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.ContextFreeActionDefinition;
import name.martingeisse.trading_game.game.action.PlayerAction;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.FixedItemStack;
import name.martingeisse.trading_game.game.item.ItemStack;
import name.martingeisse.trading_game.gui.item.ItemIcons;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import name.martingeisse.wicket.helpers.InlineProgressBar;
import name.martingeisse.wicket.helpers.InvisibleWebComponent;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
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

		add(new Label("playerName", new PropertyModel<>(this, "player.name")));
		add(new Label("playerID", new PropertyModel<>(this, "player.id")));

		add(new ListView<ContextFreeActionDefinition>("contextFreeActionDefinitions", gameDefinitionModel("contextFreeActionDefinitions")) {
			@Override
			protected void populateItem(ListItem<ContextFreeActionDefinition> item) {
				Link<?> link = new Link<Void>("link") {
					@Override
					public void onClick() {
						Player player = getPlayer();
						player.scheduleAction(item.getModelObject().getFactory().apply(player));
					}
				};
				link.add(new Label("name", item.getModelObject().getName()));
				item.add(link);
				FixedInventory billOfMaterials = item.getModelObject().getBillOfMaterials();
				if (billOfMaterials == null || billOfMaterials.getItemStacks().isEmpty()) {
					item.add(new InvisibleWebComponent("billOfMaterials"));
				} else {
					item.add(new ListView<FixedItemStack>("billOfMaterials", billOfMaterials.getItemStacks()) {
						@Override
						protected void populateItem(ListItem<FixedItemStack> item) {
							item.add(new Label("amount", item.getModelObject().getSize()));
							item.add(new Label("name", item.getModelObject().getItemType().getName()));
						}
					});
				}
			}
		});

		add(new Label("currentAction", new PropertyModel<>(this, "player.actionProgress.action")) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getDefaultModelObject() != null);
			}
		});
		add(new InlineProgressBar("currentActionProgressBar", new PropertyModel<>(this, "player.actionProgress.progressPoints"))
				.setTotalAmountModel(new PropertyModel<>(this, "player.actionProgress.action.requiredProgressPoints")));
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

		add(new BookmarkablePageLink<>("playerListLink", PlayerListPage.class));
		add(new BookmarkablePageLink<>("renamePlayerLink", RenamePlayerPage.class));

	}

}
