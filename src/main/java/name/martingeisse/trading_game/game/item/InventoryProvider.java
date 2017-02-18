package name.martingeisse.trading_game.game.item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import name.martingeisse.trading_game.platform.postgres.PostgresService;

/**
 * Helper class to handle mixed injection / id parameters in class {@link Inventory}.
 */
@Singleton
public class InventoryProvider {

	private final PostgresService postgresService;
	private final ItemTypeSerializer itemTypeSerializer;

	@Inject
	public InventoryProvider(PostgresService postgresService, ItemTypeSerializer itemTypeSerializer) {
		this.postgresService = postgresService;
		this.itemTypeSerializer = itemTypeSerializer;
	}

	/**
	 * Creates a new instance based by the specified database ID. This constructor does not ensure that the ID
	 * actually exists in the database.
	 *
	 * @param id the inventory ID
	 */
	public Inventory getInventory(long id) {
		return new Inventory(postgresService, itemTypeSerializer, id);
	}

}
