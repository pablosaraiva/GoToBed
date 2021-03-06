package pablosaraiva.gotobed;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pablosaraiva.gotobed.bedproviders.BedProvider;
import pablosaraiva.gotobed.bedproviders.FileBedProvider;
import pablosaraiva.gotobed.exception.BedException;
import pablosaraiva.gotobed.model.Kid;

public class GoToBedTest {
	private GoToBed gtb;

	@Before
	public void init() {
		BedProvider provider = new FileBedProvider();
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
	
	@Test
	public void testSayGoodbyeToOneKidItIsLikeSendingHimToCollege() throws BedException {
		Kid newKidOnTheBlock = new Kid();
		newKidOnTheBlock.setName("Someone we don't know");
		
		List<Kid> kids;
		
		kids = gtb.wake(Kid.class);
		assertFalse(kids.contains(newKidOnTheBlock));
		assertTrue(newKidOnTheBlock.getId() == 0);

		gtb.sleep(newKidOnTheBlock);
		kids = gtb.wake(Kid.class);
		assertTrue(kids.contains(newKidOnTheBlock));
		assertTrue(newKidOnTheBlock.getId() > 0);

		gtb.goodbye(newKidOnTheBlock);
		kids = gtb.wake(Kid.class);
		assertFalse(kids.contains(newKidOnTheBlock));
		assertTrue(newKidOnTheBlock.getId() == 0);
	}
	
	@Test
	public void testUpdateObject() throws BedException {
		List<Kid> kids;
		
		Kid kid = new Kid();
		kid.setName("Jon Doe");
		gtb.sleep(kid);
		long thisKidId = kid.getId();

		kids = gtb.wake(Kid.class);
		assertTrue(kids.contains(kid));
		
		kid.setName("John Doe");
		kids = gtb.wake(Kid.class);
		assertFalse(kids.contains(kid));
		
		gtb.sleep(kid);
		kids = gtb.wake(Kid.class);
		assertTrue(kids.contains(kid));
		assertEquals(thisKidId, kid.getId());
	}

}
