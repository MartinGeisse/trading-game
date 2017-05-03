package name.martingeisse.trading_game.game.player;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.dsl.BooleanExpression;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.space.Space;
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
	private final Space space;
	private final EntityProvider entityProvider;

	@Inject
	public PlayerRepositoryDataLink(PostgresContextService postgresContextService, JacksonService jacksonService, Space space, EntityProvider entityProvider) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
		this.space = space;
		this.entityProvider = entityProvider;
	}

	public PlayerDataLink createPlayerDataLink(PlayerRow data) {
		return new PlayerDataLink(postgresContextService, entityProvider, jacksonService, space, data);
	}

	public CloseableIterator<PlayerRow> getAllPlayerRows() {
		QPlayerRow qp = QPlayerRow.Player;
		return postgresContextService.select(qp).from(qp).iterate();
	}

	public PlayerRow getPlayerRow(BooleanExpression predicate) {
		QPlayerRow qp = QPlayerRow.Player;
		return postgresContextService.select(qp).from(qp).where(predicate).fetchFirst();
	}

}
