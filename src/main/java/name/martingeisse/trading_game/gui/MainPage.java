/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui;

import name.martingeisse.trading_game.game.Player;
import name.martingeisse.trading_game.game.action.Action;
import name.martingeisse.trading_game.game.item.ItemStack;
import name.martingeisse.trading_game.game.skill.Skill;
import name.martingeisse.trading_game.game.space.GeometryUtil;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.game.space.SpaceStation;
import name.martingeisse.trading_game.gui.item.ItemIcons;
import name.martingeisse.trading_game.platform.wicket.page.AbstractPage;
import name.martingeisse.wicket.helpers.InlineProgressBar;
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

	private long selectedSpaceObjectId = -1;
	private final IModel<SpaceObject> selectedSpaceObjectModel = new LoadableDetachableModel<SpaceObject>() {
		@Override
		protected SpaceObject load() {
			return getGame().getSpace().get(selectedSpaceObjectId);
		}
	};

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
		playerContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));
		add(playerContainer);

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
		currentActionContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));

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
		pendingActionsContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));

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
		inventoryContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));

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
		skillsContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));

		WebMarkupContainer spaceObjectsContainer = new WebMarkupContainer("spaceObjectsContainer");
		add(spaceObjectsContainer);
		spaceObjectsContainer.add(new ListView<SpaceObject>("spaceObjects", new PropertyModel<>(this, "game.space.spaceObjects")) {
			@Override
			protected void populateItem(ListItem<SpaceObject> spaceObjectItem) {
				AjaxLink<?> link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						selectedSpaceObjectId = spaceObjectItem.getModelObject().getId();
						target.add(MainPage.this.get("selectedSpaceObjectContainer"));
					}
				};
				link.add(new Label("name", spaceObjectItem.getModelObject().getName()));
				link.add(new Label("x", spaceObjectItem.getModelObject().getX()));
				link.add(new Label("y", spaceObjectItem.getModelObject().getY()));
				spaceObjectItem.add(link);
			}
		});

		WebMarkupContainer selectedSpaceObjectContainer = new WebMarkupContainer("selectedSpaceObjectContainer");
		selectedSpaceObjectContainer.setOutputMarkupId(true);
		selectedSpaceObjectContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));
		add(selectedSpaceObjectContainer);
		selectedSpaceObjectContainer.add(new Label("name", new PropertyModel<>(selectedSpaceObjectModel, "name")));
		selectedSpaceObjectContainer.add(new Label("type", new PropertyModel<>(selectedSpaceObjectModel, "class.simpleName")));
		selectedSpaceObjectContainer.add(new Label("x", new PropertyModel<>(selectedSpaceObjectModel, "x")));
		selectedSpaceObjectContainer.add(new Label("y", new PropertyModel<>(selectedSpaceObjectModel, "y")));
		selectedSpaceObjectContainer.add(new Label("distance", new PropertyModel<>(this, "selectedSpaceObjectDistance")));
		selectedSpaceObjectContainer.add(new ListView<Action>("actions", new PropertyModel<>(this, "selectedSpaceObjectActions")) {
			@Override
			protected void populateItem(ListItem<Action> actionItem) {
				AjaxLink<?> link = new AjaxLink<Void>("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						Player player = getPlayer();
						player.cancelCurrentAction();
						player.cancelAllPendingActions();
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
		selectedSpaceObjectContainer.add(new ListView<ItemStack>("itemStacks", new PropertyModel<>(this, "selectedSpaceObjectItems")) {
			@Override
			protected void populateItem(ListItem<ItemStack> item) {
				item.add(new Label("size", "" + item.getModelObject().getSize()));
				item.add(new Label("itemType", "" + item.getModelObject().getItemType()));
				item.add(new Image("icon", ItemIcons.get(item.getModelObject().getItemType())));
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

	public Double getSelectedSpaceObjectDistance() {
		if (selectedSpaceObjectModel.getObject() == null) {
			return null;
		} else {
			return GeometryUtil.getDistance(getPlayer().getShip(), selectedSpaceObjectModel.getObject());
		}
	}

	public List<Action> getSelectedSpaceObjectActions() {
		if (selectedSpaceObjectModel.getObject() == null) {
			return null;
		} else {
			return selectedSpaceObjectModel.getObject().getActionsFor(getPlayer());
		}
	}

	public List<ItemStack> getSelectedSpaceObjectItems() {
		if (selectedSpaceObjectModel.getObject() instanceof SpaceStation) {
			return ((SpaceStation)selectedSpaceObjectModel.getObject()).getInventory().getItemStacks();
		} else {
			return null;
		}
	}

}
