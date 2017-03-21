package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.ColumnMetadata;

import javax.annotation.Generated;
import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QActionQueueRow is a Querydsl query type for ActionQueueRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QActionQueueRow extends com.querydsl.sql.RelationalPathBase<ActionQueueRow> {

	private static final long serialVersionUID = 2004712590;

	public static final QActionQueueRow ActionQueue = new QActionQueueRow("ActionQueue");

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final com.querydsl.sql.PrimaryKey<ActionQueueRow> actionQueuePkey = createPrimaryKey(id);

	public final com.querydsl.sql.ForeignKey<PlayerRow> _playerActionQueueIdFkey = createInvForeignKey(id, "actionQueueId");

	public final com.querydsl.sql.ForeignKey<ActionQueueSlotRow> _actionQueueSlotActionQueueIdFkey = createInvForeignKey(id, "actionQueueId");

	public QActionQueueRow(String variable) {
		super(ActionQueueRow.class, forVariable(variable), "game", "ActionQueue");
		addMetadata();
	}

	public QActionQueueRow(String variable, String schema, String table) {
		super(ActionQueueRow.class, forVariable(variable), schema, table);
		addMetadata();
	}

	public QActionQueueRow(Path<? extends ActionQueueRow> path) {
		super(path.getType(), path.getMetadata(), "game", "ActionQueue");
		addMetadata();
	}

	public QActionQueueRow(PathMetadata metadata) {
		super(ActionQueueRow.class, metadata, "game", "ActionQueue");
		addMetadata();
	}

	public void addMetadata() {
		addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
	}

}

