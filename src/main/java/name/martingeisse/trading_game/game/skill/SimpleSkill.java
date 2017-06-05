package name.martingeisse.trading_game.game.skill;

/**
 *
 */
public final class SimpleSkill implements Skill {

	private final String name;
	private final int requiredSecondsForLearning;

	public SimpleSkill(String name, int requiredSecondsForLearning) {
		if (name == null) {
			throw new IllegalArgumentException("name argument cannot be null");
		}
		if (requiredSecondsForLearning < 0) {
			throw new IllegalArgumentException("requiredSecondsForLearning argument cannot be negative");
		}
		this.name = name;
		this.requiredSecondsForLearning = requiredSecondsForLearning;
	}

	/**
	 * Getter method.
	 *
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Getter method.
	 *
	 * @return the requiredSecondsForLearning
	 */
	@Override
	public int getRequiredSecondsForLearning() {
		return requiredSecondsForLearning;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Skill) {
			Skill other = (Skill)obj;
			return name.equals(other.getName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

}
