package name.martingeisse.trading_game.game.skill;

import name.martingeisse.trading_game.platform.postgres.PostgresService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Represents the skills of a player.
 */
public final class PlayerSkills {

	private final PostgresService postgresService;
	private final long playerId;

	PlayerSkills(PostgresService postgresService, long playerId) {
		this.postgresService = postgresService;
		this.playerId = playerId;
	}

	/**
	 * Getter method.
	 *
	 * @return the playerId
	 */
	public long getPlayerId() {
		return playerId;
	}

	//	/**
//	 * Called once every second to advance game logic.
//	 */
//	public void tick() {
//		if (skillCurrentlyBeingLearned != null) {
//			secondsLearned++;
//			if (secondsLearned >= skillCurrentlyBeingLearned.getRequiredSecondsForLearning()) {
//				skills.add(skillCurrentlyBeingLearned);
//				skillCurrentlyBeingLearned = null;
//				secondsLearned = 0;
//			}
//		}
//		if (skillCurrentlyBeingLearned == null && !learningQueue.isEmpty()) {
//			skillCurrentlyBeingLearned = learningQueue.remove(0);
//			secondsLearned = 0;
//		}
//	}
//
//	public <T extends Skill> void contribute(Class<T> skillType, Consumer<? super T> consumer) {
//		for (Skill skill : skills) {
//			if (skillType.isInstance(skill)) {
//				T typedSkill = skillType.cast(skill);
//				consumer.accept(typedSkill);
//			}
//		}
//	}
//
//	public boolean enqueueForLearning(Skill skill) {
//		if (skills.contains(skill)) {
//			return false;
//		}
//		if (skillCurrentlyBeingLearned == skill) {
//			return false;
//		}
//		if (learningQueue.contains(skill)) {
//			return false;
//		}
//		learningQueue.add(skill);
//		return true;
//	}
//
//	public void cancelSkillCurrentlyBeingLearned() {
//		// TODO skill points should not be lost. When canceling, the skill points for that skill should be kept and
//		// be used when re-starting learning that skill.
//		skillCurrentlyBeingLearned = null;
//		secondsLearned = 0;
//	}
//
//	public void cancelSkillFromLearningQueue(int index) {
//		if (index >= 0 && index < learningQueue.size()) {
//			learningQueue.remove(index);
//		}
//	}

}
