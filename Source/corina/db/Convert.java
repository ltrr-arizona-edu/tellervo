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
import corina.formats.WrongFiletypeException;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

// load a bunch of data files, and stuff 'em in a RDBMS.  issues:
// -- ID numbers aren't always 6-digit ints: use strings (solves 0-prefix problem for free)

// this needs refactoring into more-usable and more-reusable classes:
// - convert a sample into SQL (SQLize.java, should be DB.java)
// - recursively dump all files into a database ("for each file in <folder>, do <operation>")

public class Convert {

    // dump all of |folder| into the database reachable by |connection|.
    // BUG: what if folder is really a file?
    public static void dump(File folder, Connection connection) {
	System.out.println("starting dump at " + new java.util.Date());

	try {
	    connection.setAutoCommit(false);
	} catch (SQLException se) {
	    System.out.println("error setting autocommit: " + se);
	}

	DB db = new DB(connection);
	process(folder.listFiles(), db);

	try {
	    connection.commit();
	} catch (SQLException se) {
	    System.out.println("error committing: " + se);
	}

	try {
	    connection.setAutoCommit(true);
	} catch (SQLException se) {
	    System.out.println("error setting autocommit: " + se);
	}

	try {
	    connection.close(); // ???
	} catch (SQLException se) {
	    System.out.println("error closing db: " + se);
	}

	System.out.println("finished with dump at " + new java.util.Date());
    }

    // speed: for all of ZKB: 1303ms loading, 13711ms dumping.  (ouch.)
    // obsolete! -- these numbers are for no prepared statements, and auto-commit on turned on.

    // REFACTOR: this "do for each file in <folder>" i see all the time.
    // can't i extract-method that, and just pass it a folder and a closure?
    private static void process(File args[], DB db) {
	for (int i=0; i<args.length; i++) {

	    // check for dir
	    if (args[i].isDirectory()) {
		process(args[i].listFiles(), db);
		continue;
	    }

	    try {
		// dump it in the database
		Sample s = new Sample(args[i].getPath());
		db.save(s);
	    } catch (WrongFiletypeException wfte) {
		// System.out.println("NOTE: ignoring " + args[i]);
	    } catch (IOException ioe) {
		System.out.println("FAIL (io) on " + args[i]); // skip it
	    } catch (SQLException se) {
		System.out.println("FAIL (sql=" + se + ") on " + args[i]); // skip it
	    }
	}
    }
}
