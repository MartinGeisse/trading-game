/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.game.Game;
import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.action.ContextFreeActionDefinition;
import name.martingeisse.trading_game.game.action.PlayerAction;
import name.martingeisse.trading_game.game.item.FixedInventory;
import name.martingeisse.trading_game.game.item.FixedItemStack;
import name.martingeisse.trading_game.game.item.ItemStack;
import name.martingeisse.trading_game.game.skill.Skill;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;

import java.util.*;

/**
 * This is the main page for controlling the game.
 */
public class MainPage extends AbstractPage {

	/**
	 * Constructor
	 */
	public MainPage() {

		add(new Label("playerName", new PropertyModel<>(this, "player.name")));
		add(new Label("playerID", new PropertyModel<>(this, "player.id")));

		add(new ListView<ContextFreeActionDefinition>("contextFreeActionDefinitions", gameDefinitionModel("contextFreeActionDefinitions")) {

			@Override
			protected void populateItem(ListItem<ContextFreeActionDefinition> item) {
				AjaxLink<?> link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						schedule(1, item.getModelObject(), target);
					}
				};
				link.add(new Label("name", item.getModelObject().getName()));
				item.add(link);
				item.add(new PromptAjaxLink<Void>("multiLink") {

					@Override
					protected String getPrompt() {
						return item.getModelObject().getName() + " how many times?";
					}

					@Override
					protected void onClick(AjaxRequestTarget target, String promptInput) {
						int times;
						try {
							times = Integer.parseInt(promptInput);
						} catch (NumberFormatException e) {
							target.appendJavaScript("invalid input");
							return;
						}
						schedule(times, item.getModelObject(), target);
					}

				});

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

			private void schedule(int repetitions, ContextFreeActionDefinition actionDefinition, AjaxRequestTarget target) {
				Player player = getPlayer();
				player.scheduleAction(repetitions, actionDefinition.getFactory().apply(player));
				target.add(MainPage.this.get("currentActionContainer"));
				target.add(MainPage.this.get("pendingActionsContainer"));
				target.add(MainPage.this.get("inventoryContainer"));
			}

		});

		WebMarkupContainer currentActionContainer = new WebMarkupContainer("currentActionContainer");
		add(currentActionContainer);
		currentActionContainer.add(new Label("currentAction", new PropertyModel<>(this, "player.actionProgress.action")) {
			@Override
			protected void onConfigure() {
				super.onConfigure();
				setVisible(getDefaultModelObject() != null);
			}
		});
		IModel<Integer> currentActionProgressModel = new PropertyModel<>(this, "player.actionProgress.progressPoints");
		InlineProgressBar currentActionProgressBar = new InlineProgressBar("currentActionProgressBar", currentActionProgressModel);
		currentActionProgressBar.setTotalAmountModel(new PropertyModel<>(this, "player.actionProgress.action.requiredProgressPoints"));
		currentActionProgressBar.add(new ProgressBarClientProgressBehavior() {
			@Override
			protected int getRemainingSeconds() {
				int remainingProgressPoints = getPlayer().getActionProgress().getAction().getRequiredProgressPoints() - currentActionProgressModel.getObject();
				return remainingProgressPoints / Game.getTicksPerSecond();
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
		currentActionContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));

		WebMarkupContainer pendingActionsContainer = new WebMarkupContainer("pendingActionsContainer");
		add(pendingActionsContainer);
		pendingActionsContainer.add(new ListView<ActionQueue.Entry>("pendingActions", new PropertyModel<>(this, "player.pendingActions")) {
			@Override
			protected void populateItem(ListItem<ActionQueue.Entry> item) {
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

		add(new BookmarkablePageLink<>("playerListLink", PlayerListPage.class));
		add(new BookmarkablePageLink<>("renamePlayerLink", RenamePlayerPage.class));

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
				int remainingLearningPoints = getPlayer().getSkills().getSkillCurrentlyBeingLearned().getRequiredLearningPoints() - getPlayer().getSkills().getLearningPoints();
				return remainingLearningPoints / Game.getTicksPerSecond();
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
