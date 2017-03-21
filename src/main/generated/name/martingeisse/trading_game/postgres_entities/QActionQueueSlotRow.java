package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.sql.ColumnMetadata;

import javax.annotation.Generated;
import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QActionQueueSlotRow is a Querydsl query type for ActionQueueSlotRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QActionQueueSlotRow extends com.querydsl.sql.RelationalPathBase<ActionQueueSlotRow> {

	private static final long serialVersionUID = 2076014512;

	public static final QActionQueueSlotRow ActionQueueSlot = new QActionQueueSlotRow("ActionQueueSlot");

	public final SimplePath<name.martingeisse.trading_game.tools.codegen.PostgresJsonb> action = createSimple("action", name.martingeisse.trading_game.tools.codegen.PostgresJsonb.class);

	public final NumberPath<Long> actionQueueId = createNumber("actionQueueId", Long.class);

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final BooleanPath prerequisite = createBoolean("prerequisite");

	public final BooleanPath started = createBoolean("started");

	public final com.querydsl.sql.PrimaryKey<ActionQueueSlotRow> actionQueueSlotPkey = createPrimaryKey(id);

	public final com.querydsl.sql.ForeignKey<ActionQueueRow> actionQueueSlotActionQueueIdFkey = createForeignKey(actionQueueId, "id");

	public QActionQueueSlotRow(String variable) {
		super(ActionQueueSlotRow.class, forVariable(variable), "game", "ActionQueueSlot");
		addMetadata();
	}

	public QActionQueueSlotRow(String variable, String schema, String table) {
		super(ActionQueueSlotRow.class, forVariable(variable), schema, table);
		addMetadata();
	}

	public QActionQueueSlotRow(Path<? extends ActionQueueSlotRow> path) {
		super(path.getType(), path.getMetadata(), "game", "ActionQueueSlot");
		addMetadata();
	}

	public QActionQueueSlotRow(PathMetadata metadata) {
		super(ActionQueueSlotRow.class, metadata, "game", "ActionQueueSlot");
		addMetadata();
	}

	public void addMetadata() {
		addMetadata(action, ColumnMetadata.named("action").withIndex(4).ofType(Types.OTHER).withSize(2147483647).notNull());
		addMetadata(actionQueueId, ColumnMetadata.named("actionQueueId").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(prerequisite, ColumnMetadata.named("prerequisite").withIndex(3).ofType(Types.BIT).withSize(1).notNull());
		addMetadata(started, ColumnMetadata.named("started").withIndex(5).ofType(Types.BIT).withSize(1).notNull());
	}

}

