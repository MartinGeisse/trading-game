package name.martingeisse.trading_game.game.market;

import com.querydsl.core.types.Path;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.player.Player;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.QMarketOrderRow;

/**
 *
 */
public final class MarketOrder {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;
	private final EntityProvider entityProvider;
	private final long id;

	public MarketOrder(PostgresContextService postgresContextService, JacksonService jacksonService, EntityProvider entityProvider, long id) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
		this.entityProvider = entityProvider;
		this.id = id;
	}

	/**
	 * Getter method.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	private <T> T getField(Path<T> path) {
		QMarketOrderRow qmo = QMarketOrderRow.MarketOrder;
		return postgresContextService.select(path).from(qmo).where(qmo.id.eq(id)).fetchFirst();
	}

	private <T> void setField(Path<T> path, T newValue) {
		QMarketOrderRow qmo = QMarketOrderRow.MarketOrder;
		postgresContextService.update(qmo).set(path, newValue).where(qmo.id.eq(id)).execute();
	}

	public String getSerializedItemType() {
		return getField(QMarketOrderRow.MarketOrder.itemType);
	}

	public ItemType getItemType() {
		return jacksonService.deserialize(getSerializedItemType(), ItemType.class);
	}

	public Long getLocationSpaceObjectBaseDataId() {
		return getField(QMarketOrderRow.MarketOrder.locationSpaceObjectBaseDataId);
	}

	public SpaceObject getLocation() {
		return entityProvider.getSpaceObject(getLocationSpaceObjectBaseDataId());
	}

	public Long getPrincipalPlayerId() {
		return getField(QMarketOrderRow.MarketOrder.principalPlayerId);
	}

	public Player getPrincipal() {
		return entityProvider.getPlayer(getPrincipalPlayerId());
	}

	public Integer getQuantity() {
		return getField(QMarketOrderRow.MarketOrder.quantity);
	}

	public name.martingeisse.trading_game.game.market.MarketOrderType getType() {
		return getField(QMarketOrderRow.MarketOrder.type);
	}

	public Long getUnitPrice() {
		return getField(QMarketOrderRow.MarketOrder.unitPrice);
	}

}
