package name.martingeisse.trading_game.game.space;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import name.martingeisse.trading_game.game.space.SpaceObject;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;
import name.martingeisse.trading_game.platform.postgres.PostgresService;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseData;
import name.martingeisse.trading_game.postgres_entities.SpaceObjectBaseData;

import java.util.function.Consumer;

/**
 *
 */
@Singleton
public class SpaceObjectsRepository {

	private final PostgresService postgresService;

	@Inject
	public SpaceObjectsRepository(PostgresService postgresService) {
		this.postgresService = postgresService;
	}

	/**
	 * Loads a single space object from the repository.
	 *
	 * @param id the ID of the object to load
	 * @return the loaded object
	 */
	public SpaceObject load(long id) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QSpaceObjectBaseData qbd = QSpaceObjectBaseData.SpaceObjectBaseData;
			SpaceObjectBaseData baseData = connection.query().select(qbd).from(qbd).where(qbd.id.eq(id)).fetchFirst();
			SpaceObject spaceObject = baseData.getType().newSpaceObject();
			spaceObject.setId(baseData.getId());
			spaceObject.setName(baseData.getName());
			spaceObject.setX(baseData.getX());
			spaceObject.setY(baseData.getY());
			return spaceObject;
		}
	}

	public void insert(SpaceObject spaceObject) {
		insert(spaceObject.getBaseData());
	}

	public void insert(SpaceObjectBaseData baseData) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QSpaceObjectBaseData qbd = QSpaceObjectBaseData.SpaceObjectBaseData;
			SQLInsertClause insert = connection.insert(qbd);
			insert.set(qbd.type, baseData.getType());
			insert.set(qbd.name, baseData.getName());
			insert.set(qbd.x, baseData.getX());
			insert.set(qbd.y, baseData.getY());
			insert.execute();
		}
	}

	public void update(long id, Consumer<UpdateClause> fieldUpdater) {
		try (PostgresConnection connection = postgresService.newConnection()) {
			QSpaceObjectBaseData qbd = QSpaceObjectBaseData.SpaceObjectBaseData;
			SQLUpdateClause update = connection.update(qbd).where(qbd.id.eq(id));
			fieldUpdater.accept(update);
			update.execute();
		}
	}

}
