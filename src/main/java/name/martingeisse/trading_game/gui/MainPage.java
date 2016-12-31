/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.action.ContextFreeActionDefinition;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.FixedItemStack;
import name.martingeisse.trading_game.game.item.ItemStack;
import name.martingeisse.trading_game.game.skill.Skill;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.gui.item.ItemIcons;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import name.martingeisse.wicket.helpers.InlineProgressBar;
import name.martingeisse.wicket.helpers.InvisibleWebComponent;
import name.martingeisse.wicket.helpers.ProgressBarClientProgressBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is the main page for controlling the game.
 */
public class MainPage extends AbstractPage {

	/**
	 * Constructor
	 */
	public MainPage() {

		WebMarkupContainer playerContainer = new WebMarkupContainer("playerContainer");
		playerContainer.setOutputMarkupId(true);
		playerContainer.add(new Label("playerName", new PropertyModel<>(this, "player.name")));
		playerContainer.add(new Label("playerX", new PropertyModel<>(this, "player.ship.x")));
		playerContainer.add(new Label("playerY", new PropertyModel<>(this, "player.ship.y")));
		playerContainer.add(new Label("playerID", new PropertyModel<>(this, "player.id")));
		playerContainer.add(new BookmarkablePageLink<>("playerListLink", PlayerListPage.class));
		playerContainer.add(new BookmarkablePageLink<>("renamePlayerLink", RenamePlayerPage.class));
		playerContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));
		add(playerContainer);

		add(new ListView<ContextFreeActionDefinition>("contextFreeActionDefinitions", gameDefinitionModel("contextFreeActionDefinitions")) {

			@Override
			protected void populateItem(ListItem<ContextFreeActionDefinition> item) {
				AjaxLink<?> link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						Player player = getPlayer();
						player.scheduleAction(item.getModelObject().getFactory().apply(player));
						target.add(MainPage.this.get("currentActionContainer"));
						target.add(MainPage.this.get("pendingActionsContainer"));
						target.add(MainPage.this.get("inventoryContainer"));
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

				FixedInventory yield = item.getModelObject().getYield();
				if (yield == null || yield.getItemStacks().isEmpty()) {
					item.add(new InvisibleWebComponent("yield"));
				} else {
					item.add(new ListView<FixedItemStack>("yield", yield.getItemStacks()) {
						@Override
						protected void populateItem(ListItem<FixedItemStack> item) {
							item.add(new Label("amount", item.getModelObject().getSize()));
							item.add(new Label("name", item.getModelObject().getItemType().getName()));
						}
					});
				}
			}

		});

		WebMarkupContainer currentActionContainer = new WebMarkupContainer("currentActionContainer");
		add(currentActionContainer);
		currentActionContainer.add(new Label("currentAction", new PropertyModel<>(this, "player.actionExecution.name")) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getDefaultModelObject() != null);
			}
		});
		IModel<Integer> currentActionProgressModel = new PropertyModel<>(this, "player.actionExecution.progress.currentProgressPoints");
		InlineProgressBar currentActionProgressBar = new InlineProgressBar("currentActionProgressBar", currentActionProgressModel) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getPlayer().getActionExecution().getProgress() != null);
			}
		};
		currentActionProgressBar.setTotalAmountModel(new PropertyModel<>(this, "player.actionExecution.progress.requiredProgressPoints"));
		currentActionProgressBar.add(new ProgressBarClientProgressBehavior() {
			@Override
			protected int getRemainingSeconds() {
				Integer remainingTime = getPlayer().getActionExecution().getRemainingTime();
				return (remainingTime == null ? 0 : remainingTime);
			}
		});
		currentActionContainer.queue(currentActionProgressBar);
		currentActionContainer.queue(new AjaxLink<Void>("cancelCurrentActionLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				getPlayer().cancelCurrentAction();
				target.add(MainPage.this.get("currentActionContainer"));
			}
		});
		currentActionContainer.add(new Label("remainingTime", new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				Integer remainingTime = getPlayer().getActionExecution().getRemainingTime();
				if (remainingTime == null) {
					return "N/A";
				} else if (remainingTime < 60) {
					return remainingTime + "s";
				} else if (remainingTime < 3600) {
					return String.format("%dm %02ds", remainingTime / 60, remainingTime % 60);
				} else {
					return String.format("%dh %02dm %02ds", remainingTime / 3600, remainingTime / 60 % 60, remainingTime % 60);
				}
			}
		}));
		currentActionContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));

		WebMarkupContainer pendingActionsContainer = new WebMarkupContainer("pendingActionsContainer");
		add(pendingActionsContainer);
		pendingActionsContainer.add(new ListView<Action>("pendingActions", new PropertyModel<>(this, "player.pendingActions")) {
			@Override
			protected void populateItem(ListItem<Action> item) {
				item.add(new Label("text", item.getModelObject().toString()));
				item.add(new AjaxLink<Void>("cancelLink") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						getPlayer().cancelPendingAction(item.getIndex());
						target.add(MainPage.this.get("pendingActionsContainer"));
					}
				});
			}
		});
		pendingActionsContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));

		WebMarkupContainer inventoryContainer = new WebMarkupContainer("inventoryContainer");
		add(inventoryContainer);
		inventoryContainer.add(new ListView<ItemStack>("itemStacks", getPlayer().getInventory().getItemStacks()) {
			@Override
			protected void populateItem(ListItem<ItemStack> item) {
				item.add(new Label("size", "" + item.getModelObject().getSize()));
				item.add(new Label("itemType", "" + item.getModelObject().getItemType()));
				item.add(new Image("icon", ItemIcons.get(item.getModelObject().getItemType())));
			}
		});
		inventoryContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));

		WebMarkupContainer skillsContainer = new WebMarkupContainer("skillsContainer");
		skillsContainer.add(new ListView<Skill>("acquiredSkills", new PropertyModel<>(this, "playerSkills")) {
			@Override
			protected void populateItem(ListItem<Skill> item) {
				item.add(new Label("name", item.getModelObject().getName()));
			}
		});
		skillsContainer.add(new Label("skillCurrentlyBeingLearned", new PropertyModel<>(this, "player.skills.skillCurrentlyBeingLearned.name")) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getDefaultModelObject() != null);
			}
		});
		IModel<Integer> skillCurrentlyBeingLearnedProgressModel = new PropertyModel<>(this, "player.skills.learningPoints");
		InlineProgressBar skillCurrentlyBeingLearnedProgressBar = new InlineProgressBar("skillCurrentlyBeingLearnedProgressBar", skillCurrentlyBeingLearnedProgressModel);
		skillCurrentlyBeingLearnedProgressBar.setTotalAmountModel(new PropertyModel<>(this, "player.skills.skillCurrentlyBeingLearned.requiredLearningPoints"));
		skillCurrentlyBeingLearnedProgressBar.add(new ProgressBarClientProgressBehavior() {
			@Override
			protected int getRemainingSeconds() {
				return getPlayer().getSkills().getSkillCurrentlyBeingLearned().getRequiredSecondsForLearning() - getPlayer().getSkills().getSecondsLearned();
			}
		});
		skillsContainer.queue(skillCurrentlyBeingLearnedProgressBar);
		skillsContainer.queue(new AjaxLink<Void>("cancelSkillCurrentlyBeingLearnedLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				getPlayer().getSkills().cancelSkillCurrentlyBeingLearned();
				target.add(MainPage.this.get("skillsContainer"));
			}
		});
		skillsContainer.add(new ListView<Skill>("skillLearningQueue", new PropertyModel<>(this, "player.skills.learningQueue")) {
			@Override
			protected void populateItem(ListItem<Skill> item) {
				item.add(new Label("name", item.getModelObject().getName()));
			}
		});
		skillsContainer.add(new ListView<Skill>("skillsAvailableForLearning", new PropertyModel<>(this, "skillsAvailableForLearning")) {
			@Override
			protected void populateItem(ListItem<Skill> item) {
				Link<?> link = new Link<Void>("link") {
					@Override
					public void onClick() {
						getPlayer().getSkills().enqueueForLearning(item.getModelObject());
					}
				};
				link.add(new Label("name", item.getModelObject().getName()));
				item.add(link);
			}
		});
		add(skillsContainer);
		skillsContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));

		WebMarkupContainer spaceObjectsContainer = new WebMarkupContainer("spaceObjectsContainer");
		add(spaceObjectsContainer);
		spaceObjectsContainer.add(new ListView<SpaceObject>("spaceObjects", new PropertyModel<>(this, "game.space.spaceObjects")) {
			@Override
			protected void populateItem(ListItem<SpaceObject> spaceObjectItem) {
				spaceObjectItem.add(new Label("name", new PropertyModel<>(spaceObjectItem.getModel(), "name")));
				spaceObjectItem.add(new Label("x", new PropertyModel<>(spaceObjectItem.getModel(), "x")));
				spaceObjectItem.add(new Label("y", new PropertyModel<>(spaceObjectItem.getModel(), "y")));
				IModel<List<Action>> objectActionsModel = new LoadableDetachableModel<List<Action>>() {
					@Override
					protected List<Action> load() {
						return spaceObjectItem.getModelObject().getActionsFor(getPlayer());
					}
				};
				spaceObjectItem.add(new ListView<Action>("actions", objectActionsModel) {
					@Override
					protected void populateItem(ListItem<Action> actionItem) {
						AjaxLink<?> link = new AjaxLink<Void>("link") {
							@Override
							public void onClick(AjaxRequestTarget target) {
								Player player = getPlayer();
								player.scheduleAction(actionItem.getModelObject());
								target.add(MainPage.this.get("currentActionContainer"));
								target.add(MainPage.this.get("pendingActionsContainer"));
								target.add(MainPage.this.get("inventoryContainer"));
							}
						};
						link.add(new Label("name", actionItem.getModelObject().toString()));
						actionItem.add(link);
					}
				});
			}
		});

	}

	public final List<Skill> getPlayerSkills() {
		List<Skill> skills = new ArrayList<>(getPlayer().getSkills().getSkills());
		Collections.sort(skills, (x, y) -> x.getName().compareTo(y.getName()));
		return skills;
	}

	public final List<Skill> getSkillsAvailableForLearning() {
		List<Skill> skills = new ArrayList<>(getGameDefinition().getSkills());
		skills.removeAll(getPlayer().getSkills().getSkills());
		skills.remove(getPlayer().getSkills().getSkillCurrentlyBeingLearned());
		skills.removeAll(getPlayer().getSkills().getLearningQueue());
		Collections.sort(skills, (x, y) -> x.getName().compareTo(y.getName()));
		return skills;
	}

}
