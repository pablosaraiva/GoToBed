package pablosaraiva.gotobed;

import java.util.Date;

import org.junit.Test;

import pablosaraiva.gotobed.exception.BedException;
import pablosaraiva.gotobed.model.Kid;

public class GoToBedTest {

	@Test
	public void test() throws BedException {
		BedProvider provider = new SimpleBedProvider();
		GoToBed gtb = new GoToBed(provider);
		
		Kid k1 = new Kid();
		k1.setName("John Doe");
		k1.setBirthdate(new Date());
		gtb.sleep(k1);

	}

}
