package name.martingeisse.trading_game.gui.self;

import name.martingeisse.trading_game.game.skill.PlayerSkills;
import name.martingeisse.trading_game.game.skill.Skill;
import name.martingeisse.trading_game.gui.gamepage.GuiNavigationLink;
import name.martingeisse.trading_game.platform.wicket.AbstractPanel;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Shows the player's skills.
 */
public class PlayerSkillsPanel extends AbstractPanel {

	public PlayerSkillsPanel(String id) {
		super(id);
		add(new Label("name", new PropertyModel<>(this, "player.name")));

		PlayerSkills playerSkills = getPlayer().getSkills();
		add(new ListView<Skill>("acquiredSkills", new PropertyModel<>(this, "player.skills.acquiredSkills")) {
			@Override
			protected void populateItem(ListItem<Skill> item) {
				item.add(new Label("name", new PropertyModel<>(item.getModel(), "name")));
			}
		});
		add(new ListView<Pair<Skill, Integer>>("learningSkills", new PropertyModel<>(this, "player.skills.learningSkillsByLearningOrder")) {
			@Override
			protected void populateItem(ListItem<Pair<Skill, Integer>> item) {
				item.add(new Label("name", new PropertyModel<>(item.getModel(), "left.name")));
				item.add(new Label("accumulatedLearningPoints", new PropertyModel<>(item.getModel(), "right")));
				item.add(new Label("requiredLearningPoints", new PropertyModel<>(item.getModel(), "left.requiredSecondsForLearning")));
			}
		});
	}

}
