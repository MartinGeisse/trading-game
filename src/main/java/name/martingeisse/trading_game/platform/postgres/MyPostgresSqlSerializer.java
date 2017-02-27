package name.martingeisse.trading_game.platform.postgres;

import com.querydsl.core.types.Path;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLSerializer;
import name.martingeisse.trading_game.tools.codegen.PostgresJsonb;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 */
public class MyPostgresSqlSerializer extends SQLSerializer {

	private final Configuration configuration;

	public MyPostgresSqlSerializer(Configuration configuration, boolean dml, boolean useLiterals) {
		super(configuration, dml);
		setUseLiterals(useLiterals);
		this.configuration = configuration;
	}

	// override to add type-casts to enum constants, both standalone and within collections
	@Override
	public void visitConstant(Object constant) {
		if (constant instanceof Collection<?>) {
			LinkedList<Path<?>> constantPaths = (LinkedList<Path<?>>)getConstantPaths();
			append("(");
			boolean first = true;
			for (Object element : ((Collection) constant)) {
				if (!first) {
					append(", ");
				}
				append("?");
				if (element instanceof Enum<?>) {
					append("::").append(getEnumTypeName(element.getClass()));
				}
				getConstants().add(element);
				if (first && (constantPaths.size() < getConstants().size())) {
					constantPaths.add(null);
				}
				first = false;
			}
			append(")");
			int size = ((Collection) constant).size() - 1;
			Path<?> lastPath = constantPaths.peekLast();
			for (int i = 0; i < size; i++) {
				constantPaths.add(lastPath);
			}
		} else {
			super.visitConstant(constant);
			if (constant instanceof Enum<?>) {
				append("::").append(getEnumTypeName(constant.getClass()));
			} else if (constant instanceof PostgresJsonb) {
				append("::jsonb");
			}
		}
	}

	private String getEnumTypeName(Class<?> c) {
		Class<?> sc = c.getSuperclass();
		if (Enum.class.equals(sc)) {
			return MyPostgresConfiguration.ENUM_CLASS_TO_TYPE_NAME.get(c);
		} else {
			return MyPostgresConfiguration.ENUM_CLASS_TO_TYPE_NAME.get(sc);
		}
	}

}
