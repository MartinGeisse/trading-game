package name.martingeisse.trading_game.game.player;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

/**
 *
 */
@Singleton
public final class PlayerLoginRepository {

	private final PlayerRepository playerRepository;
	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;
	private final EntityProvider entityProvider;

	@Inject
	public PlayerLoginRepository(PlayerRepository playerRepository, PostgresContextService postgresContextService, JacksonService jacksonService, EntityProvider entityProvider) {
		this.playerRepository = playerRepository;
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
		this.entityProvider = entityProvider;
	}

	private Player instantiate(PlayerRow data) {
		return new Player(playerRepository, new PlayerDataLink(postgresContextService, entityProvider, jacksonService, data));
	}

	/**
	 * Gets player by login token.
	 *
	 * @param loginToken the login token
	 * @return the player
	 */
	public Player getPlayerByLoginToken(String loginToken) {
		QPlayerRow qp = QPlayerRow.Player;
		PlayerRow playerRow = postgresContextService.select(qp).from(qp).where(qp.loginToken.eq(loginToken)).fetchFirst();
		if (playerRow == null) {
			throw new IllegalArgumentException("player not found for that login token");
		}
		return instantiate(playerRow);
	}

	/**
	 * Gets a list of login tokens for a specific email address.
	 */
	public ImmutableList<String> getLoginTokensByEmailAddress(String emailAddress) {
		QPlayerRow qp = QPlayerRow.Player;
		return ImmutableList.copyOf(postgresContextService.select(qp.loginToken).from(qp).where(qp.emailAddress.eq(emailAddress), qp.loginToken.isNotNull()).fetch());
	}

}
