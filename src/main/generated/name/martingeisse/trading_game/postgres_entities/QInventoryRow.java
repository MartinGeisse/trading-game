package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.ColumnMetadata;

import javax.annotation.Generated;
import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;




/**
 * QInventoryRow is a Querydsl query type for InventoryRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QInventoryRow extends com.querydsl.sql.RelationalPathBase<InventoryRow> {

    private static final long serialVersionUID = 883607501;

    public static final QInventoryRow Inventory = new QInventoryRow("Inventory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.querydsl.sql.PrimaryKey<InventoryRow> inventoryPkey = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SpaceObjectBaseDataRow> _spaceObjectBaseDataInventoryIdFkey = createInvForeignKey(id, "inventoryId");

    public final com.querydsl.sql.ForeignKey<InventorySlotRow> _inventorySlotInventoryIdFkey = createInvForeignKey(id, "inventoryId");

    public QInventoryRow(String variable) {
        super(InventoryRow.class, forVariable(variable), "game", "Inventory");
        addMetadata();
    }

    public QInventoryRow(String variable, String schema, String table) {
        super(InventoryRow.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QInventoryRow(Path<? extends InventoryRow> path) {
        super(path.getType(), path.getMetadata(), "game", "Inventory");
        addMetadata();
    }

    public QInventoryRow(PathMetadata metadata) {
        super(InventoryRow.class, metadata, "game", "Inventory");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

