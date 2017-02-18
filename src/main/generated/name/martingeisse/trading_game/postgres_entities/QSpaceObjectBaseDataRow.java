package name.martingeisse.trading_game.postgres_entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QSpaceObjectBaseDataRow is a Querydsl query type for SpaceObjectBaseDataRow
 */
@Generated("name.martingeisse.trading_game.platform.postgres.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QSpaceObjectBaseDataRow extends com.querydsl.sql.RelationalPathBase<SpaceObjectBaseDataRow> {

    private static final long serialVersionUID = 944017097;

    public static final QSpaceObjectBaseDataRow SpaceObjectBaseData = new QSpaceObjectBaseDataRow("SpaceObjectBaseData");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> inventoryId = createNumber("inventoryId", Long.class);

    public final NumberPath<Long> longField1 = createNumber("longField1", Long.class);

    public final StringPath name = createString("name");

    public final EnumPath<name.martingeisse.trading_game.game.space.SpaceObjectType> type = createEnum("type", name.martingeisse.trading_game.game.space.SpaceObjectType.class);

    public final NumberPath<Long> x = createNumber("x", Long.class);

    public final NumberPath<Long> y = createNumber("y", Long.class);

    public final com.querydsl.sql.PrimaryKey<SpaceObjectBaseDataRow> spaceObjectBaseDataPkey = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<InventoryRow> spaceObjectBaseDataInventoryIdFkey = createForeignKey(inventoryId, "id");

    public final com.querydsl.sql.ForeignKey<PlayerRow> _playerShipIdFkey = createInvForeignKey(id, "shipId");

    public QSpaceObjectBaseDataRow(String variable) {
        super(SpaceObjectBaseDataRow.class, forVariable(variable), "game", "SpaceObjectBaseData");
        addMetadata();
    }

    public QSpaceObjectBaseDataRow(String variable, String schema, String table) {
        super(SpaceObjectBaseDataRow.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSpaceObjectBaseDataRow(Path<? extends SpaceObjectBaseDataRow> path) {
        super(path.getType(), path.getMetadata(), "game", "SpaceObjectBaseData");
        addMetadata();
    }

    public QSpaceObjectBaseDataRow(PathMetadata metadata) {
        super(SpaceObjectBaseDataRow.class, metadata, "game", "SpaceObjectBaseData");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(inventoryId, ColumnMetadata.named("inventoryId").withIndex(6).ofType(Types.BIGINT).withSize(19));
        addMetadata(longField1, ColumnMetadata.named("longField1").withIndex(7).ofType(Types.BIGINT).withSize(19));
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(Types.VARCHAR).withSize(2000).notNull());
        addMetadata(type, ColumnMetadata.named("type").withIndex(2).ofType(Types.VARCHAR).withSize(2147483647).notNull());
        addMetadata(x, ColumnMetadata.named("x").withIndex(4).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(y, ColumnMetadata.named("y").withIndex(5).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

