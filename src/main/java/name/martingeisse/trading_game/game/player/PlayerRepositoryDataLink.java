package name.martingeisse.trading_game.game.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.dsl.BooleanExpression;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

/**
 * TODO use this class or delete it
 */
@Singleton
public final class PlayerRepositoryDataLink {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;
	private final EntityProvider entityProvider;

	@Inject
	public PlayerRepositoryDataLink(PostgresContextService postgresContextService, JacksonService jacksonService, EntityProvider entityProvider) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
		this.entityProvider = entityProvider;
	}

	public PlayerDataLink createPlayerDataLink(PlayerRow data) {
		return new PlayerDataLink(postgresContextService, entityProvider, jacksonService, data);
	}

	// TODO won't work unit postgres contexts work. With the code below, the connection gets closed before any rows have
	// been fetched.
	public CloseableIterator<PlayerRow> getAllPlayerRows() {
		QPlayerRow qp = QPlayerRow.Player;
		return postgresContextService.select(qp).from(qp).iterate();
	}

	public PlayerRow getPlayerRow(BooleanExpression predicate) {
		QPlayerRow qp = QPlayerRow.Player;
		return postgresContextService.select(qp).from(qp).where(predicate).fetchFirst();
	}

}
