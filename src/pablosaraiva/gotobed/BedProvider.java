package pablosaraiva.gotobed;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BedProvider {

	public abstract Connection getConnection() throws SQLException;

	
}
