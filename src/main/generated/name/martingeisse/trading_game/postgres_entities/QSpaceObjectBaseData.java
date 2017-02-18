package name.martingeisse.trading_game.postgres_entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import name.martingeisse.trading_game.game.space.SpaceObjectType;

import java.sql.Types;




/**
 * QSpaceObjectBaseData is a Querydsl query type for SpaceObjectBaseData
 */
@Generated("name.martingeisse.trading_game.platform.postgres.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QSpaceObjectBaseData extends com.querydsl.sql.RelationalPathBase<SpaceObjectBaseData> {

    private static final long serialVersionUID = 703292753;

    public static final QSpaceObjectBaseData SpaceObjectBaseData = new QSpaceObjectBaseData("SpaceObjectBaseData");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final EnumPath<SpaceObjectType> type = createEnum("type", SpaceObjectType.class);

    public final NumberPath<Long> x = createNumber("x", Long.class);

    public final NumberPath<Long> y = createNumber("y", Long.class);

    public final com.querydsl.sql.PrimaryKey<SpaceObjectBaseData> spaceObjectBaseDataPkey = createPrimaryKey(id);

    public QSpaceObjectBaseData(String variable) {
        super(SpaceObjectBaseData.class, forVariable(variable), "game", "SpaceObjectBaseData");
        addMetadata();
    }

    public QSpaceObjectBaseData(String variable, String schema, String table) {
        super(SpaceObjectBaseData.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSpaceObjectBaseData(Path<? extends SpaceObjectBaseData> path) {
        super(path.getType(), path.getMetadata(), "game", "SpaceObjectBaseData");
        addMetadata();
    }

    public QSpaceObjectBaseData(PathMetadata metadata) {
        super(SpaceObjectBaseData.class, metadata, "game", "SpaceObjectBaseData");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(Types.VARCHAR).withSize(2000).notNull());
        addMetadata(type, ColumnMetadata.named("type").withIndex(2).ofType(Types.VARCHAR).withSize(2147483647).notNull());
        addMetadata(x, ColumnMetadata.named("x").withIndex(4).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(y, ColumnMetadata.named("y").withIndex(5).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

