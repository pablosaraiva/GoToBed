package pablosaraiva.gotobed;

import org.junit.Test;

import pablosaraiva.gotobed.model.Kid;

public class GoToBedTest {

	@Test
	public void test() {
		BedProvider provider = new MyBedProvider();
		GoToBed gtb = new GoToBed(provider);
		
		Kid k1 = new Kid();
		gtb.sleep(k1);
	}

}
