package name.martingeisse.trading_game.postgres_entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QInventory is a Querydsl query type for Inventory
 */
@Generated("name.martingeisse.trading_game.platform.postgres.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QInventory extends com.querydsl.sql.RelationalPathBase<Inventory> {

    private static final long serialVersionUID = -1190958387;

    public static final QInventory Inventory = new QInventory("Inventory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.querydsl.sql.PrimaryKey<Inventory> inventoryPkey = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SpaceObjectBaseData> _spaceObjectBaseDataInventoryIdFkey = createInvForeignKey(id, "inventoryId");

    public final com.querydsl.sql.ForeignKey<InventorySlot> _inventorySlotInventoryIdFkey = createInvForeignKey(id, "inventoryId");

    public QInventory(String variable) {
        super(Inventory.class, forVariable(variable), "game", "Inventory");
        addMetadata();
    }

    public QInventory(String variable, String schema, String table) {
        super(Inventory.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QInventory(Path<? extends Inventory> path) {
        super(path.getType(), path.getMetadata(), "game", "Inventory");
        addMetadata();
    }

    public QInventory(PathMetadata metadata) {
        super(Inventory.class, metadata, "game", "Inventory");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

