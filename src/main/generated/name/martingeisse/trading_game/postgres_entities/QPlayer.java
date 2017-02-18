package name.martingeisse.trading_game.postgres_entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QPlayer is a Querydsl query type for Player
 */
@Generated("name.martingeisse.trading_game.platform.postgres.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QPlayer extends com.querydsl.sql.RelationalPathBase<Player> {

    private static final long serialVersionUID = -1111152880;

    public static final QPlayer Player = new QPlayer("Player");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> shipId = createNumber("shipId", Long.class);

    public final com.querydsl.sql.PrimaryKey<Player> playerPkey = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<SpaceObjectBaseData> playerShipIdFkey = createForeignKey(shipId, "id");

    public QPlayer(String variable) {
        super(Player.class, forVariable(variable), "game", "Player");
        addMetadata();
    }

    public QPlayer(String variable, String schema, String table) {
        super(Player.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QPlayer(Path<? extends Player> path) {
        super(path.getType(), path.getMetadata(), "game", "Player");
        addMetadata();
    }

    public QPlayer(PathMetadata metadata) {
        super(Player.class, metadata, "game", "Player");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(2000).notNull());
        addMetadata(shipId, ColumnMetadata.named("shipId").withIndex(3).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

