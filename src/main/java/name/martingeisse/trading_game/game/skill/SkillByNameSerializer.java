package name.martingeisse.trading_game.game.skill;

import com.google.common.collect.ImmutableList;

/**
 * Keeps a list of skills and serializes them by name.
 */
public final class SkillByNameSerializer implements SkillSerializer {

	private final ImmutableList<Skill> skills;

	public SkillByNameSerializer(ImmutableList<Skill> skills) {
		this.skills = skills;
	}

	@Override
	public String serializeSkill(Skill skill) {
		return skill.getName();
	}

	@Override
	public Skill deserializeSkill(String serializedSkill) {
		for (Skill skill : skills) {
			if (skill.getName().equals(serializedSkill)) {
				return skill;
			}
		}
		throw new RuntimeException("cannot deserialize skill: " + serializedSkill);
	}

}
