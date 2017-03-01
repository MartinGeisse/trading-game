package name.martingeisse.trading_game.game.skill;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

/**
 *
 */
@Singleton
public final class PlayerSkillsRepository {

	private final PostgresService postgresService;

	@Inject
	public PlayerSkillsRepository(PostgresService postgresService) {
		this.postgresService = postgresService;
	}

	/**
	 * Returns a PlayerSkills object representing the skills of the player with the specified id.
	 *
	 * @param playerId  the player's id
	 * @return the PlayerSkills object
	 */
	public PlayerSkills getPlayerSkills(long playerId) {
		return new PlayerSkills(postgresService, playerId);
	}

}
