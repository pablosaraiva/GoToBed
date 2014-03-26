package pablosaraiva.gotobed.bedproviders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FileBedProvider extends BedProvider {
	static  {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		 return DriverManager.getConnection("jdbc:hsqldb:file:./gotobed/db/", "SA", "");
	}
}
