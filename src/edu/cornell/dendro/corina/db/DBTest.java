package corina.db;

import corina.Sample;

import java.sql.*;

public class DBTest {
    public static void main(String args[]) throws Exception {
	org.apache.log4j.BasicConfigurator.configure();

	// start up JDBC
	try {
	    Class.forName("org.postgresql.Driver");
	    // Class.forName("org.hsqldb.jdbcDriver");
	} catch (ClassNotFoundException cnfe) {
	    System.out.println("can't load jdbc driver: " + cnfe);
	    System.exit(77);
	}
	// DESIGN: can i get the JDBC driver from the URI?
	// Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/dendro",
	Connection con = DriverManager.getConnection("jdbc:postgresql:dendro",
						     "kharris", "a8cfkfre");
	// Connection con = DriverManager.getConnection("jdbc:hsqldb:/Users/kharris/dendro",
	// "sa", "");

	for (int i=0; i<args.length; i++) {
	    String arg = args[i];

	    if (arg.equals("build"))
		Schema.build(con);

	    if (arg.equals("destroy"))
		Schema.destroy(con);

	    if (arg.equals("verify")) {
		boolean v = Schema.verify(con);
		System.out.println(v);
	    }

	    if (arg.equals("info")) {
		DatabaseMetaData dbmd = con.getMetaData();

		String productName = dbmd.getDatabaseProductName();
		String productVersion = dbmd.getDatabaseProductVersion();
		String driverName = dbmd.getDriverName();
		String driverVersion = dbmd.getDriverVersion();

		System.out.println("Database: " + productName +
				   " (version: " + productVersion + ")");
		System.out.println("Driver: " + driverName +
				   " (version: " + driverVersion + ")");
	    }

	    if (arg.equals("count")) {
		PreparedStatement countStmt =
		    con.prepareStatement("SELECT COUNT(sid) FROM metadata;");
		ResultSet count = countStmt.executeQuery();
		count.next(); // advance to first row
		int n = count.getInt(1);

		System.out.println(n);
	    }

	    if (arg.equals("import")) {
		String folder = args[++i];

		DB db = new DB(con);
		db.dump(folder);
	    }

	    if (arg.equals("load")) {
		String file = args[++i];

		DB db = new DB(con);

		int sid = file.hashCode();

		Sample s = db.load(sid);
		System.out.println(s.toString());
	    }

	    // --> ADD NEW COMMANDS HERE
	}

	// shut down JDBC
	con.close();
    }
}
/*
  stuff to work on:
  -- this test class
  -- authorization -- how?  (user-level stuff, too)
  -- initial creation -- ?
  -- new, delete, rename?, open, etc. -- sids?
  -- remove filename from metadata -- it should have a DataSource member(??)
*/
