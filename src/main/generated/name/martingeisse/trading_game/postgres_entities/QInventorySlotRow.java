package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;

import javax.annotation.Generated;
import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

/**
 * QInventorySlotRow is a Querydsl query type for InventorySlotRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QInventorySlotRow extends com.querydsl.sql.RelationalPathBase<InventorySlotRow> {

	public static final QInventorySlotRow InventorySlot = new QInventorySlotRow("InventorySlot");
	private static final long serialVersionUID = -20640913;
	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final NumberPath<Long> inventoryId = createNumber("inventoryId", Long.class);

	public final StringPath itemType = createString("itemType");

	public final NumberPath<Long> playerId = createNumber("playerId", Long.class);

	public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

	public final com.querydsl.sql.PrimaryKey<InventorySlotRow> inventorySlotPkey = createPrimaryKey(id);

	public final com.querydsl.sql.ForeignKey<PlayerRow> inventorySlotPlayerIdFkey = createForeignKey(playerId, "id");

	public final com.querydsl.sql.ForeignKey<InventoryRow> inventorySlotInventoryIdFkey = createForeignKey(inventoryId, "id");

	public QInventorySlotRow(String variable) {
		super(InventorySlotRow.class, forVariable(variable), "game", "InventorySlot");
		addMetadata();
	}

	public QInventorySlotRow(String variable, String schema, String table) {
		super(InventorySlotRow.class, forVariable(variable), schema, table);
		addMetadata();
	}

	public QInventorySlotRow(Path<? extends InventorySlotRow> path) {
		super(path.getType(), path.getMetadata(), "game", "InventorySlot");
		addMetadata();
	}

	public QInventorySlotRow(PathMetadata metadata) {
		super(InventorySlotRow.class, metadata, "game", "InventorySlot");
		addMetadata();
	}

	public void addMetadata() {
		addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(inventoryId, ColumnMetadata.named("inventoryId").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(itemType, ColumnMetadata.named("itemType").withIndex(4).ofType(Types.VARCHAR).withSize(2000).notNull());
		addMetadata(playerId, ColumnMetadata.named("playerId").withIndex(3).ofType(Types.BIGINT).withSize(19));
		addMetadata(quantity, ColumnMetadata.named("quantity").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
	}

}

