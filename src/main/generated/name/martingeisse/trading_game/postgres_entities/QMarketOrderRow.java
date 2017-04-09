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
 * QMarketOrderRow is a Querydsl query type for MarketOrderRow
 */
@Generated("name.martingeisse.trading_game.tools.codegen.MyMetaDataSerializer")
@SuppressWarnings("all")
public class QMarketOrderRow extends com.querydsl.sql.RelationalPathBase<MarketOrderRow> {

	private static final long serialVersionUID = -923158217;

	public static final QMarketOrderRow MarketOrder = new QMarketOrderRow("MarketOrder");

	public final NumberPath<Long> id = createNumber("id", Long.class);

	public final NumberPath<Long> locationSpaceObjectBaseDataId = createNumber("locationSpaceObjectBaseDataId", Long.class);

	public final NumberPath<Long> principalPlayerId = createNumber("principalPlayerId", Long.class);

	public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

	public final StringPath type = createString("type");

	public final NumberPath<Long> unitPrice = createNumber("unitPrice", Long.class);

	public final com.querydsl.sql.PrimaryKey<MarketOrderRow> marketOrderPkey = createPrimaryKey(id);

	public final com.querydsl.sql.ForeignKey<SpaceObjectBaseDataRow> marketOrderLocationSpaceObjectBaseDataIdFkey = createForeignKey(locationSpaceObjectBaseDataId, "id");

	public final com.querydsl.sql.ForeignKey<PlayerRow> marketOrderPrincipalPlayerIdFkey = createForeignKey(principalPlayerId, "id");

	public QMarketOrderRow(String variable) {
		super(MarketOrderRow.class, forVariable(variable), "game", "MarketOrder");
		addMetadata();
	}

	public QMarketOrderRow(String variable, String schema, String table) {
		super(MarketOrderRow.class, forVariable(variable), schema, table);
		addMetadata();
	}

	public QMarketOrderRow(Path<? extends MarketOrderRow> path) {
		super(path.getType(), path.getMetadata(), "game", "MarketOrder");
		addMetadata();
	}

	public QMarketOrderRow(PathMetadata metadata) {
		super(MarketOrderRow.class, metadata, "game", "MarketOrder");
		addMetadata();
	}

	public void addMetadata() {
		addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(locationSpaceObjectBaseDataId, ColumnMetadata.named("locationSpaceObjectBaseDataId").withIndex(3).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(principalPlayerId, ColumnMetadata.named("principalPlayerId").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
		addMetadata(quantity, ColumnMetadata.named("quantity").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
		addMetadata(type, ColumnMetadata.named("type").withIndex(4).ofType(Types.VARCHAR).withSize(2147483647).notNull());
		addMetadata(unitPrice, ColumnMetadata.named("unitPrice").withIndex(6).ofType(Types.BIGINT).withSize(19).notNull());
	}

}

