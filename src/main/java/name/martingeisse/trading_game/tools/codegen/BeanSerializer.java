package name.martingeisse.trading_game.tools.codegen;

import com.mysema.codegen.CodeWriter;
import com.mysema.codegen.model.*;
import com.querydsl.codegen.*;
import com.querydsl.core.util.BeanUtils;
import com.querydsl.sql.codegen.support.PrimaryKeyData;
import com.querydsl.sql.dml.SQLInsertClause;
import name.martingeisse.trading_game.common.util.WtfException;
import name.martingeisse.trading_game.platform.postgres.PostgresConnection;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Customized bean serializer that creates the Java beans (not the query classes) from the database
 * schema.
 */
public class BeanSerializer extends SerializerHelper implements Serializer {

	private final QueryTypeFactory queryTypeFactory;

	@Inject
	public BeanSerializer(QueryTypeFactory queryTypeFactory) {
		this.queryTypeFactory = queryTypeFactory;
	}

	/* (non-Javadoc)
		 * @see com.mysema.query.codegen.Serializer#serialize(com.mysema.query.codegen.EntityType, com.mysema.query.codegen.SerializerConfig, com.mysema.codegen.CodeWriter)
		 */
	@Override
	public void serialize(final EntityType entityType, final SerializerConfig config, final CodeWriter w) throws IOException {
		@SuppressWarnings("unused")
		final String tableName = entityType.getData().get("table").toString();
		@SuppressWarnings("unchecked")
		final Collection<PrimaryKeyData> primaryKeys = (Collection<PrimaryKeyData>) entityType.getData().get(PrimaryKeyData.class);

		// determine primary key
		PrimaryKeyData primaryKey;
		if (primaryKeys == null || primaryKeys.size() == 0) {
			primaryKey = null;
		} else if (primaryKeys.size() == 1) {
			primaryKey = primaryKeys.iterator().next();
		} else {
			throw new WtfException("Found entity with more than one primary key: " + entityType.getSimpleName());
		}

		// determine primary-key and non-primary-key fields
		List<Property> primaryKeyProperties;
		if (primaryKey == null) {
			primaryKeyProperties = new ArrayList<>();
		} else {
			primaryKeyProperties = new ArrayList<>();
			propertyLoop:
			for (final Property property : entityType.getProperties()) {
				for (final String primaryKeyPropertyName : primaryKey.getColumns()) {
					if (primaryKeyPropertyName.equals(property.getName())) {
						primaryKeyProperties.add(property);
						continue propertyLoop;
					}
				}
			}
		}

		// determine if we have an "ID"
		final Property idProperty;
		if (primaryKeyProperties.size() == 1 && "id".equals(primaryKeyProperties.get(0).getName())) {
			idProperty = primaryKeyProperties.get(0);
		} else {
			idProperty = null;
		}

		// map properties by name
		final Map<String, Property> propertiesByName = new HashMap<>();
		for (final Property property : entityType.getProperties()) {
			propertiesByName.put(property.getEscapedName(), property);
		}

		// check if the entity has a boolean "deleted" property
		Property deletedFlagProperty = propertiesByName.get("deleted");
		if (deletedFlagProperty != null && deletedFlagProperty.getType().getJavaClass() != Boolean.class && deletedFlagProperty.getType().getJavaClass() != Boolean.TYPE) {
			deletedFlagProperty = null;
		}

		// check if the entity has an "orderIndex" property
		Property orderIndexProperty = propertiesByName.get("orderIndex");
		if (orderIndexProperty != null && orderIndexProperty.getType().getJavaClass() != Integer.class && orderIndexProperty.getType().getJavaClass() != Integer.TYPE) {
			orderIndexProperty = null;
		}

		// file comment
		printFileComment(w);

		// package declaration
		if (!entityType.getPackageName().isEmpty()) {
			w.packageDecl(entityType.getPackageName());
		}

		// imports
		printImports(entityType, config, w, idProperty != null, deletedFlagProperty != null, orderIndexProperty != null);

		// class Javadoc comment
		w.javadoc("This class represents rows from table '" + entityType.getData().get("table") + "'.");

		// class annotations
		printAnnotations(entityType.getAnnotations(), w);

		// begin writing the class itself
		final List<Type> interfaces = new ArrayList<>();
		interfaces.add(new SimpleType("Serializable"));
		w.beginClass(entityType, null, interfaces.toArray(new Type[0]));

		// add an explicit empty constructor because it looks nicer
		w.javadoc("Constructor.");
		w.beginConstructor();
		w.end();

		// generate the data fields
		for (final Property property : entityType.getProperties()) {
			w.javadoc("the " + property.getEscapedName());
			printAnnotations(property.getAnnotations(), w);
			w.privateField(property.getType(), property.getEscapedName());
		}

		// generate accessor methods
		for (final Property property : entityType.getProperties()) {
			final String propertyName = property.getEscapedName();
			final String capitalizedPropertyName = BeanUtils.capitalize(propertyName);
			final Type propertyType = property.getType();

			// getter method
			w.javadoc("Getter method for the " + propertyName + ".", "", "@return the " + propertyName);
			w.beginPublicMethod(propertyType, "get" + capitalizedPropertyName);
			w.line("return ", propertyName, ";");
			w.end();

			// setter method
			w.javadoc("Setter method for the " + propertyName + ".", "", "@param " + propertyName + " the " + propertyName + " to set");
			final Parameter parameter = new Parameter(propertyName, propertyType);
			w.beginPublicMethod(Types.VOID, "set" + capitalizedPropertyName, parameter);
			w.line("this.", propertyName, " = ", propertyName, ";");
			w.end();

		}

		// generate static helper methods
		String qLine;
		{
			Type queryType = queryTypeFactory.create(entityType);
			String qtype = queryType.getSimpleName();
			String qField = entityType.getSimpleName();
			if (!qField.endsWith("Row")) {
				throw new WtfException(qField);
			}
			qField = qField.substring(0, qField.length() - 3);
			qLine = qtype + " q = " + qtype + "." + qField + ";";
		}
		Parameter connection = new Parameter("connection", new SimpleType("PostgresConnection"));
		if (idProperty != null) {

			// loadById
			w.javadoc("Loads the instance with the specified ID.", "", "@param connection the database connection", "@param id the ID of the instance to load", "@return the loaded instance");
			w.beginStaticMethod(entityType, "loadById", connection, new Parameter("id", idProperty.getType()));
			w.line(qLine);
			w.line("return connection.query().select(q).from(q).where(q.id.eq(id)).fetchFirst();");
			w.end();

			// insert
			w.javadoc("Inserts this instance into the database. This object must not have an ID yet.");
			w.beginPublicMethod(new SimpleType("void"), "insert", connection);
			w.line("if (id != null) {");
			w.line("	throw new IllegalStateException(\"this object already has an id: \" + id);");
			w.line("}");
			w.line(qLine);
			w.line("SQLInsertClause insert = connection.insert(q);");
			for (final Property property : entityType.getProperties()) {
				if (property != idProperty) {
					w.line("insert.set(q." + property.getEscapedName() + ", " + property.getEscapedName() + ");");
				}
			}
			w.line("id = insert.executeWithKey(" + idProperty.getType() + ".class);");
			w.end();

		}

		// generate toString() method
		w.line("/* (non-Javadoc)");
		w.line(" * @see java.lang.Object#toString()");
		w.line(" */");
		w.annotation(Override.class);
		w.beginPublicMethod(Types.STRING, "toString");
		final StringBuilder builder = new StringBuilder();
		for (final Property property : entityType.getProperties()) {
			final String propertyName = property.getEscapedName();
			builder.append(builder.length() == 0 ? ("\"{" + entityType.getSimpleName() + ". ") : " + \", ");
			builder.append(propertyName);
			builder.append(" = \" + ");
			if (property.getType().getCategory() == TypeCategory.ARRAY) {
				builder.append("Arrays.toString(").append(propertyName).append(")");
			} else {
				builder.append(propertyName);
			}
		}
		w.line("return ", builder.toString(), " + \"}\";");
		w.end();

		// finish writing the class itself
		w.end();

	}

	/**
	 * Prints import clauses.
	 */
	private void printImports(final EntityType entityType, final SerializerConfig config, final CodeWriter w, final boolean hasId, final boolean hasDeletedFlag, final boolean hasOrderIndex)
			throws IOException {

		// to avoid duplicate imports, we first collect all imports in a set
		final Set<String> imports = new HashSet<>();

		// annotation types
		addAnnotationImports(imports, entityType.getAnnotations());
		for (final Property property : entityType.getProperties()) {
			addAnnotationImports(imports, property.getAnnotations());
		}

		// collection types
		addIf(imports, List.class.getName(), entityType.hasLists());
		addIf(imports, Collection.class.getName(), entityType.hasCollections());
		addIf(imports, Set.class.getName(), entityType.hasSets());
		addIf(imports, Map.class.getName(), entityType.hasMaps());

		// utility classes
		addIf(imports, Arrays.class.getName(), entityType.hasArrays());
		imports.add("java.io.Serializable");

		// query-class
		addIf(imports, PostgresConnection.class.getName(), hasId);
		addIf(imports, SQLInsertClause.class.getName(), hasId);

		// actually write the imports
		printImports(w, imports);

	}

	/**
	 * Adds imports for all annotations to the import collection.
	 */
	private void addAnnotationImports(final Collection<String> imports, final Collection<Annotation> annotations) {
		for (final Annotation annotation : annotations) {
			imports.add(annotation.annotationType().getName());
		}
	}

}