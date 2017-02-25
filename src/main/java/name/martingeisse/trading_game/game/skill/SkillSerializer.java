package name.martingeisse.trading_game.game.skill;

/**
 * Provides a serialized representation of skills as strings.
 *
 * NOTE: Various pieces of code currently depend on the serialized version of a skill being unambiguous. This is
 * the case for the current serializer.
 */
public interface SkillSerializer {

	public String serializeSkill(Skill skill);

	public Skill deserializeSkill(String serializedSkill);

}
