GoToBed
=======

####Java Persistence Made Easy

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

###3 - You need to extend the BedProvider class so you can give this library database connections:

The reason for this being this way is, the programmer can use any connection pool, connections strings and whatever he wants. All this library needs is a place where it can request connections.

This is a very simple implementatin of the BedProvider that works:

	public class SimpleBedProvider extends BedProvider {
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

	BedProvider provider = new SimpleBedProvider();
	GoToBed gtb = new GoToBed(provider);
		
	Kid k1 = new Kid();
	gtb.sleep(k1);
		
That's it. At this point, GoToBed already created your table and inserted your data.

##How to get your objects back from the database

##All you have to do is ask GoToBed to wake'em up. 

It can't get easier than this.

		List<Kid> kids = gtb.wake(Kid.class);
		
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
