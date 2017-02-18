package name.martingeisse.trading_game.postgres_entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QInventorySlot is a Querydsl query type for InventorySlot
 */
@Generated("name.martingeisse.trading_game.platform.postgres.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QInventorySlot extends com.querydsl.sql.RelationalPathBase<InventorySlot> {

    private static final long serialVersionUID = 1622055531;

    public static final QInventorySlot InventorySlot = new QInventorySlot("InventorySlot");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> inventoryId = createNumber("inventoryId", Long.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final com.querydsl.sql.PrimaryKey<InventorySlot> inventorySlotPkey = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<Inventory> inventorySlotInventoryIdFkey = createForeignKey(inventoryId, "id");

    public QInventorySlot(String variable) {
        super(InventorySlot.class, forVariable(variable), "game", "InventorySlot");
        addMetadata();
    }

    public QInventorySlot(String variable, String schema, String table) {
        super(InventorySlot.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QInventorySlot(Path<? extends InventorySlot> path) {
        super(path.getType(), path.getMetadata(), "game", "InventorySlot");
        addMetadata();
    }

    public QInventorySlot(PathMetadata metadata) {
        super(InventorySlot.class, metadata, "game", "InventorySlot");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(inventoryId, ColumnMetadata.named("inventoryId").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(quantity, ColumnMetadata.named("quantity").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

