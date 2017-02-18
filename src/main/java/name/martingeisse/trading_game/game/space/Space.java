package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.postgresql.PostgreSQLQuery;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.InventoryRow;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseDataRow;
import name.martingeisse.trading_game.postgres_entities.SpaceObjectBaseDataRow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class acts as a repository for {@link SpaceObject}.
 * <p>
 * Note that the same space object will be returned as a new Java object ({@link SpaceObject}) each time. Use the
 * object's id to check for identity in the repository.
 */
public final class Space {

	private static final QSpaceObjectBaseDataRow qbd = QSpaceObjectBaseDataRow.SpaceObjectBaseData;

	private final PostgresService postgresService;
	private final Injector injector; // might use a specialized SpaceObjectFactory instead

	@Inject
	public Space(PostgresService postgresService, Injector injector) {
		this.postgresService = postgresService;
		this.injector = injector;
	}

	private SpaceObject reconstructSpaceObject(SpaceObjectBaseDataRow baseData) {
		SpaceObject spaceObject = injector.getInstance(baseData.getType().getSpaceObjectClass());
		spaceObject.internalSetPostgresService(postgresService);
		spaceObject.internalSetId(baseData.getId());
		spaceObject.internalSetName(baseData.getName());
		spaceObject.internalSetX(baseData.getX());
		spaceObject.internalSetY(baseData.getY());
		return spaceObject;
	}

	/**
	 * Gets a single space object from the repository.
	 *
	 * @param id the ID of the object to load
	 * @return the loaded object
	 */
	public SpaceObject get(long id) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			return reconstructSpaceObject(SpaceObjectBaseDataRow.loadById(connection, id));
		}
	}

	/**
	 * Gets a list of space objects based on a query configurator.
	 *
	 * @param queryConfigurator the query configurator that defines the criteria for selection
	 * @return the selected space objects
	 */
	public List<SpaceObject> get(Consumer<PostgreSQLQuery<SpaceObjectBaseDataRow>> queryConfigurator) {
		List<SpaceObject> result = new ArrayList<>();
		try (PostgresConnection connection = postgresService.newConnection()) {
			PostgreSQLQuery<SpaceObjectBaseDataRow> query = connection.query().select(qbd).from(qbd);
			queryConfigurator.accept(query);
			try (CloseableIterator<SpaceObjectBaseDataRow> iterator = query.iterate()) {
				while (iterator.hasNext()) {
					result.add(reconstructSpaceObject(iterator.next()));
				}
			}
		}
		return result;
	}

	/**
	 * Gets all static space objects in a newly created list.
	 */
	public ImmutableList<StaticSpaceObject> getStaticSpaceObjects() {
		return ImmutableList.copyOf((List<StaticSpaceObject>) (List) get(query -> query.where(qbd.type.in(SpaceObjectType.getStaticTypes()))));
	}

	/**
	 * Gets all dynamic space objects in a newly created list.
	 */
	public ImmutableList<DynamicSpaceObject> getDynamicSpaceObjects() {
		return ImmutableList.copyOf((List<DynamicSpaceObject>) (List) get(query -> query.where(qbd.type.in(SpaceObjectType.getDynamicTypes()))));
	}

	/**
	 * Finds a space object by position and matching radius. If multiple objects match then the nearest one is returned.
	 * Returns null if no object lies within the radius.
	 */
	public SpaceObject get(long x, long y, long radius, Comparator<SpaceObject> priorityComparator) {
		SpaceObject matchingObject = null;
		long matchingSquaredDistance = Long.MAX_VALUE;
		long squaredRadius = radius * radius;
		try (PostgresConnection connection = postgresService.newConnection()) {
			BooleanExpression p1 = qbd.x.goe(x - radius);
			BooleanExpression p2 = qbd.x.loe(x + radius);
			BooleanExpression p3 = qbd.y.goe(y - radius);
			BooleanExpression p4 = qbd.y.loe(y + radius);
			try (CloseableIterator<SpaceObjectBaseDataRow> iterator = connection.query().select(qbd).from(qbd).where(p1, p2, p3, p4).iterate()) {
				while (iterator.hasNext()) {
					SpaceObjectBaseDataRow baseData = iterator.next();
					long dx = baseData.getX() - x;
					if (dx > radius || dx < -radius) {
						continue;
					}
					long dy = baseData.getY() - y;
					if (dy > radius || dy < -radius) {
						continue;
					}
					long squaredDistance = dx * dx + dy * dy;
					if (squaredDistance > squaredRadius) {
						continue;
					}
					SpaceObject spaceObject = reconstructSpaceObject(baseData);
					int priorityOrder = priorityComparator.compare(spaceObject, matchingObject);
					boolean replaceMatching;
					if (priorityOrder > 0) {
						replaceMatching = true;
					} else if (priorityOrder < 0) {
						replaceMatching = false;
					} else {
						replaceMatching = squaredDistance < matchingSquaredDistance;
					}
					if (replaceMatching) {
						matchingSquaredDistance = squaredDistance;
						matchingObject = spaceObject;
					}
				}
			}
		}
		return matchingObject;
	}

	/**
	 * Creates a new player ship and returns its ID.
	 */
	public long createPlayerShip(String name, long x, long y) {
		try (PostgresConnection connection = postgresService.newConnection()) {

			InventoryRow inventory = new InventoryRow();
			inventory.insert(connection);

			SpaceObjectBaseDataRow baseData = new SpaceObjectBaseDataRow();
			baseData.setType(SpaceObjectType.PLAYER_SHIP);
			baseData.setName(name);
			baseData.setX(x);
			baseData.setY(y);
			baseData.setInventoryId(inventory.getId());
			baseData.insert(connection);

			return baseData.getId();
		}
	}

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick(PostgresConnection connection) {
		ImmutableSet<SpaceObjectType> typesThatSupportTick = SpaceObjectType.getTypesThatSupportTick();
		if (typesThatSupportTick.isEmpty()) {
			return;
		}
		try (CloseableIterator<SpaceObjectBaseDataRow> iterator = connection.query().select(qbd).from(qbd).where(qbd.type.in(typesThatSupportTick)).iterate()) {
			while (iterator.hasNext()) {
				SpaceObjectBaseDataRow baseData = iterator.next();
				SpaceObject spaceObject = reconstructSpaceObject(baseData);
				spaceObject.tick(connection);
			}
		}
	}

}
