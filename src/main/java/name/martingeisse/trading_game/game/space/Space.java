package name.martingeisse.trading_game.game.space;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.postgresql.PostgreSQLQuery;
import name.martingeisse.trading_game.common.database.GeometricExpressions;
import name.martingeisse.trading_game.platform.postgres.PostgresContextService;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseDataRow;
import name.martingeisse.trading_game.postgres_entities.SpaceObjectBaseDataRow;
import org.postgresql.geometric.PGpoint;

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
@Singleton
public final class Space {

	private static final QSpaceObjectBaseDataRow qbd = QSpaceObjectBaseDataRow.SpaceObjectBaseData;

	private final PostgresContextService postgresContextService;
	private final SpaceObjectReconstitutor spaceObjectReconstitutor;

	@Inject
	public Space(PostgresContextService postgresContextService, SpaceObjectReconstitutor spaceObjectReconstitutor) {
		this.postgresContextService = postgresContextService;
		this.spaceObjectReconstitutor = spaceObjectReconstitutor;
	}

	private SpaceObject reconstructSpaceObject(SpaceObjectBaseDataRow baseData) {
		return baseData.getType().newInstance(spaceObjectReconstitutor, baseData);
	}

	/**
	 * Gets a single space object from the repository.
	 *
	 * @param id the ID of the object to load
	 * @return the loaded object
	 */
	public SpaceObject get(long id) {
		SpaceObjectBaseDataRow row = SpaceObjectBaseDataRow.loadById(postgresContextService.getConnection(), id);
		if (row == null) {
			throw new IllegalArgumentException("invalid space object ID: " + id);
		}
		return reconstructSpaceObject(row);
	}

	/**
	 * Gets a list of space objects based on a query configurator.
	 *
	 * @param queryConfigurator the query configurator that defines the criteria for selection
	 * @return the selected space objects
	 */
	public List<SpaceObject> get(Consumer<PostgreSQLQuery<SpaceObjectBaseDataRow>> queryConfigurator) {
		List<SpaceObject> result = new ArrayList<>();
		PostgreSQLQuery<SpaceObjectBaseDataRow> query = postgresContextService.select(qbd).from(qbd);
		queryConfigurator.accept(query);
		// this is faster than query.iterate()
		for (SpaceObjectBaseDataRow row : query.fetch()) {
			result.add(reconstructSpaceObject(row));
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
	 * Gets a subset of static space objects in a newly created list. This method selects all objects that overlap a
	 * selection rectangle.
	 * <p>
	 * Note that for now, all objects are represented by a maximal bounding circle. This should use the actual object
	 * size instead.
	 */
	public ImmutableList<StaticSpaceObject> getStaticSpaceObjects(long minX, long minY, long maxX, long maxY) {

		// adjust for object size by using a maximal bounding circle, so we can search for object centers
		long adjustedMinX = minX - StaticSpaceObject.BOUNDING_RADIUS;
		long adjustedMinY = minY - StaticSpaceObject.BOUNDING_RADIUS;
		long adjustedMaxX = maxX + StaticSpaceObject.BOUNDING_RADIUS;
		long adjustedMaxY = maxY + StaticSpaceObject.BOUNDING_RADIUS;

		return ImmutableList.copyOf((List<StaticSpaceObject>) (List) get(query -> {
			query.where(qbd.type.in(SpaceObjectType.getStaticTypes()));
			PGpoint minPoint = new PGpoint(adjustedMinX, adjustedMinY);
			PGpoint maxPoint = new PGpoint(adjustedMaxX, adjustedMaxY);
			query.where(GeometricExpressions.pointInsideRectangle(qbd.position, Expressions.constant(minPoint), Expressions.constant(maxPoint)));
		}));

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
	 * <p>
	 * Note that this method ignores the radius of space objects for now and treats them all as points.
	 * This should be fixed in the future, so the selection radius argument can be an object-independent
	 * user interface based radius. (Currently, that radius is a workaround for the point-based search).
	 */
	public SpaceObject get(long x, long y, long radius, Comparator<SpaceObject> priorityComparator) {
		SpaceObject matchingObject = null;
		long matchingSquaredDistance = Long.MAX_VALUE;
		long squaredRadius = radius * radius;
		PostgreSQLQuery<SpaceObjectBaseDataRow> query = postgresContextService.select(qbd).from(qbd);
		query.where(GeometricExpressions.pointInsideBoundingBox(qbd.position, x, y, radius));
		try (CloseableIterator<SpaceObjectBaseDataRow> iterator = query.iterate()) {
			while (iterator.hasNext()) {
				SpaceObjectBaseDataRow baseData = iterator.next();
				long dx = (long) baseData.getPosition().x - x;
				if (dx > radius || dx < -radius) {
					continue;
				}
				long dy = (long) baseData.getPosition().y - y;
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
		return matchingObject;
	}

	/**
	 * Called once every second to advance game logic.
	 */
	public void tick() {
		ImmutableSet<SpaceObjectType> typesThatSupportTick = SpaceObjectType.getTypesThatSupportTick();
		if (typesThatSupportTick.isEmpty()) {
			return;
		}
		try (CloseableIterator<SpaceObjectBaseDataRow> iterator = postgresContextService.query().select(qbd).from(qbd).where(qbd.type.in(typesThatSupportTick)).iterate()) {
			while (iterator.hasNext()) {
				SpaceObjectBaseDataRow baseData = iterator.next();
				SpaceObject spaceObject = reconstructSpaceObject(baseData);
				spaceObject.tick();
			}
		}
	}

}
