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

import corina.Year;
import corina.Sample;
import corina.Element;
import corina.Metadata;
import corina.Metadata.Field;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.sql.SQLException;

import java.io.File;

import java.util.List;

/**
   Converts a Sample into a series of SQL statements.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class SQLize {

    private Connection con;

    private Sample s;

    private int sid;

    // set up a file-to-sql converter, including loading the file.
    // run() will use the given connection (with prepared statements)
    // to dump it into the database.
    public SQLize(Sample s, Connection con) {
	this.con = con;
	this.s = s;

	sid = s.meta.get("filename").hashCode();
	// or if you prefer strings: sid = Integer.toHexString(filename.hashCode());
    }

    // WRITEME: construct Sample from sid (presumably, you'll get the sid by searching in the browser)
    // WRITEME: update a Sample in the DB (in one transaction, insert/remove as necessary?)
    // (BEGIN TRANSACTION; <do stuff>; COMMIT;)

    // dump this sample into the database.
    public void run() throws SQLException {
	// begin transaction
	{
	    Statement stmt = con.createStatement();
	    stmt.execute("BEGIN TRANSACTION;");
	}

	// data
	insertData(s.data, "data");
	if (s.isSummed())
	    insertData(s.count, "count");

	// metadata (including range)
	insertMeta();

	// wj (optional)
	if (s.hasWeiserjahre()) {
	    insertData(s.incr, "incr");
	    insertData(s.decr, "decr");
	}

	// elements (optional)
	if (s.elements != null)
	    insertElements();

	// commit transaction
	{
	    Statement stmt = con.createStatement();
	    stmt.execute("COMMIT;");
	}
    }

    private void insertElements() throws SQLException {
	// insert each element, as its filename (for now)
	for (int i=0; i<s.elements.size(); i++) {
	    PreparedStatement stmt = con.prepareStatement("INSERT INTO elements VALUES (?, ?)");
	    stmt.setInt(1, sid);
	    stmt.setString(2, ((Element) s.elements.get(i)).getFilename());
	    stmt.executeUpdate();
	    stmt.close();
	}
    }

    private void insertMeta() throws SQLException {
	// get site name/code (actually "directory i'm in")
	File dir = new File((String) s.meta.get("filename"));
	String site = dir.getParentFile().getName();

	// build template, by counting number of meta fields
	StringBuffer line = new StringBuffer("INSERT INTO meta VALUES (?, ?");
	line.append(", ?, ?, ?"); // start, end, span
	for (int i=0; i<Metadata.fields.length; i++)
	    line.append(", ?");
	line.append(")");

	PreparedStatement stmt = con.prepareStatement(line.toString());

	stmt.setInt(1, sid);

	// this may look ugly, but it is correct.  see Year.java:intValue() for why.
	stmt.setInt(2, Integer.parseInt(s.range.getStart().toString()));
	stmt.setInt(3, Integer.parseInt(s.range.getEnd().toString()));
	stmt.setInt(4, s.range.span());

	stmt.setString(5, site);

	for (int i=0; i<Metadata.fields.length; i++) {
	    Object v = s.meta.get(Metadata.fields[i].variable);

	    if (v instanceof Integer)
		stmt.setInt(6+i, ((Integer) v).intValue());
	    else if (v == null)
		stmt.setNull(6+i, Types.INTEGER); // setNull wants a type?  evil!
	    else
		stmt.setString(6+i, (String) v);
	}

	stmt.executeUpdate();
	stmt.close();
    }

    // insert any List as decadal data, into a table with the given name.
    private void insertData(List data, String name) throws SQLException {
	// if there's no data, there's no reason to do anything.
	// (plus we hit a bug below if we actually try.)
	if (data.size() == 0)
	    return;

	// set up prepared statement; every line starts with sid
	PreparedStatement stmt =
	    con.prepareStatement("INSERT INTO " + name + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	stmt.setInt(1, sid);

	// starting sample: fill nulls
	for (int i=0; i<s.range.getStart().column(); i++)
	    stmt.setNull(i+3, Types.INTEGER);

	// "INSERT INTO data VALUES (%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d);";
	for (Year y=s.range.getStart(); y.compareTo(s.range.getEnd())<=0; y=y.add(+1)) {

	    // new line: second field is always year (decade)
	    if (s.range.startOfRow(y))
		stmt.setInt(2, Integer.parseInt(y.toString()));

	    // if NOT first line, add NULL for year zero
	    if (y.isYearOne() && !s.range.getStart().equals(y))
		stmt.setNull(3, Types.INTEGER);

	    // add datum
	    int index = y.diff(s.range.getStart());
	    stmt.setInt(y.column() + 3, ((Integer) data.get(index)).intValue());

	    // end of line?  send it in.
	    if (y.column()==9)
		stmt.executeUpdate();
	}

	// finish sample: fill nulls
	if (s.range.getEnd().column() != 9) {
	    for (int i=s.range.getEnd().column()+1; i<10; i++)
		stmt.setNull(i+3, Types.INTEGER);
	    stmt.executeUpdate();
	}

	// BUG: i shouldn't execute the updates decade-at-a-time.  that's even WORSE
	// than using a filesystem.  use BEGIN TRANSACTION / COMMIT -- around an entire
	// sample.

	// close statement
	stmt.close();
    }
}
