package name.martingeisse.trading_game.game.skill;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.querydsl.core.QueryException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.postgresql.PostgreSQLQuery;
import name.martingeisse.trading_game.common.database.DatabaseUtil;
import name.martingeisse.trading_game.game.definition.GameDefinition;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.PlayerSkillRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerSkillRow;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the skills of a player.
 */
public final class PlayerSkills {

	private static final QPlayerSkillRow qps = QPlayerSkillRow.PlayerSkill;

	private final PostgresContextService postgresContextService;
	private final GameDefinition gameDefinition;
	private final long playerId;

	public PlayerSkills(PostgresContextService postgresContextService, GameDefinition gameDefinition, long playerId) {
		this.postgresContextService = postgresContextService;
		this.gameDefinition = gameDefinition;
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

	private PostgreSQLQuery<PlayerSkillRow> selectByNameOrder() {
		return postgresContextService.select(qps).from(qps).where(qps.playerId.eq(playerId)).orderBy(qps.name.asc());
	}

	private PostgreSQLQuery<PlayerSkillRow> selectByLearningOrder() {
		return postgresContextService.select(qps).from(qps).where(qps.playerId.eq(playerId)).orderBy(qps.learningOrderIndex.asc());
	}

	/**
	 * Returns all skills the player has acquired, i.e. unlocked and then fully learnt.
	 */
	public ImmutableList<Skill> getAcquiredSkills() {
		List<Skill> result = new ArrayList<>();
		for (PlayerSkillRow row : selectByNameOrder().where(qps.learningFinished.isTrue()).fetch()) {
			result.add(gameDefinition.getSkillByName(row.getName()));
		}
		return ImmutableList.copyOf(result);
	}

	/**
	 * Returns all skills being learnt, i.e. unlocked but not yet acquired, and the accumulated learning points,
	 * ordered by skill name.
	 *
	 * This method does not distinguish between skills that have been unlocked but without learning points (i.e.
	 * unlocked and 0% learnt), and those that have been unlocked and learning points have been spent: Both are
	 * returned.
	 */
	public ImmutableList<Pair<Skill, Integer>> getLearningSkillsByNameOrder() {
		return getLearningSkills(selectByNameOrder());
	}

	/**
	 * Returns all skills being learnt, i.e. unlocked but not yet acquired, and the accumulated learning points,
	 * in learning order. See getLearningSkillsByNameOrder() for details.
	 */
	public ImmutableList<Pair<Skill, Integer>> getLearningSkillsByLearningOrder() {
		return getLearningSkills(selectByLearningOrder());
	}

	private ImmutableList<Pair<Skill, Integer>> getLearningSkills(PostgreSQLQuery<PlayerSkillRow> query) {
		List<Pair<Skill, Integer>> result = new ArrayList<>();
		for (PlayerSkillRow row : query.where(qps.learningFinished.isFalse()).fetch()) {
			result.add(Pair.of(gameDefinition.getSkillByName(row.getName()), row.getLearningPoints()));
		}
		return ImmutableList.copyOf(result);
	}

	/**
	 * Unlocks a skill. Returns true if successful. Returns false if the player has already unlocked that skill.
	 *
	 * This method does not check the reason why the skill gets unlocked. That is, by calling this method, the caller
	 * asserts that the player has done everything necessary to unlock the skill.
	 *
	 * If the player has already unlocked the specified skill, then this method does nothing and returns false.
	 */
	public boolean unlock(Skill skill) {
		try {
			postgresContextService.insert(qps).set(qps.playerId, playerId).set(qps.name, skill.getName()).execute();
			return true;
		} catch (QueryException e) {
			if (DatabaseUtil.isDuplicateKeyViolation(e)) {
				return false;
			} else {
				throw e;
			}
		}
	}

	/**
	 * Moves the specified skill to the top of the learning queue. All other skills retain their learning order.
	 *
	 * Note that this method allows only restricted control over the learning order, but it should be sufficient for
	 * now. Detailed control will probably be added later.
	 *
	 * Does nothing if the skill has already been acquired, or not yet unlocked.
	 */
	public void setFirstForLearning(Skill skill) {
		BooleanExpression predicate = qps.playerId.eq(playerId).and(qps.learningFinished.isFalse());
		postgresContextService.update(qps).set(qps.learningOrderIndex, qps.learningOrderIndex.add(1)).where(predicate).execute();
		postgresContextService.update(qps).set(qps.learningOrderIndex, 0).where(predicate, qps.name.eq(skill.getName())).execute();
	}

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick() {
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
	}
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
