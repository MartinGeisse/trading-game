package name.martingeisse.trading_game.game.player;

import com.querydsl.core.types.Path;
import name.martingeisse.trading_game.game.EntityProvider;
import name.martingeisse.trading_game.game.action.ActionQueue;
import name.martingeisse.trading_game.game.equipment.PlayerShipEquipment;
import name.martingeisse.trading_game.game.jackson.JacksonService;
import name.martingeisse.trading_game.game.space.PlayerShip;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.PlayerRow;
import name.martingeisse.trading_game.postgres_entities.QCachedPlayerAttributeRow;
import name.martingeisse.trading_game.postgres_entities.QPlayerRow;

/**
 *
 */
public final class PlayerDataLink {

	private final PostgresContextService postgresContextService;
	private final EntityProvider entityProvider;
	private final JacksonService jacksonService;
	private final long id;
	private final long shipId;
	private final long actionQueueId;

	public PlayerDataLink(PostgresContextService postgresContextService, EntityProvider entityProvider, JacksonService jacksonService, PlayerRow playerRow) {
		this.postgresContextService = postgresContextService;
		this.entityProvider = entityProvider;
		this.jacksonService = jacksonService;
		this.id = playerRow.getId();
		this.shipId = playerRow.getShipId();
		this.actionQueueId = playerRow.getActionQueueId();
	}

	public long getId() {
		return id;
	}

	public <T> T getField(Path<T> path) {
		QPlayerRow qp = QPlayerRow.Player;
		return postgresContextService.select(path).from(qp).where(qp.id.eq(id)).fetchFirst();
	}

	public <T> void setField(Path<T> path, T newValue) {
		QPlayerRow qp = QPlayerRow.Player;
		postgresContextService.update(qp).set(path, newValue).where(qp.id.eq(id)).execute();
	}

	public PlayerShip getShip() {
		return (PlayerShip) entityProvider.getSpaceObject(shipId);
	}

	public ActionQueue getActionQueue() {
		return entityProvider.getActionQueue(actionQueueId);
	}

	public PlayerShipEquipment getEquipment() {
		return entityProvider.getPlayerShipEquipment(shipId);
	}

	public void clearCachedAttributes() {
		QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
		postgresContextService.delete(qa).where(qa.playerId.eq(id)).execute();
	}

	public void insertCachedAttribute(PlayerAttributeKey key, Object value) {
		String serializedValue = jacksonService.serialize(value);
		QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
		postgresContextService.insert(qa).set(qa.playerId, id).set(qa.key, key).set(qa.value, serializedValue).execute();
	}

	public Object getCachedAttribute(PlayerAttributeKey key) {
		QCachedPlayerAttributeRow qa = QCachedPlayerAttributeRow.CachedPlayerAttribute;
		String serializedValue = postgresContextService.select(qa.value).from(qa).where(qa.playerId.eq(id), qa.key.eq(key)).fetchFirst();
		if (serializedValue == null) {
			throw new IllegalStateException("missing player attribute " + key + " for player ID " + id);
		}
		return jacksonService.deserialize(serializedValue, Object.class);
	}

}
