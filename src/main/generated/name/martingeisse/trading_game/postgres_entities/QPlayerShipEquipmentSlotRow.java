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
 * QPlayerShipEquipmentSlotRow is a Querydsl query type for PlayerShipEquipmentSlotRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QPlayerShipEquipmentSlotRow extends com.querydsl.sql.RelationalPathBase<PlayerShipEquipmentSlotRow> {

	private static final long serialVersionUID = 639401754;

	public static final QPlayerShipEquipmentSlotRow PlayerShipEquipmentSlot = new QPlayerShipEquipmentSlotRow("PlayerShipEquipmentSlot");

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final StringPath itemType = createString("itemType");

	public final EnumPath<name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentSlotType> slotType = createEnum("slotType", name.martingeisse.trading_game.game.equipment.PlayerShipEquipmentSlotType.class);

	public final NumberPath<Long> spaceObjectBaseDataId = createNumber("spaceObjectBaseDataId", Long.class);

	public final com.querydsl.sql.PrimaryKey<PlayerShipEquipmentSlotRow> playerShipEquipmentSlotPkey = createPrimaryKey(id);

	public final com.querydsl.sql.ForeignKey<SpaceObjectBaseDataRow> playerShipEquipmentSlotSpaceObjectBaseDataIdFkey = createForeignKey(spaceObjectBaseDataId, "id");

	public QPlayerShipEquipmentSlotRow(String variable) {
		super(PlayerShipEquipmentSlotRow.class, forVariable(variable), "game", "PlayerShipEquipmentSlot");
		addMetadata();
	}

	public QPlayerShipEquipmentSlotRow(String variable, String schema, String table) {
		super(PlayerShipEquipmentSlotRow.class, forVariable(variable), schema, table);
		addMetadata();
	}

	public QPlayerShipEquipmentSlotRow(Path<? extends PlayerShipEquipmentSlotRow> path) {
		super(path.getType(), path.getMetadata(), "game", "PlayerShipEquipmentSlot");
		addMetadata();
	}

	public QPlayerShipEquipmentSlotRow(PathMetadata metadata) {
		super(PlayerShipEquipmentSlotRow.class, metadata, "game", "PlayerShipEquipmentSlot");
		addMetadata();
	}

	public void addMetadata() {
		addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(itemType, ColumnMetadata.named("itemType").withIndex(4).ofType(Types.VARCHAR).withSize(2000).notNull());
		addMetadata(slotType, ColumnMetadata.named("slotType").withIndex(3).ofType(Types.VARCHAR).withSize(2147483647).notNull());
		addMetadata(spaceObjectBaseDataId, ColumnMetadata.named("spaceObjectBaseDataId").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
	}

}

