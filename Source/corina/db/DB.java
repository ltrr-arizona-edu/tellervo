//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.db;

/*
  overview:

  (dialog) Connect to Database:
  Database: [jdbc:postgresql://... ]
  Username: [        ]
  Password: [        ]

  (menuitem) Insert Files into Database:
  => (file chooser?)
  => (modeless dialog: "%d files inserted"

  (window) corina.db
  (top=metapanel + "Search"(ret), bottom=result table?)
  (modeline: "%d samples in database", update every 10s? trigger?)
*/

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

    // given a db connection, count the number of samples in it
    public static int countSamples(Connection c) throws SQLException {
	Statement stmt = c.createStatement();
	ResultSet ans = stmt.executeQuery("select count(sid) from ranges");
	ans.next(); // dunno why, but i need this...
	return ans.getInt("count");
    }

    // debugging: open the database, count the samples, and time it
    public static void main(String args[]) throws SQLException {
	// start up JDBC
	try {
	    Class.forName("org.postgresql.Driver");
	} catch (ClassNotFoundException cnfe) {
	    System.out.println("can't load postgres jdbc driver");
	    System.exit(77);
	}
	Connection con = DriverManager.getConnection("jdbc:postgresql://picea.arts.cornell.edu/dendro",
						     "kharris", "merhaba");

	// count samples, and time it
	long t1 = System.currentTimeMillis();
	int n = countSamples(con);
	long t2 = System.currentTimeMillis();

	// print it
	System.out.println("there are " + n + " samples, and it took me " + (t2-t1) + "ms to count them");

	// shut down JDBC
	con.close();
    }

}
