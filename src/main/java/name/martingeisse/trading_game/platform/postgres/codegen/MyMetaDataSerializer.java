package name.martingeisse.trading_game.platform.postgres.codegen;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.querydsl.codegen.EntityType;
import com.querydsl.codegen.Property;
import com.querydsl.codegen.TypeMappings;
import com.querydsl.sql.codegen.MetaDataSerializer;
import com.querydsl.sql.codegen.NamingStrategy;
import com.querydsl.sql.codegen.SQLCodegenModule;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.Set;

/**
 *
 */
public class MyMetaDataSerializer extends MetaDataSerializer {

	private final Class<?> entityPathType;

	/**
	 * Constructor.
	 *
	 * @param typeMappings
	 * @param namingStrategy
	 * @param innerClassesForKeys
	 * @param imports
	 * @param columnComparator
	 * @param entityPathType
	 */
	@Inject
	public MyMetaDataSerializer(TypeMappings typeMappings, NamingStrategy namingStrategy, @Named(SQLCodegenModule.INNER_CLASSES_FOR_KEYS) boolean innerClassesForKeys,
								@Named(SQLCodegenModule.IMPORTS) Set<String> imports, @Named(SQLCodegenModule.COLUMN_COMPARATOR) Comparator<Property> columnComparator,
								@Named(SQLCodegenModule.ENTITYPATH_TYPE) Class<?> entityPathType) {
		super(typeMappings, namingStrategy, innerClassesForKeys, imports, columnComparator, entityPathType);
		this.entityPathType = entityPathType;
	}

	@Override
	protected void introClassHeader(CodeWriter writer, EntityType model) throws IOException {
		Type queryType = typeMappings.getPathType(model, model, true);

		writer.line("@Generated(\"", getClass().getName(), "\")");
		writer.line("@SuppressWarnings(\"all\")");

		TypeCategory category = model.getOriginalCategory();
		// serialize annotations only, if no bean types are used
		if (model.equals(queryType)) {
			for (Annotation annotation : model.getAnnotations()) {
				writer.annotation(annotation);
			}
		}
		writer.beginClass(queryType, new ClassType(category, entityPathType, model));
		writer.privateStaticFinal(Types.LONG_P, "serialVersionUID", String.valueOf(model.hashCode()));
	}

}
