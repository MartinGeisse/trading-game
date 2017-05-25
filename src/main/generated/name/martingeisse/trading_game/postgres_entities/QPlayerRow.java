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
 * QPlayerRow is a Querydsl query type for PlayerRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QPlayerRow extends com.querydsl.sql.RelationalPathBase<PlayerRow> {

	private static final long serialVersionUID = -1042415446;

	public static final QPlayerRow Player = new QPlayerRow("Player");

	public final NumberPath<Long> actionQueueId = createNumber("actionQueueId", Long.class);

	public final StringPath emailAddress = createString("emailAddress");

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final StringPath loginToken = createString("loginToken");

	public final NumberPath<Long> money = createNumber("money", Long.class);

	public final StringPath name = createString("name");

	public final NumberPath<Long> shipId = createNumber("shipId", Long.class);

	public final com.querydsl.sql.PrimaryKey<PlayerRow> playerPkey = createPrimaryKey(id);

	public final com.querydsl.sql.ForeignKey<ActionQueueRow> playerActionQueueIdFkey = createForeignKey(actionQueueId, "id");

	public final com.querydsl.sql.ForeignKey<SpaceObjectBaseDataRow> playerShipIdFkey = createForeignKey(shipId, "id");

	public final com.querydsl.sql.ForeignKey<GameFeedbackRow> _gameFeedbackPlayerIdFkey = createInvForeignKey(id, "playerId");

	public final com.querydsl.sql.ForeignKey<InventorySlotRow> _inventorySlotPlayerIdFkey = createInvForeignKey(id, "playerId");

	public final com.querydsl.sql.ForeignKey<MarketOrderRow> _marketOrderPrincipalPlayerIdFkey = createInvForeignKey(id, "principalPlayerId");

	public final com.querydsl.sql.ForeignKey<PlayerSkillRow> _playerSkillPlayerIdFkey = createInvForeignKey(id, "playerId");

	public final com.querydsl.sql.ForeignKey<PlayerSkillLearningQueueSlotRow> _playerSkillLearningQueueSlotPlayerIdFkey = createInvForeignKey(id, "playerId");

	public final com.querydsl.sql.ForeignKey<CachedPlayerAttributeRow> _cachedPlayerAttributePlayerIdFkey = createInvForeignKey(id, "playerId");

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
		addMetadata(actionQueueId, ColumnMetadata.named("actionQueueId").withIndex(6).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(emailAddress, ColumnMetadata.named("emailAddress").withIndex(4).ofType(Types.VARCHAR).withSize(500));
		addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(loginToken, ColumnMetadata.named("loginToken").withIndex(3).ofType(Types.VARCHAR).withSize(500));
		addMetadata(money, ColumnMetadata.named("money").withIndex(7).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(500).notNull());
		addMetadata(shipId, ColumnMetadata.named("shipId").withIndex(5).ofType(Types.BIGINT).withSize(19).notNull());
	}

}

