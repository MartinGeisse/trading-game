package name.martingeisse.trading_game.postgres_entities;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;

import javax.annotation.Generated;
import java.sql.Types;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QPlayerSkillRow is a Querydsl query type for PlayerSkillRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QPlayerSkillRow extends com.querydsl.sql.RelationalPathBase<PlayerSkillRow> {

	private static final long serialVersionUID = 1568834809;

	public static final QPlayerSkillRow PlayerSkill = new QPlayerSkillRow("PlayerSkill");

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final BooleanPath learningFinished = createBoolean("learningFinished");

	public final NumberPath<Integer> learningOrderIndex = createNumber("learningOrderIndex", Integer.class);

	public final NumberPath<Integer> learningPoints = createNumber("learningPoints", Integer.class);

	public final StringPath name = createString("name");

	public final NumberPath<Long> playerId = createNumber("playerId", Long.class);

	public final com.querydsl.sql.PrimaryKey<PlayerSkillRow> playerSkillPkey = createPrimaryKey(id);

	public final com.querydsl.sql.ForeignKey<PlayerRow> playerSkillPlayerIdFkey = createForeignKey(playerId, "id");

	public QPlayerSkillRow(String variable) {
		super(PlayerSkillRow.class, forVariable(variable), "game", "PlayerSkill");
		addMetadata();
	}

	public QPlayerSkillRow(String variable, String schema, String table) {
		super(PlayerSkillRow.class, forVariable(variable), schema, table);
		addMetadata();
	}

	public QPlayerSkillRow(Path<? extends PlayerSkillRow> path) {
		super(path.getType(), path.getMetadata(), "game", "PlayerSkill");
		addMetadata();
	}

	public QPlayerSkillRow(PathMetadata metadata) {
		super(PlayerSkillRow.class, metadata, "game", "PlayerSkill");
		addMetadata();
	}

	public void addMetadata() {
		addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(learningFinished, ColumnMetadata.named("learningFinished").withIndex(6).ofType(Types.BIT).withSize(1).notNull());
		addMetadata(learningOrderIndex, ColumnMetadata.named("learningOrderIndex").withIndex(5).ofType(Types.INTEGER).withSize(10));
		addMetadata(learningPoints, ColumnMetadata.named("learningPoints").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
		addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(Types.VARCHAR).withSize(2000).notNull());
		addMetadata(playerId, ColumnMetadata.named("playerId").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
	}

}

