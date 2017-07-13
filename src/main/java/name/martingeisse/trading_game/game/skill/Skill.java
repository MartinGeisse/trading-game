package name.martingeisse.trading_game.game.skill;

import com.fasterxml.jackson.annotation.JsonValue;
import name.martingeisse.trading_game.game.definition.GameDefinition;

/**
 * These skill objects exist globally, as part of the {@link GameDefinition}. A player can earn them by learning. Once
 * learnt, a skill influences the game logic at various points through two main mechanisms:
 * <p>
 * 1. A skill may intervene at various points by acting as a listener. When things happen, all skills of the current
 * player get notified and may change the situation, for example by changing the progress points needed for an
 * action.
 * <p>
 * 2. Other parts of the game may explicitly check for specific skills and change their behavior. For example certain
 * blueprints require specific skills to be crafted.
 * <p>
 * TODO this duality isn't nice. In terms of decoupling, (1) is better, especially since it allows for equipped items
 * to do the same without much hassle. Maybe (2) just isn't a good idea and should be changed to use (1) as well, for
 * example by allowing skills to contribute actions, or by having a blueprint fire a player-event and only if
 * the relevant skill sets a flag in the event is the blueprint usable.
 * <p>
 * Skill implementations would have to inspect the event class. By turning this around, it may also be possible by a
 * situation (e.g. check if a blueprint can be used) to check for skills with a specific type (class or interface). That
 * way, there is only one game-defining class hierarchy instead of two parallel hierarchies.
 * <p>
 * BTW, standard actions could follow the same pattern by introducing a fake default skill which handles these cases.
 * <p>
 * Skill objects must implement equals() and hashCode() based on name equality (the name is used to identify the skill
 * during serialization).
 */
public interface Skill {

	@JsonValue
	public String getName();

	public int getRequiredSecondsForLearning();

}
