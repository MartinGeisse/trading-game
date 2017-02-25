package name.martingeisse.trading_game.game.space;

import com.querydsl.sql.RelationalPathBase;
import name.martingeisse.trading_game.game.event.DatabaseRowEvent;
import name.martingeisse.trading_game.postgres_entities.QSpaceObjectBaseDataRow;

/**
 *
 */
public class SpaceObjectPositionChangedEvent extends DatabaseRowEvent {

	public SpaceObjectPositionChangedEvent(long spaceObjectId) {
		super(QSpaceObjectBaseDataRow.SpaceObjectBaseData, spaceObjectId);
	}

}
