package pablosaraiva.gotobed;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pablosaraiva.gotobed.exception.BedException;
import pablosaraiva.gotobed.model.Kid;

public class GoToBedTest {
	private GoToBed gtb;

	@Before
	public void init() {
		BedProvider provider = new SimpleBedProvider();
		gtb = new GoToBed(provider);
	}

	@Test
	public void testCreateAKidAndPutHimToSleep() throws BedException {
		Kid k1 = new Kid();
		k1.setName("John Doe");
		k1.setBirthdate(new Date());
		gtb.sleep(k1);
	}
	
	@Test
	public void testWakeUpAllKids() throws BedException {
		List<Kid> kids = gtb.wake(Kid.class);
		for (Kid kid: kids) {
			System.out.println(kid.getId() + ": " + kid.getName());
		}
	}

}
