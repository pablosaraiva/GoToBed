package pablosaraiva.gotobed;

import org.junit.Test;

import pablosaraiva.gotobed.model.Kid;

public class GoToBedTest {

	@Test
	public void test() {
		GoToBed gtb = new GoToBed();
		
		Kid k1 = new Kid();
		gtb.sleep(k1);
	}

}
