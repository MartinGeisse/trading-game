package name.martingeisse.trading_game.platform.postgres.codegen;

import com.querydsl.sql.SchemaAndTable;
import com.querydsl.sql.codegen.DefaultNamingStrategy;
import com.querydsl.sql.codegen.MetaDataExporter;
import name.martingeisse.trading_game.platform.postgres.MyPostgresConfiguration;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.File;
import java.sql.Connection;

/**
 * Generates QueryDSL classes.
 */
public class PostgresQueryDslCodeGeneratorMain {

	// prevent instantiation
	private PostgresQueryDslCodeGeneratorMain() {
	}

	/**
	 * The main method.
	 *
	 * @param args ...
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {

		PGSimpleDataSource source = new PGSimpleDataSource();
		source.setServerName("localhost");
		source.setDatabaseName("trading_game");
		source.setUser("postgres");
		source.setPassword("postgres");

		try (Connection connection = source.getConnection()) {
			MetaDataExporter exporter = new MetaDataExporter();
			exporter.setTargetFolder(new File("src/main/generated"));
			exporter.setPackageName("name.martingeisse.trading_game.postgres_entities");
			exporter.setSerializerClass(MyMetaDataSerializer.class);
			exporter.setBeanSerializerClass(BeanSerializer.class);
			exporter.setConfiguration(MyPostgresConfiguration.CONFIGURATION);
			exporter.setNamingStrategy(new DefaultNamingStrategy() {
				@Override
				public String getClassName(String tableName) {
					return super.getClassName(tableName) + "Row";
				}
			});
			exporter.export(connection.getMetaData());
		}

	}

}