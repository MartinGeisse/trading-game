package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;

import javax.annotation.Generated;
import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QGameFeedbackRow is a Querydsl query type for GameFeedbackRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QGameFeedbackRow extends com.querydsl.sql.RelationalPathBase<GameFeedbackRow> {

	private static final long serialVersionUID = -1433832076;

	public static final QGameFeedbackRow GameFeedback = new QGameFeedbackRow("GameFeedback");

	public final SimplePath<name.martingeisse.trading_game.tools.codegen.PostgresJsonb> context = createSimple("context", name.martingeisse.trading_game.tools.codegen.PostgresJsonb.class);

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final NumberPath<Long> playerId = createNumber("playerId", Long.class);

	public final StringPath sessionId = createString("sessionId");

	public final StringPath text = createString("text");

	public final DateTimePath<org.joda.time.DateTime> timestamp = createDateTime("timestamp", org.joda.time.DateTime.class);

	public final com.querydsl.sql.PrimaryKey<GameFeedbackRow> gameFeedbackPkey = createPrimaryKey(id);

	public final com.querydsl.sql.ForeignKey<PlayerRow> gameFeedbackPlayerIdFkey = createForeignKey(playerId, "id");

	public QGameFeedbackRow(String variable) {
		super(GameFeedbackRow.class, forVariable(variable), "game", "GameFeedback");
		addMetadata();
	}

	public QGameFeedbackRow(String variable, String schema, String table) {
		super(GameFeedbackRow.class, forVariable(variable), schema, table);
		addMetadata();
	}

	public QGameFeedbackRow(Path<? extends GameFeedbackRow> path) {
		super(path.getType(), path.getMetadata(), "game", "GameFeedback");
		addMetadata();
	}

	public QGameFeedbackRow(PathMetadata metadata) {
		super(GameFeedbackRow.class, metadata, "game", "GameFeedback");
		addMetadata();
	}

	public void addMetadata() {
		addMetadata(context, ColumnMetadata.named("context").withIndex(5).ofType(Types.OTHER).withSize(2147483647).notNull());
		addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(playerId, ColumnMetadata.named("playerId").withIndex(4).ofType(Types.BIGINT).withSize(19));
		addMetadata(sessionId, ColumnMetadata.named("sessionId").withIndex(3).ofType(Types.VARCHAR).withSize(1000));
		addMetadata(text, ColumnMetadata.named("text").withIndex(6).ofType(Types.VARCHAR).withSize(2147483647).notNull());
		addMetadata(timestamp, ColumnMetadata.named("timestamp").withIndex(2).ofType(Types.TIMESTAMP).withSize(35).withDigits(6).notNull());
	}

}

