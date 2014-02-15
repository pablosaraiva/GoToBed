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
		k1.setAge(0);
		k1.setBirthdate(new Date());
		System.out.println(k1.getId());
		gtb.sleep(k1);
		System.out.println(k1.getId());

	}

}
