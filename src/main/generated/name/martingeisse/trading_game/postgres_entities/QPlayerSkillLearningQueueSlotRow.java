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
 * QPlayerSkillLearningQueueSlotRow is a Querydsl query type for PlayerSkillLearningQueueSlotRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QPlayerSkillLearningQueueSlotRow extends com.querydsl.sql.RelationalPathBase<PlayerSkillLearningQueueSlotRow> {

	private static final long serialVersionUID = 1588103594;

	public static final QPlayerSkillLearningQueueSlotRow PlayerSkillLearningQueueSlot = new QPlayerSkillLearningQueueSlotRow("PlayerSkillLearningQueueSlot");

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final NumberPath<Integer> learningOrderIndex = createNumber("learningOrderIndex", Integer.class);

	public final NumberPath<Integer> learningPoints = createNumber("learningPoints", Integer.class);

	public final NumberPath<Long> playerId = createNumber("playerId", Long.class);

	public final StringPath skillType = createString("skillType");

	public final com.querydsl.sql.PrimaryKey<PlayerSkillLearningQueueSlotRow> playerSkillLearningQueueSlotPkey = createPrimaryKey(id);

	public final com.querydsl.sql.ForeignKey<PlayerRow> playerSkillLearningQueueSlotPlayerIdFkey = createForeignKey(playerId, "id");

	public QPlayerSkillLearningQueueSlotRow(String variable) {
		super(PlayerSkillLearningQueueSlotRow.class, forVariable(variable), "game", "PlayerSkillLearningQueueSlot");
		addMetadata();
	}

	public QPlayerSkillLearningQueueSlotRow(String variable, String schema, String table) {
		super(PlayerSkillLearningQueueSlotRow.class, forVariable(variable), schema, table);
		addMetadata();
	}

	public QPlayerSkillLearningQueueSlotRow(Path<? extends PlayerSkillLearningQueueSlotRow> path) {
		super(path.getType(), path.getMetadata(), "game", "PlayerSkillLearningQueueSlot");
		addMetadata();
	}

	public QPlayerSkillLearningQueueSlotRow(PathMetadata metadata) {
		super(PlayerSkillLearningQueueSlotRow.class, metadata, "game", "PlayerSkillLearningQueueSlot");
		addMetadata();
	}

	public void addMetadata() {
		addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(learningOrderIndex, ColumnMetadata.named("learningOrderIndex").withIndex(5).ofType(Types.INTEGER).withSize(10));
		addMetadata(learningPoints, ColumnMetadata.named("learningPoints").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
		addMetadata(playerId, ColumnMetadata.named("playerId").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(skillType, ColumnMetadata.named("skillType").withIndex(3).ofType(Types.VARCHAR).withSize(2000).notNull());
	}

}

