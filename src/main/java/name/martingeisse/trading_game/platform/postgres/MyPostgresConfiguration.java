package name.martingeisse.trading_game.platform.postgres;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.types.EnumByNameType;
import name.martingeisse.trading_game.game.space.SpaceObjectType;
import name.martingeisse.trading_game.tools.codegen.PostgresJsonb;
import name.martingeisse.trading_game.tools.codegen.PostgresJsonbType;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.postgresql.geometric.PGpoint;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class MyPostgresConfiguration {

	/**
	 * A mapping of class-to-Postgres-type-name for all custom-defined Postgres enum types.
	 */
	public static final Map<Class<? extends Enum<?>>, String> ENUM_CLASS_TO_TYPE_NAME;

	/**
	 * SQL TEMPLATES.
	 */
	public static final SQLTemplates TEMPLATES;

	/**
	 * The customized configuration.
	 */
	public static final Configuration CONFIGURATION;

	static {

		ENUM_CLASS_TO_TYPE_NAME = new HashMap<>();
		ENUM_CLASS_TO_TYPE_NAME.put(SpaceObjectType.class, "\"game\".\"SpaceObjectType\"");

		TEMPLATES = PostgreSQLTemplates.builder().quote().printSchema().build();
		for (Class enumClass : ENUM_CLASS_TO_TYPE_NAME.keySet()) {
			TEMPLATES.getCustomTypes().add(new EnumByNameType<>(enumClass));
		}
		TEMPLATES.getCustomTypes().add(new PostgresJsonbType());

		CONFIGURATION = new Configuration(TEMPLATES);
		for (Map.Entry<Class<? extends Enum<?>>, String> entry : ENUM_CLASS_TO_TYPE_NAME.entrySet()) {
			CONFIGURATION.registerType(entry.getValue(), entry.getKey());
		}
		CONFIGURATION.registerType("timestamptz", DateTime.class);
		CONFIGURATION.registerType("timestamp", LocalDateTime.class);
		CONFIGURATION.registerType("date", LocalDate.class);
		CONFIGURATION.registerType("time", LocalTime.class);
		CONFIGURATION.registerType("jsonb", PostgresJsonb.class);
		CONFIGURATION.registerType("point", PGpoint.class);

	}

	// prevent instantiation
	private MyPostgresConfiguration() {
	}

}
