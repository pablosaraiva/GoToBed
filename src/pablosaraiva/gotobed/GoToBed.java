package pablosaraiva.gotobed;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import pablosaraiva.gotobed.annotations.DoNotSave;
import pablosaraiva.gotobed.annotations.Sleeper;
import pablosaraiva.gotobed.annotations.SleeperId;
import pablosaraiva.gotobed.exception.BedException;

public class GoToBed {
	private BedProvider bedProvider;

	public GoToBed(BedProvider bedProvider) {
		this.bedProvider = bedProvider;

		Reflections reflections = new Reflections();
		Set<Class<?>> sleeperClasses = reflections
				.getTypesAnnotatedWith(Sleeper.class);
		for (Class<?> clazz : sleeperClasses) {
			try {
				updateTable(clazz);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void sleep(Object obj) throws BedException {
		try {
			if (hasId(obj)) {
				update(obj);
			} else {
				insert(obj);
			}

		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			throw new BedException(e);
		}
	}

	private void insert(Object obj) throws SQLException, IllegalArgumentException, IllegalAccessException {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(tableNameFor(obj.getClass()));
		sb.append(" (");
		boolean first = true;
		List<Field> fieldsToBePersisted = getPersistableFieldsFor(obj.getClass());
		for (Field f : fieldsToBePersisted) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append(columnNameFor(f));
		}

		sb.append(") VALUES (");
		first = true;
		for (int i = 0; i < fieldsToBePersisted.size(); i++) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}

			sb.append("?");
		}
		sb.append(")");
		System.out.println(sb.toString());

		Connection conn = bedProvider.getConnection();
		PreparedStatement ps = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
		for (int i = 0; i < fieldsToBePersisted.size(); i++) {
			Field f = fieldsToBePersisted.get(i);
			f.setAccessible(true);
			ps.setObject(i+1, f.get(obj));
			f.setAccessible(false);
		}
		ps.execute();
		
		ResultSet generatedKeys = ps.getGeneratedKeys();
		if (generatedKeys.next()) {
			Field f = getIdFieldFor(obj.getClass());
			f.setAccessible(true);
			f.set(obj, generatedKeys.getLong(1));
			f.setAccessible(false);
		}

		conn.close();
	}

	private Field getIdFieldFor(Class<?> clazz) {
		Field idField = null;
		for (Field f : clazz.getDeclaredFields()) {
			if (f.isAnnotationPresent(SleeperId.class)) {
				if (f.getType() == Long.class || f.getType() == Long.TYPE) {
					idField = f;
					break;
				}
			}
		}
		return idField;
	}

	private void update(Object obj) throws IllegalArgumentException, IllegalAccessException, SQLException {
		List<Field> persistableFields = getPersistableFieldsFor(obj.getClass());
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(tableNameFor(obj.getClass()));
		boolean first = true;
		for (Field f: persistableFields) {
			if (first) {
				sb.append(" SET "); 
				first = false;
			} else {
				sb.append(", ");
			}
			sb.append(columnNameFor(f));
			sb.append(" = ?");
		}
		sb.append(" WHERE ID = ");
		sb.append(getIdFor(obj));
		System.out.println(sb.toString());
		Connection conn = bedProvider.getConnection();
		PreparedStatement ps = conn.prepareStatement(sb.toString());
		for (int i = 0; i < persistableFields.size(); i++) {
			Field f = persistableFields.get(i);
			f.setAccessible(true);
			ps.setObject(i+1, f.get(obj));
			f.setAccessible(false);
		}
		ps.execute();
	}

	private boolean hasId(Object obj) throws IllegalArgumentException, IllegalAccessException {

		Field idField = getIdFieldFor(obj.getClass());
		if (idField != null) {
			idField.setAccessible(true);
			Long id = (Long) idField.get(obj);
			idField.setAccessible(false);
			if (id != null && id > 0) {
				return true;
			}
		}
		return false;
	}

	private void updateTable(Class<?> clazz) throws SQLException {
		if (existsTableForClass(clazz)) {
			updateTableColumnsForFlass(clazz);
		} else {
			createTableForClass(clazz);
		}
	}

	private void createTableForClass(Class<?> clazz) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(tableNameFor(clazz));
		sb.append("(");
		sb.append("ID BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY");
		for (Field f : getPersistableFieldsFor(clazz)) {
			String columnType = getStringColumnTypeFor(f.getType());
			sb.append(",");
			sb.append(columnNameFor(f));
			sb.append(" ");
			sb.append(columnType);
		}
		sb.append(")");
		System.out.println(sb.toString());
		Connection conn = bedProvider.getConnection();
		Statement st = conn.createStatement();
		st.execute(sb.toString());
		conn.close();
	}

	private String columnNameFor(Field f) {
		return f.getName().toUpperCase();
	}

	private boolean isPersistableAndNotId(Field f) {
		if (f.isAnnotationPresent(SleeperId.class)) {
			return false;
		} else if (f.isAnnotationPresent(DoNotSave.class)) {
			return false;
		} else if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
			return false;
		} else if (!persistenceIsImplementedForClass(f.getType())) {
			return false;
		} else {
			return true;
		}
	}

	private boolean persistenceIsImplementedForClass(Class<?> clazz) {
		String columnType = getStringColumnTypeFor(clazz);
		if ((columnType == null) || ("".equals(columnType))) {
			return false;
		} else {
			return true;
		}
	}

	private String getStringColumnTypeFor(Class<?> clazz) {
		if (clazz == String.class) {
			return "VARCHAR(128)";
		} else if (clazz == Long.class || clazz == Long.TYPE) {
			return "BIGINT";
		} else if (clazz == Integer.class || clazz == Integer.TYPE) {
			return "INT";
		} else if (clazz == Boolean.class || clazz == Boolean.TYPE) {
			return "BOOLEAN";
		} else if (clazz == java.util.Date.class) {
			return "DATE";
		} else {
			return null;
		}
	}

	private String tableNameFor(Class<?> clazz) {
		return clazz.getSimpleName().toUpperCase();
	}

	private void updateTableColumnsForFlass(Class<?> clazz) throws SQLException {
		for (Field f : getPersistableFieldsFor(clazz)) {
			if (isPersistableAndNotId(f)) {
				if (!existsColumnFor(f)) {
					createColumnFor(f);
				}
			}
		}

	}

	private boolean existsColumnFor(Field f) throws SQLException {
		boolean exists;
		Connection conn = bedProvider.getConnection();
		DatabaseMetaData metadata = conn.getMetaData();
		ResultSet rs = metadata.getColumns(null, null,
				tableNameFor(f.getDeclaringClass()), columnNameFor(f));
		if (rs.next()) {
			exists = true;
		} else {
			exists = false;
		}
		conn.close();
		return exists;
	}

	private void createColumnFor(Field f) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE ");
		sb.append(tableNameFor(f.getDeclaringClass()));
		sb.append(" ADD ");
		sb.append(columnNameFor(f));
		sb.append(" ");
		sb.append(getStringColumnTypeFor(f.getType()));
		System.out.println(sb.toString());
		Connection conn = bedProvider.getConnection();
		Statement st = conn.createStatement();
		st.execute(sb.toString());
		conn.close();
	}

	private boolean existsTableForClass(Class<?> clazz) throws SQLException {
		boolean exists;
		Connection conn = bedProvider.getConnection();
		DatabaseMetaData metadata = conn.getMetaData();
		ResultSet rs = metadata.getTables(null, null, tableNameFor(clazz), null);
		if (rs.next()) {
			exists = true;
		} else {
			exists = false;
		}
		conn.close();
		return exists;
	}
	
	private List<Field> getPersistableFieldsFor(Class<?> clazz) {
		List<Field> persistableFields = new ArrayList<>();
		for (Field f : clazz.getDeclaredFields()) {
			if (isPersistableAndNotId(f)) {
				persistableFields.add(f);
			}
		}
		return persistableFields;
	}

	public <T> List<T> wake(Class<T> clazz) throws BedException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ID");
		List<Field> persistableFields = getPersistableFieldsFor(clazz);
		for (Field f: persistableFields) {
			sb.append(", ");
			sb.append(columnNameFor(f));
		}
		sb.append(" from ");
		sb.append(tableNameFor(clazz));
		System.out.println(sb.toString());
		List<T> wakers = new ArrayList<>();
		try (Connection conn = bedProvider.getConnection()) {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sb.toString());
			while (rs.next()) {
				T obj = clazz.newInstance();
				Field idField = getIdFieldFor(clazz);
				idField.setAccessible(true);
				idField.set(obj, rs.getObject("ID"));
				idField.setAccessible(false);
				for (Field f: persistableFields) {
					f.setAccessible(true);
					f.set(obj, rs.getObject(columnNameFor(f)));
					f.setAccessible(false);
				}
				wakers.add(obj);
			}
		} catch (SQLException | InstantiationException | IllegalAccessException e) {
			throw new BedException(e);
		}
		return wakers;
	}

	public void goodbye(Object obj) throws BedException {
		Long id;
		try {
			id = getIdFor(obj);
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM ");
			sb.append(tableNameFor(obj.getClass()));
			sb.append(" WHERE ID = ");
			sb.append(id);
			System.out.println(sb.toString());
			Connection conn = bedProvider.getConnection();
			Statement st = conn.createStatement();
			st.execute(sb.toString());
			setIdFor(obj, 0L);
		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			throw new BedException(e);
		}

	}

	private void setIdFor(Object obj, Long id) throws IllegalArgumentException, IllegalAccessException {
		Field f = getIdFieldFor(obj.getClass());
		f.setAccessible(true);
		f.set(obj, id);
		f.setAccessible(false);
	}

	private Long getIdFor(Object obj) throws IllegalArgumentException, IllegalAccessException {
		Field f = getIdFieldFor(obj.getClass());
		f.setAccessible(true);
		Long id = (Long) f.get(obj);
		f.setAccessible(false);
		return id;
	}

}
