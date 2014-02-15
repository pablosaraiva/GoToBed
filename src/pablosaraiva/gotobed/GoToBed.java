package pablosaraiva.gotobed;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import org.reflections.Reflections;

import pablosaraiva.gotobed.annotations.DoNotSave;
import pablosaraiva.gotobed.annotations.Id;
import pablosaraiva.gotobed.annotations.Sleeper;

public class GoToBed {	
	static {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Reflections reflections = new Reflections();
		Set<Class<?>> sleeperClasses = reflections.getTypesAnnotatedWith(Sleeper.class);
		for (Class<?> clazz: sleeperClasses) {
			try {
				updateTable(clazz);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/", "SA", "");
	}

	public void sleep(Object obj) {

	}

	private static void updateTable(Class<?> clazz) throws SQLException {
		if (existsTableForClass(clazz)) {
			updateTableColumnsForFlass(clazz);
		} else {
			createTableForClass(clazz);
		}
	}

	private static void createTableForClass(Class<?> clazz) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(tableNameFor(clazz));
		sb.append("(");
		sb.append("id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY");
		for (Field f: clazz.getDeclaredFields()) {
			if (isPersistableAndNotId(f)) {
				String columnType = getStringColumnTypeFor(f.getType());
				sb.append(",");
				sb.append(columnNameFor(f));
				sb.append(" ");
				sb.append(columnType);
			}
		}
		sb.append(")");
		System.out.println(sb.toString());
		Connection conn = getConnection();
		Statement st = conn.createStatement();
		st.execute(sb.toString());
		conn.close();
	}

	private static String columnNameFor(Field f) {
		return f.getName().toUpperCase();
	}

	private static boolean isPersistableAndNotId(Field f) {
		if (f.isAnnotationPresent(Id.class)) {
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
	

	private static boolean persistenceIsImplementedForClass(Class<?> clazz) {
		String columnType = getStringColumnTypeFor(clazz);
		if ((columnType == null) || ("".equals(columnType))) {
			return false;
		} else {
			return true;
		}
	}



	private static String getStringColumnTypeFor(Class<?> clazz) {
		if (clazz == String.class) {
			return "VARCHAR(128)";
		} else if (clazz == Long.class) {
			return "BIGINT";
		} else if (clazz == Integer.class) {
			return "INT";
		} else if (clazz == Boolean.class) {
			return "BOOLEAN";
		} else if (clazz == java.util.Date.class) {
			return "DATE";
		} else {
			return null;
		}
	}

	private static String tableNameFor(Class<?> clazz) {
		return clazz.getSimpleName().toUpperCase();
	}

	private static void updateTableColumnsForFlass(Class<?> clazz) {
		// TODO Auto-generated method stub
		
	}

	private static boolean existsTableForClass(Class<?> clazz) {
		return false;
	}

}
