package name.martingeisse.trading_game.postgres_entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QPlayerRow is a Querydsl query type for PlayerRow
 */
@Generated("name.martingeisse.trading_game.platform.postgres.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QPlayerRow extends com.querydsl.sql.RelationalPathBase<PlayerRow> {

    private static final long serialVersionUID = -1042415446;

    public static final QPlayerRow Player = new QPlayerRow("Player");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> shipId = createNumber("shipId", Long.class);

    public final com.querydsl.sql.PrimaryKey<PlayerRow> playerPkey = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SpaceObjectBaseDataRow> playerShipIdFkey = createForeignKey(shipId, "id");

    public QPlayerRow(String variable) {
        super(PlayerRow.class, forVariable(variable), "game", "Player");
        addMetadata();
    }

    public QPlayerRow(String variable, String schema, String table) {
        super(PlayerRow.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QPlayerRow(Path<? extends PlayerRow> path) {
        super(path.getType(), path.getMetadata(), "game", "Player");
        addMetadata();
    }

    public QPlayerRow(PathMetadata metadata) {
        super(PlayerRow.class, metadata, "game", "Player");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(2000).notNull());
        addMetadata(shipId, ColumnMetadata.named("shipId").withIndex(3).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

