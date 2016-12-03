package name.martingeisse.trading_game.game.skill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

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

	public void tick() {
		if (skillCurrentlyBeingLearned != null) {
			learningPoints++;
			if (learningPoints >= skillCurrentlyBeingLearned.getRequiredLearningPoints()) {
				skills.add(skillCurrentlyBeingLearned);
				skillCurrentlyBeingLearned = null;
				learningPoints = 0;
			}
		}
		if (skillCurrentlyBeingLearned == null && !learningQueue.isEmpty()) {
			skillCurrentlyBeingLearned = learningQueue.remove(0);
			learningPoints = 0;
		}
	}

	public <T extends Skill> void contribute(Class<T> skillType, Consumer<? super T> consumer) {
		for (Skill skill : skills) {
			if (skillType.isInstance(skill)) {
				T typedSkill = skillType.cast(skill);
				consumer.accept(typedSkill);
			}
		}
	}

	public boolean enqueueForLearning(Skill skill) {
		if (skills.contains(skill)) {
			return false;
		}
		if (skillCurrentlyBeingLearned == skill) {
			return false;
		}
		if (learningQueue.contains(skill)) {
			return false;
		}
		learningQueue.add(skill);
		return true;
	}

	public void cancelSkillCurrentlyBeingLearned() {
		// TODO skill points should not be lost. When canceling, the skill points for that skill should be kept and
		// be used when re-starting learning that skill.
		skillCurrentlyBeingLearned = null;
		learningPoints = 0;
	}

	public void cancelSkillFromLearningQueue(int index) {
		if (index >= 0 && index < learningQueue.size()) {
			learningQueue.remove(index);
		}
	}

}
