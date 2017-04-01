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
		/*
		TODO: This query shows that our abstractions are bad -- not leaky, but rather not feature-complete. The way
		this code needs to access space object data to get the name of an inventory suggests that at database level,
		the game entities are more interdependent than we want to make them appear at repository level. That's okay
		since the repositories are an abstraction from the POV of the game and GUI code. To be feature-complete and
		non-leaky, however, we need repositories and services for all queries, so game code never sees that actual
		database.

		Solution #1: game code repositories and services use a common "database service" in the background.

		Solution #2: game code repositories are only defined as interfaces by the game code, and their implementation
		is part of the database layer.

		Both solutions require a clear definition of what a "service" is, what a "repository" is, and if needed, what
		the role of the query/database service is.

		---

		However, moving the repository code doesn't eliminate database queries since they are present in the entity
		classes (e.g. class Player), so this needs to be moved too. That fact alone heavily favors solution #1 above,
		since entities can use the database service just as well as repositories can.

		Also, repositories and entities can add additional functionality that does not map directly to database
		queries.

		Quick idea: a database service will be huge and contain most to all database queries. A custom DSL comes to
		mind to write those down in a concise way. To avoid parameter-passing overhead in the code, the "database
		connections vs. threads" problem must be solved first.

		Note that this solution does NOT demand that every query for the whole application is listed in a central class.
		It rather suggests that whenever a set of queries can be moved out of that class easily, they probably
		define a whole independent subsystem (hint: microservice!). Or put the other way, if those classes cannot be
		moved into their own microservice because of logical (data-)dependencies, they are probably not independent
		enough to justify moving their queries out of the central query service.

		---

		What's the advantage of all this?
		- unit testing

		Unit testing might as well be supported by defining, per-package, a sepcified database service interface.
		The default implementation uses QueryDSL (and might be defined in a central package or even be implemented by
		a single service class for all interfaces). So we then have Player, PlayerRepository and PlayerDatabaseService
		(or PlayerDatabase). This is a role interface, so InventoryDatabase is used for InventoryNameService. Since
		that class would be too huge for testing, InventoryNameDatabase could be separated and used by InventoryNameService
		(though in this case, the service would be very thin). For players it makes more sense to separate them, but
		a consistent code structure has its pros.

		 */
		try (PostgresConnection connection = MyWicketApplication.get().getDependency(PostgresService.class).newConnection()) {
			QSpaceObjectBaseDataRow qr = QSpaceObjectBaseDataRow.SpaceObjectBaseData;
			return connection.query().select(qr.name).from(qr).where(qr.inventoryId.eq(inventoryId)).fetchFirst();
		}
	}

}
