GoToBed
=======

####Java ORM Made Easy

####WARNING: This is a library under early development stages. 

I hope to finish a usable version soon. :)

####THIS IS NOT READY FOR PRODUCTION ENVIRONMENTS.

This is a very simple persistence library so the programmer can focus at the real problem instead of maintaining database related code.

##How to persist object in 4 simple steps

###1 - Annotate the classes you want to persist with the @Sleeper annotation:

	@Sleeper
	public class Kid 

###2 - Create a Long or long field and annotate is with the @SleeperId annotation:

	@Sleeper
	public class Kid {
		@SleeperId
		private long id;
	}

The name of the field does not matter;
The visibility of the field does not matter;
GoToBed does not need getter and setter methods (you can create them if you want to).

###3 - Choose a bed provider or create one

####3.1 MemoryBedProvider

The memory provider gives you a database that will run as long as your application is running. It is well suited for cache.

####3.2 FileBedProvider

The file provider gives you a hypersonic sql database stored at your local disk.

####3.3 Create your own provider

This way you can create your own bed provider that gives connections to whatever database you want to use.

######Currently, GoToBed is only tested in Hypersonic Database.

This is a very simple implementatin of the BedProvider that works:

	public class SampleBedProvider extends BedProvider {
		static  {
			try {
				Class.forName("org.hsqldb.jdbc.JDBCDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	
		@Override
		public Connection getConnection() throws SQLException {
			return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/", "SA", "");
		}
	}

###4 - Send your object to bed:

	BedProvider provider = new MemoryBedProvider();
	GoToBed gtb = new GoToBed(provider);
		
	Kid k1 = new Kid();
	gtb.sleep(k1);
		
That's it. At this point, GoToBed already created your table and inserted your data.

##Updating objects is just that easy

###Just tell them to sleep again

	gtb.sleep(k1);

##How to get your objects back from the database?

###All you have to do is ask GoToBed to wake'em up. 

It can't get easier than this.

		List<Kid> kids = gtb.wake(Kid.class);
		
##How to say goodbye to your objects

###If you don't want an object anymore, all you have to do is say goodbye

It would be nice if real life were like this.

	gtb.goodbye(newKidOnTheBlock);
	
This test case shows a object being created, consulted and then discarded.

	@Test
	public void testSayGoodbyeToOneKidItIsLikeSendingHimToCollege() throws BedException {
		BedProvider provider = new MemoryBedProvider();
		GoToBed gtb = new GoToBed(provider);
		
		Kid newKidOnTheBlock = new Kid();
		newKidOnTheBlock.setName("Someone we don't know yet");
		
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
	
		
###What GoToBed can do:

Create tables to store your objects;
Alter tables as your objects gain new fields;
Store String, long, int, Long, Integer and Date fields.
Read your objects back from the database;

###What GoToBed will do soon:

Store fields that relate to other Sleepers;
Filter objects you want to get back, so you don't have to load all your objects of a single type at once.

###What GoToBed will not do (at least soon):

Create indexes to optimize queries (although you can do it yourself after the tables are created);
Deal with fancy database relationships. This is meant to be simple.

##Contacts

Twitter @pablosaraiva
