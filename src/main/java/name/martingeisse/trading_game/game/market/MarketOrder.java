package name.martingeisse.trading_game.game.market;

import com.querydsl.core.types.Path;
import name.martingeisse.trading_game.game.item.ItemType;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.QMarketOrderRow;

/**
 * TODO:
 * - must the money be paid up-front?
 * - must the items be provided up-front?
 * - what is the actual unit price -- the buyer or seller price?
 */
public final class MarketOrder {

	private final PostgresContextService postgresContextService;
	private final JacksonService jacksonService;
	private final long id;

	public MarketOrder(PostgresContextService postgresContextService, JacksonService jacksonService, long id) {
		this.postgresContextService = postgresContextService;
		this.jacksonService = jacksonService;
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

	public Long getPrincipalPlayerId() {
		return getField(QMarketOrderRow.MarketOrder.principalPlayerId);
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
