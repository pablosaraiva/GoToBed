package pablosaraiva.gotobed;

import java.sql.SQLException;

import org.junit.Test;

import pablosaraiva.gotobed.model.Kid;

public class GoToBedTest {

	@Test
	public void test() throws SQLException {
		BedProvider provider = new SimpleBedProvider();
		GoToBed gtb = new GoToBed(provider);
		
		Kid k1 = new Kid();
		System.out.println(k1.getId());
		gtb.sleep(k1);
		System.out.println(k1.getId());

	}

}
