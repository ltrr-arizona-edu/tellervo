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

import corina.Sample;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// load a bunch of data files, and stuff 'em in a RDBMS.  issues:
// -- use of the postgres driver is hardcoded
// -- my hostname and database name are hardcoded
// -- my username and a (dummy) password are hardcoded
// -- ID numbers aren't always 6-digit ints: use strings (solves 0-prefix problem for free)

// this needs refactoring into more-usable and more-reusable classes:
// - convert a sample into SQL
// - recursively dump all files into a database
// - front-end: connect to database, dump data, select from data, etc.

public class Convert {

    private static Connection con;

    // args is a dir
    public static void main(String args[]) throws ClassNotFoundException, SQLException {
	// start up JDBC
	Class.forName("org.postgresql.Driver");
	con = DriverManager.getConnection("jdbc:postgresql://picea.arts.cornell.edu/dendro",
					  "kharris", "merhaba");

	// load some files
	File x[] = new File[args.length];
	for (int i=0; i<args.length; i++)
	    x[i] = new File(args[i]);

	// process those files
	process(x);

	// shut down JDBC
	con.close();
    }

    // for all of ZKB: 1303ms loading, 13711ms dumping.  crap.

    private static void process(File args[]) {
	for (int i=0; i<args.length; i++) {

	    // check for dir
	    if (args[i].isDirectory()) {
		process(args[i].listFiles());
		continue;
	    }

	    try {
		// dump it in the database
		Sample s = new Sample(args[i].getPath());
		SQLize ize = new SQLize(s, con);
		ize.run();
	    } catch (IOException ioe) {
		System.out.println("FAIL (io) on " + args[i]); // skip it
	    } catch (SQLException se) {
		System.out.println("FAIL (sql=" + se + ") on " + args[i]); // skip it
	    }
	}
    }
}
