package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;

import javax.annotation.Generated;
import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;




/**
 * QCachedPlayerAttributeRow is a Querydsl query type for CachedPlayerAttributeRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QCachedPlayerAttributeRow extends com.querydsl.sql.RelationalPathBase<CachedPlayerAttributeRow> {

    private static final long serialVersionUID = 129179216;

    public static final QCachedPlayerAttributeRow CachedPlayerAttribute = new QCachedPlayerAttributeRow("CachedPlayerAttribute");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<name.martingeisse.trading_game.game.player.PlayerAttributeKey> key = createEnum("key", name.martingeisse.trading_game.game.player.PlayerAttributeKey.class);

    public final NumberPath<Long> playerId = createNumber("playerId", Long.class);

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<CachedPlayerAttributeRow> cachedPlayerAttributePkey = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<PlayerRow> cachedPlayerAttributePlayerIdFkey = createForeignKey(playerId, "id");

    public QCachedPlayerAttributeRow(String variable) {
        super(CachedPlayerAttributeRow.class, forVariable(variable), "game", "CachedPlayerAttribute");
        addMetadata();
    }

    public QCachedPlayerAttributeRow(String variable, String schema, String table) {
        super(CachedPlayerAttributeRow.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QCachedPlayerAttributeRow(Path<? extends CachedPlayerAttributeRow> path) {
        super(path.getType(), path.getMetadata(), "game", "CachedPlayerAttribute");
        addMetadata();
    }

    public QCachedPlayerAttributeRow(PathMetadata metadata) {
        super(CachedPlayerAttributeRow.class, metadata, "game", "CachedPlayerAttribute");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(key, ColumnMetadata.named("key").withIndex(3).ofType(Types.VARCHAR).withSize(2147483647).notNull());
        addMetadata(playerId, ColumnMetadata.named("playerId").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(4).ofType(Types.VARCHAR).withSize(2147483647).notNull());
    }

}

