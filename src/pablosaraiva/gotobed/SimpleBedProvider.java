package pablosaraiva.gotobed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SimpleBedProvider extends BedProvider {
	static  {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/", "SA", "");
	}


}
