package name.martingeisse.trading_game.game.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.platform.wicket.MyWicketApplication;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseDataRow;

/**
 * This service provides a readable name for each inventory.
 */
@Singleton
public class InventoryNameService {

	private final PostgresService postgresService;

	@Inject
	public InventoryNameService(PostgresService postgresService) {
		this.postgresService = postgresService;
	}

	public String getNameForInventoryId(long inventoryId) {
		try (PostgresConnection connection = MyWicketApplication.get().getDependency(PostgresService.class).newConnection()) {
			QSpaceObjectBaseDataRow qr = QSpaceObjectBaseDataRow.SpaceObjectBaseData;
			return connection.query().select(qr.name).from(qr).where(qr.inventoryId.eq(inventoryId)).fetchFirst();
		}
	}

}
