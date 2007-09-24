package edu.cornell.dendro.corina.db;

import java.sql.*;

/**
   Schema tools for Corina databases.

   This class is only responsible for creating, verifying, and
   destroying Corina databases.  All of the data access, like how to
   put a Sample into the database, is in the class DB.

   <h2>Database Schema</h2>

   <p>Samples are stored in the RDBMS in 3 tables: data, metadata, and
   elements.  Range is stored as metadata fields.  Non-data numeric
   data (count, incr, decr) are stored in the data table, with a
   different type code.</p>

   <blockquote>In the future, there will be 2 more tables to round it
   out: sites, and history.  The sites table will hold all of the
   sites: site ID, country, 3-letter code, 3-digit code, full name,
   species present (use more tables for multiple species?), type
   (ancient, forest, etc.), and location (latitude/longitude).  The
   history table will hold a list of all operations performed on the
   table: history ID, time/date, user, sample ID, and operation.  For
   example, [4893782, 7 June 2003 2:02pm, Ken Harris, 123981, Inserted
   MR at 1002].  Initially, this will be used purely as a log of
   who-did-what.  (Imagine asking for "all of the samples that Joe
   reconciled", or "everything Joe did last week".  -- I might need to
   standardize the "operation type" column a bit more, or add another
   column.)  Later, it may be used to selectively undo certain
   operations.</blockquote>

   <p>Corina databases can also store Sites.  These are stored in a
   table called "sites".</p>

   <p>Corina doesn't create any explicit indexes.  Corina creates
   PRIMARY KEYs as needed, so no indexes are needed.  (sid on metadata
   and elements, and (sid,type,decade) on data.)</p>

   <h2>Data</h2>

   <p>The table is called "data".  It holds all decadal numeric data
   for samples.  The data is split up into decades, and each decade is
   stored in one row of the table.  If there's more than one type of
   decadal data (like ring width, number of samples,
   number-increasing, and number-decreasing), there are multiple rows
   for that decade, with different "type" values.</p>

   <table align="center" border="1" cellspacing="0">
     <tr><th>Field</th>  <th>Type</th> <th>Description</th></tr>
     <tr><td>sid</td>    <td>INT</td>  <td>sample ID</td></tr>
     <tr><td>decade</td> <td>INT</td>  <td>which decade this is</td></tr>
     <tr><td>type</td>   <td>CHAR</td> <td>type of decadal data</td></tr>
     <tr><td>d0</td> <td>INT</td> <td>value for year 1 of this decade</td></tr>
     <tr><td>d1</td> <td>INT</td> <td>value for year 2 of this decade</td></tr>
     <tr><td>d2</td> <td>INT</td> <td>value for year 3 of this decade</td></tr>
     <tr><td>d3</td> <td>INT</td> <td>value for year 4 of this decade</td></tr>
     <tr><td>d4</td> <td>INT</td> <td>value for year 5 of this decade</td></tr>
     <tr><td>d5</td> <td>INT</td> <td>value for year 6 of this decade</td></tr>
     <tr><td>d6</td> <td>INT</td> <td>value for year 7 of this decade</td></tr>
     <tr><td>d7</td> <td>INT</td> <td>value for year 8 of this decade</td></tr>
     <tr><td>d8</td> <td>INT</td> <td>value for year 9 of this decade</td></tr>
     <tr><td>d9</td> <td>INT</td> <td>value for year 10 of this decade</td></tr>
   </table>

   <p>The types of data are 'W' (data - "width"), 'C' (count), 'I'
   (increments), and 'D' (decrements).</p>

   <p>If there's no value for a year, that field's value is NULL.
   This happens at the beginning of a row if the sample doesn't start
   on a decade, at the end of a row if the sample doesn't end on a
   decade, and at d0 for the year "zero".</p>

   <h2>Metadata</h2>

	  -- "end" is reserved in SQL, so we use "start" and "stop"
	  for the range.)
	
	  -- why is "id" not UNIQUE?  because for cornell's data, the
	  ID field never was unique.

	  -- on some databases, VARCHAR (used for comments) size is
	  rather small.  oracle: 4000; ms sql server: you don't even
	  want to know...  postgresql: rumor mill says at least
	  100-200K (!).  mysql: 255.

   <table align="center" border="1" cellspacing="0">
     <tr><th>Field</th>       <th>Type</th>    <th>Description</th></tr>

     <tr><td>sid</td>         <td>INT</td>     <td>sample ID</td></tr>

     <tr><td>start</td>       <td>INT</td>     <td rowspan="3">range</td></tr>
     <tr><td>stop</td>        <td>INT</td>     </tr>
     <tr><td>span</td>        <td>INT</td>     </tr>

     <tr><td>site</td>        <td>VARCHAR</td> <td>site</td></tr>

     <tr><td>id</td>          <td>VARCHAR</td> <td rowspan="17">metadata</td></tr>
     <tr><td>title</td>       <td>VARCHAR</td> </tr>
     <tr><td>dating</td>      <td>CHAR</td>    </tr>
     <tr><td>unmeas_pre</td>  <td>INT</td>     </tr>
     <tr><td>unmeas_post</td> <td>INT</td>     </tr>
     <tr><td>type</td>        <td>CHAR</td>    </tr>
     <tr><td>species</td>     <td>VARCHAR</td> </tr>
     <tr><td>format</td>      <td>CHAR</td>    </tr>
     <tr><td>index_type</td>  <td>INT</td>     </tr>
     <tr><td>sapwood</td>     <td>INT</td>     </tr>
     <tr><td>pith</td>        <td>CHAR</td>    </tr>
     <tr><td>terminal</td>    <td>VARCHAR</td> </tr>
     <tr><td>continuous</td>  <td>CHAR</td>    </tr>
     <tr><td>quality</td>     <td>VARCHAR</td> </tr>
     <tr><td>reconciled</td>  <td>CHAR</td>    </tr>
     <tr><td>author</td>      <td>VARCHAR</td> </tr>
     <tr><td>comments</td>    <td>VARCHAR</td> </tr>
   </table>
</pre>

   <h2>Elements</h2>

   <p>The elements are stored in a table called "elements".  For a sum
   with 10 elements, there are 10 rows in this table; each row
   contains the sample ID of the master, and the sample ID of one
   element.</p>

   <table align="center" border="1" cellspacing="0">
     <tr><th>Field</th> <th>Type</th> <th>Description</th></tr>
     <tr><td>sid</td>   <td>INT</td>  <td>sample ID</td></tr>
     <tr><td>esid</td>  <td>INT</td>  <td>element that is contained in
                                          thismaster</td></tr>
   </table>
</pre>

   <h2>Sites</h2>

   -- sites table

   <table align="center" border="1" cellspacing="0">
     <tr><th>Field</th>    <th>Type</th> <th>Description</th></tr>
     <tr><td>id</td>       <td>INT PRIMARY KEY(??),
     <tr><td>country</td>  <td>CHAR(2)</td>
     <tr><td>code</td>     <td>CHAR(3)</td>
     <tr><td>name</td>     <td>VARCHAR</td>
     <tr><td>species</td>  <td>VARCHAR</td>
     <tr><td>type</td>     <td>VARCHAR (??)</td>
     <tr><td>location</td> <td>VARCHAR</td>
   </table>

   <h2>Left to do</h2>
   <ul>

      <li>i won't ever need to put 2 dendro databases in the same
      database (namespace), so it doesn't do any harm that the tables
      are called "data", etc., but "corina_data" would be more in-line
      with postgres using "pg_aggregate" for postgres-specific tables.
      Or should I be using schemas?

      <li>on that note, make "data", "elements", "metadata" table
      names constants.

      <li>create metadata table columns/types from MetadataTemplate
      object

      <li>(come up with a better way to store elements? -- need
      referential integrity!)

      <li>come up with a better way to store sites?

      <li>finish documentation

      <li>error handling: what if build() called, but schema already
      exists?  what if destroy() called, but no schema here?  (need
      verify()!)

      <li>need at least a "last-modified" field in the
      <code>metadata</code> table; a <code>history</code> table which
      held (sid, author, time/date, modification) tuples would be
      optimal

      <li>improve verify() to confirm columns and types

   </ul>

   @see corina.db.DB

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Schema {
    // don't instantiate me
    private Schema() {
    }

    /**
       Build a Corina schema database.

       @param connection the connection to create the database on
       @exception SQLException if an SQL exception occurs
    */
    public static void build(Connection connection) throws SQLException {
	// schema building should be atomic
	connection.setAutoCommit(false);

	Statement stmt = connection.createStatement();

	// add metadata table

	/*
	  notes:
	
	  -- "end" is reserved in SQL, so we use "stop" for the
	  end of the range.)
	
	  -- why is "id" not UNIQUE?  because for cornell's data, the
	  ID field never was unique.

	  -- on some databases, VARCHAR size is rather small.  oracle:
	  4000; ms sql server: you don't even want to know...
	  postgresql: rumor mill says at least 100-200K (!).
	*/

	// DESIGN: get names (and types) from Metadata!  how to get
	// types?  CHAR/VARCHAR is easy to find out, but i need my own
	// flag for INTs (unmeas_*, index_type, sapwood) -- it's a
	// good idea, anyway: we can prevent people from even typing
	// non-numbers, if fields have numeric/non-numeric types.

	// (this also causes BUGs, if the order here isn't exactly the
	// same as in MetadataTemplate.)

	stmt.executeUpdate("CREATE TABLE metadata (" +
			   "sid INT PRIMARY KEY," + // sample ID
			   "start INT NOT NULL," + // range
			   "stop INT NOT NULL," +
			   "span INT NOT NULL," +
			   "site VARCHAR," + // site -- TODO: make this a REF!
			   "title VARCHAR," + // metadata
			   "id VARCHAR," +
			   "dating CHAR," +
			   "unmeas_pre INT," +
			   "unmeas_post INT," +
			   "type CHAR," +
			   "species VARCHAR," +
			   "format CHAR," +
			   "index_type INT," +
			   "sapwood INT," +
			   "pith CHAR," +
			   "terminal VARCHAR," +
			   "continuous CHAR," +
			   "quality VARCHAR," +
			   "reconciled CHAR," +
			   "author VARCHAR," +
			   "comments VARCHAR);");

	// add data table, for data, count, incr, decr
	stmt.executeUpdate("CREATE TABLE data (" +
			   "sid INT REFERENCES metadata, " +
			   "type CHAR NOT NULL, " +
			   "decade INT NOT NULL, " +
			   "d0 INT, d1 INT, d2 INT, d3 INT, d4 INT," +
			   "d5 INT, d6 INT, d7 INT, d8 INT, d9 INT," +
			   "PRIMARY KEY (sid, type, decade)" +
			   ");");

	// add elements table
	stmt.executeUpdate("CREATE TABLE elements (" +
			   "sid INT NOT NULL, " +
			   "esid INT);"); //  REFERENCES metadata (sid));");
	// BUG: can't put in referential integrity check until i know
	// all elements exist, which may never be true.  how to deal
	// with this?

	stmt.close();

	connection.commit();
	connection.setAutoCommit(true); // how do we know this is the old value?
    }

    /**
       Destroy an existing Corina-schema database.

       @param connection the connection to find the database to destroy
       @exception SQLException if an SQL exception occurs
    */
    public static void destroy(Connection connection) throws SQLException {
	connection.setAutoCommit(false);

	// die tables die!  (it's german; it means "the, table, the".)
	Statement stmt = connection.createStatement();
	try {
	    stmt.executeUpdate("DROP TABLE data;");
	    stmt.executeUpdate("DROP TABLE elements;");
	    stmt.executeUpdate("DROP TABLE metadata;");
	    // (the api says any "statements that return nothing"
	    // are ok for executeUpdate())
	} catch (SQLException se) {
	    System.out.println("error dropping table: " + se);
	    // is this fatal?  a warning?  BUG: "no table by this
	    // name" seems to cause this -- so i'd better check
	    // for the table first, and skip them if they're not
	    // here.
	    throw se;
	}

	stmt.close();

	connection.commit();
	connection.setAutoCommit(true); // how do we know this is the old value?
    }

    /**
       Is there a valid Corina database here?

       @param connection the connection to check
       @return true, if there's a valid Corina database here, else
       false
       @exception SQLException if an SQL exception occurs
    */
    public static boolean verify(Connection connection) throws SQLException {
	// strategy: for now, let's just perform a couple SELECTs, and
	// make sure no exceptions are thrown.  it won't confirm all
	// the types, but it'll confirm the tables exist.

	try {
	    Statement verifyStmt = connection.createStatement();

	    verifyStmt.executeQuery("SELECT count(*) FROM metadata;");
	    verifyStmt.executeQuery("SELECT count(*) FROM data;");
	    verifyStmt.executeQuery("SELECT count(*) FROM elements;");

	    return true;
	} catch (SQLException se) {
	    return false;
	}
    }
}
