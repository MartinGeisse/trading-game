package name.martingeisse.trading_game.game.market;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.dsl.BooleanExpression;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.MarketOrderRow;
import name.martingeisse.trading_game.postgres_entities.QMarketOrderRow;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Singleton
public class MarketOrderRepository {

	private static final QMarketOrderRow qmo = QMarketOrderRow.MarketOrder;

	private final PostgresContextService postgresContextService;
	private final EntityProvider entityProvider;

	@Inject
	public MarketOrderRepository(PostgresContextService postgresContextService, EntityProvider entityProvider) {
		this.postgresContextService = postgresContextService;
		this.entityProvider = entityProvider;
	}

	public List<MarketOrder> getMarketOrdersByPrincipal(Player principal) {
		return getMarketOrders(qmo.principalPlayerId.eq(principal.getId()));
	}

	public List<MarketOrder> getMarketOrdersByPrincipal(Player principal, MarketOrderType type) {
		return getMarketOrders(qmo.principalPlayerId.eq(principal.getId()).and(qmo.type.eq(type)));
	}

	public List<MarketOrder> getMarketOrdersByLocation(SpaceObject location) {
		return getMarketOrders(qmo.locationSpaceObjectBaseDataId.eq(location.getId()));
	}

	public List<MarketOrder> getMarketOrdersByLocation(SpaceObject location, MarketOrderType type) {
		return getMarketOrders(qmo.locationSpaceObjectBaseDataId.eq(location.getId()).and(qmo.type.eq(type)));
	}

	private List<MarketOrder> getMarketOrders(BooleanExpression predicate) {
		List<MarketOrder> result = new ArrayList<>();
		try (CloseableIterator<Long> iterator = postgresContextService.select(qmo.id).from(qmo).where(predicate).iterate()) {
			result.add(entityProvider.getMarketOrder(iterator.next()));
		}
		return result;
	}

}
