package name.martingeisse.trading_game.postgres.codegen;

import com.querydsl.sql.codegen.MetaDataExporter;
import name.martingeisse.trading_game.postgres.MyPostgresConfiguration;
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
			exporter.setBeanSerializer(new BeanSerializer());
			exporter.setConfiguration(MyPostgresConfiguration.CONFIGURATION);
			exporter.export(connection.getMetaData());
		}

	}

}