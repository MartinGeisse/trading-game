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
 * QPlayerShipEquimentSlotRow is a Querydsl query type for PlayerShipEquimentSlotRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QPlayerShipEquimentSlotRow extends com.querydsl.sql.RelationalPathBase<PlayerShipEquimentSlotRow> {

    private static final long serialVersionUID = 313821298;

    public static final QPlayerShipEquimentSlotRow PlayerShipEquimentSlot = new QPlayerShipEquimentSlotRow("PlayerShipEquimentSlot");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemType = createString("itemType");

    public final StringPath slotType = createString("slotType");

    public final NumberPath<Long> spaceObjectBaseDataId = createNumber("spaceObjectBaseDataId", Long.class);

    public final com.querydsl.sql.PrimaryKey<PlayerShipEquimentSlotRow> playerShipEquimentSlotPkey = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SpaceObjectBaseDataRow> playerShipEquimentSlotSpaceObjectBaseDataIdFkey = createForeignKey(spaceObjectBaseDataId, "id");

    public QPlayerShipEquimentSlotRow(String variable) {
        super(PlayerShipEquimentSlotRow.class, forVariable(variable), "game", "PlayerShipEquimentSlot");
        addMetadata();
    }

    public QPlayerShipEquimentSlotRow(String variable, String schema, String table) {
        super(PlayerShipEquimentSlotRow.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QPlayerShipEquimentSlotRow(Path<? extends PlayerShipEquimentSlotRow> path) {
        super(path.getType(), path.getMetadata(), "game", "PlayerShipEquimentSlot");
        addMetadata();
    }

    public QPlayerShipEquimentSlotRow(PathMetadata metadata) {
        super(PlayerShipEquimentSlotRow.class, metadata, "game", "PlayerShipEquimentSlot");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(itemType, ColumnMetadata.named("itemType").withIndex(4).ofType(Types.VARCHAR).withSize(2000).notNull());
        addMetadata(slotType, ColumnMetadata.named("slotType").withIndex(3).ofType(Types.VARCHAR).withSize(2147483647).notNull());
        addMetadata(spaceObjectBaseDataId, ColumnMetadata.named("spaceObjectBaseDataId").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

