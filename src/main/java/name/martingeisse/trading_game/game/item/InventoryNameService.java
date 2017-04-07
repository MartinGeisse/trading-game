package name.martingeisse.trading_game.game.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseDataRow;

/**
 * This service provides a readable name for each inventory.
 */
@Singleton
public class InventoryNameService {

	private final PostgresContextService postgresContextService;

	@Inject
	public InventoryNameService(PostgresContextService postgresContextService) {
		this.postgresContextService = postgresContextService;
	}

	public String getNameForInventoryId(long inventoryId) {
		QSpaceObjectBaseDataRow qr = QSpaceObjectBaseDataRow.SpaceObjectBaseData;
		return postgresContextService.select(qr.name).from(qr).where(qr.inventoryId.eq(inventoryId)).fetchFirst();
	}

}
