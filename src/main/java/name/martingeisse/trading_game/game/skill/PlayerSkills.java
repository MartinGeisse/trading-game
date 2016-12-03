package name.martingeisse.trading_game.game.skill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the skills of a player.
 */
public final class PlayerSkills {

	private Set<Skill> skills = new HashSet<>();
	private Skill skillCurrentlyBeingLearned = null;
	private int learningPoints = 0;
	private List<Skill> learningQueue = new ArrayList<>();

	/**
	 * Getter method.
	 *
	 * @return the skills
	 */
	public Set<Skill> getSkills() {
		return skills;
	}

	/**
	 * Getter method.
	 *
	 * @return the skillCurrentlyBeingLearned
	 */
	public Skill getSkillCurrentlyBeingLearned() {
		return skillCurrentlyBeingLearned;
	}

	/**
	 * Getter method.
	 *
	 * @return the learningPoints
	 */
	public int getLearningPoints() {
		return learningPoints;
	}

	/**
	 * Getter method.
	 *
	 * @return the learningQueue
	 */
	public List<Skill> getLearningQueue() {
		return learningQueue;
	}

}
