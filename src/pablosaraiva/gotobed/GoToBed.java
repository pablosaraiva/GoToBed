package pablosaraiva.gotobed;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import pablosaraiva.gotobed.annotations.SleeperId;


public class GoToBed {	
	private BedProvider bedProvider;

	public GoToBed(BedProvider bedProvider) {
		this.bedProvider = bedProvider;
	}

	public void sleep(Object obj) throws SQLException {
		Connection conn = bedProvider.getConnection();
		if (hasId(obj)) {
			update(obj);
		} else {
			insert(obj);
		}
		conn.close();
	}

	private void insert(Object obj) {
		System.out.println("will insert");
	}

	private void update(Object obj) {
		System.out.println("will update");
	}

	private boolean hasId(Object obj) {
		
		for (Field f: obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (f.isAnnotationPresent(SleeperId.class)) {
				try {
					f.setAccessible(true);
					Long id = (Long) f.get(obj);
					if (id != null && id > 0) {
						return true;
					} 
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			f.setAccessible(false);

		}
		return false;
	}

}
