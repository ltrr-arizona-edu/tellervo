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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package corina.db;

import corina.Year;
import corina.Range;
import corina.Sample;
import corina.Element;
import corina.MetadataTemplate;
import corina.MetadataTemplate.Field;
import corina.formats.WrongFiletypeException;
import corina.logging.CorinaLog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.File;
import java.io.IOException;

import java.util.Map;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
   Store Samples in an RDBMS.

  <h2>Left to do</h2>
  <ul>

     <li>don't use my own order in Schema.build(), but ask
     MetadataTemplate.

     <li>the elements table sid values don't seem to match up (verify
     this)

     <li>getting the sid from the filename isn't reliable, unless you
     use the canonical pathnames

     <li>document me!

     <li>names are bad: instead of load/save (which seem ambiguous),
     use more database-like names, like insert(Sample),
     delete(Sample?), etc.

     <li>if you try to save a sample which is already there, it throws
     some random exception (java.sql.SQLException, somewhere)

     <li>load() makes new PreparedStatements each run; to gain any
     benefit from this, they should be lazily created and re-used

     <li>need a way to update samples in-place (update(Sample)?)

     <li>non-int values cause problem: sapwood "10?" can't be put in
     an INT slot; solution: add value "10", and add "Sapwood = '10?'"
     to comments field

     <li>exception handling is poor-to-nonexistant

     <li>extract all SQL queries to either string constants, or .sql
     files

     <li>make all queries lazily-created PreparedStatements

     <li>move higher-level stuff, like loading elements automatically,
     into a different class

     <li>add support for sites, in a different class

     <li>add support for "history" table
     
<pre>
   document all performance specs.

   also, document db compatibility (should work with all, but note testing)
   -- do this in package.html!

   also, this requires a Connection, right?  how do i get that?  doc!
   -- Schema does, too -- do this in package.html!

   also, can i take URL's yet, like
         "postgresql://picea.arts.cornell.edu/dendro?sid=1344012846"
   
   also, what about security?  how do you do passwords?

   also, what is an "sid"?  how do i get one?

   rename Schema, DB to DendroDBSchema, DendroDB(Samples? / Sites??)
</pre>

  </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class DB {
  private static final CorinaLog log = new CorinaLog(DB.class);

    private Connection connection;

    /**
       Make a new DB object on an open connection.  This DB object can
       then be used to access the Corina database on this connection.

       @param connection the connection to use
    */
    public DB(Connection connection) {
	this.connection = connection;
    }

    // -- load() performance: takes 275ms to load data of
    // "blc7000.new" (100KB) from a file; takes <500ms to load same
    // data from postgresql.  i think users will be able to live with
    // 1/5sec more delay for loading a 7000-year master chronology, if
    // it means they can do full-library-searches in 5sec instead of
    // 30min.

    /**
       Load a sample from the database.  This operation is atomic.

       @param sid the sid of the sample to load
       @return the Sample
       @exception SQLException if an SQL exception occurs
    */
    public Sample load(int sid) throws SQLException {
	// DESIGN: have load take a url?
	// e.g., "postgresql://picea.arts.cornell.edu/dendro?sid=1344012846"
	Sample sample = new Sample();

	// load data
	sample.data = loadData(sid, 'W');

	// load dens, incr, decr
	sample.count = loadData(sid, 'C');
	sample.incr = loadData(sid, 'I');
	sample.decr = loadData(sid, 'D');

	// load meta -- including range
	{
    PreparedStatement stmt =
      connection.prepareStatement("SELECT * FROM metadata " +
				  "WHERE sid = ?;");
    try {
	    stmt.setInt(1, sid);
	    ResultSet meta = stmt.executeQuery();
	    meta.next(); // move to first (only) row

	    // read range.
	    sample.range = readRange(meta);

	    // read other meta fields.
	    sample.meta = readMeta(meta);

	    // DESIGN: what about "site" field?
    } finally {
      try {
        stmt.close();
      } catch (SQLException sqle) {
        log.error("Error closing prepared statement", sqle);
      }
    }
	}

	// load elements
	{
    PreparedStatement stmt =
      connection.prepareStatement("SELECT * FROM elements " +
				  "WHERE sid = ?;");
    try {
	    stmt.setInt(1, sid);
	    ResultSet elements = stmt.executeQuery();

	    sample.elements = new ArrayList();

	    while (elements.next()) {
    		Element e = new Element(elements.getString("el"));
    		sample.elements.add(e);
	    }

	    if (sample.elements.size() == 0)
	      sample.elements = null;
    } finally {
      try {
        stmt.close();
      } catch (SQLException sqle) {
        log.error("Error closing prepared statement", sqle);
      }
    }
	}
	// DESIGN: would elements be in the database as jdbc urls?

	return sample;
    }

    // extract a Range from the current row of this resultset (which
    // points to a meta row).
    private Range readRange(ResultSet meta) throws SQLException {
	Year start = new Year(meta.getInt("start"));
	Year end = new Year(meta.getInt("stop"));
	return new Range(start, end);
    }

    // extract all normal metadata from the current row of this
    // resultset (which points to a meta row).
    private Map readMeta(ResultSet meta) throws SQLException {
	Map map = new Hashtable();

	Iterator i = MetadataTemplate.getFields();
	while (i.hasNext()) {
	    MetadataTemplate.Field f = (MetadataTemplate.Field) i.next();
	    Object x = meta.getObject(f.getVariable());
	    // since i don't want to worry about types right now
	    // PERFORMANCE: but getObject() is slower than get<Type>().
	    if (x != null)
		map.put(f.getVariable(), x);
	}

	return map;
    }

    // given an sid and a connection and a type ('W', 'C', 'I', 'D'),
    // load a list of numbers.  if there's no data for this sid/type,
    // return null.
    private PreparedStatement dataStmt = null;
    private List loadData(int sid, char type) throws SQLException {
	if (dataStmt == null)
	    dataStmt = connection.prepareStatement("SELECT * FROM data " +
						   "WHERE sid = ? AND type = ? " +
						   "GROUP BY sid, decade, type, " +
						   "d0, d1, d2, d3, d4, " +
						   "d5, d6, d7, d8, d9;");
	dataStmt.setInt(1, sid);
	dataStmt.setString(2, String.valueOf(type));

	ResultSet data = dataStmt.executeQuery();

	List list = new ArrayList();
	while (data.next()) {
	    for (int i=0; i<10; i++) {
		int col = i + 4; // skip (sid, type, decade) columns
		int x = data.getInt(col);
		if (!data.wasNull()) {
		    list.add(new Integer(x));
		}
	    }
	}

	// if there's none of this type of data (no count, for
	// example), return null.
	if (list.size() == 0)
	    return null;

	return list;
    }

    /**
       Delete a sample from the database.  This operation is atomic.

       @param sid the sid of the sample to delete
       @exception SQLException if an SQL exception occurs
    */
    public void delete(int sid) throws SQLException {
	// do this as one atomic operation
	connection.setAutoCommit(false);

	// remove data (and count, incr, decr)
	if (deleteData == null)
	    deleteData = connection.prepareStatement("DELETE data " +
						     "WHERE sid = ?;");
	deleteData.setInt(1, sid);
	deleteData.executeUpdate();

	// remove metadata
	if (deleteMeta == null)
	    deleteMeta = connection.prepareStatement("DELETE metadata " +
						     "WHERE sid = ?;");
	deleteMeta.setInt(1, sid);
	deleteMeta.executeUpdate();

	// remove elements
	if (deleteEl == null)
	    deleteEl = connection.prepareStatement("DELETE elements " +
						   "WHERE sid = ?;");
	deleteEl.setInt(1, sid);
	deleteEl.executeUpdate();

	// perform deletes
	connection.commit();
	connection.setAutoCommit(true);
    }

    private PreparedStatement deleteData = null;
    private PreparedStatement deleteMeta = null;
    private PreparedStatement deleteEl = null;

    // WRITEME: update a Sample in the DB (in one transaction,
    // insert/remove as necessary?)  -- well, sort of: update =
    // (delete, insert) in one transaction.  but it's a useful
    // abstraction.

    /**
       Save a sample into the database.  This operation is atomic.

       @param sample the sample to save
       @exception SQLException if an SQL exception occurs
    */
    public void save(Sample sample) throws SQLException {
	connection.setAutoCommit(false);

	saveSample(sample);

	connection.commit();
	connection.setAutoCommit(true);
    }

    // dump this sample into the database.
    private void saveSample(Sample s) throws SQLException {
	// compute sid
	int sid = s.meta.get("filename").hashCode();
	// or if you prefer strings:
	// sid = Integer.toHexString(filename.hashCode());

	// metadata (including range)
	insertMeta(sid, s);

	// data
	insertData(sid, s, s.data, 'W');
	if (s.isSummed())
	    insertData(sid, s, s.count, 'C');

	// wj (optional)
	if (s.hasWeiserjahre()) {
	    insertData(sid, s, s.incr, 'I');
	    insertData(sid, s, s.decr, 'D');
	}

	// elements (optional)
	if (s.elements != null)
	    insertElements(sid, s);
    }

    /**
       Import all files from a folder into the database.  All files in
       sub-folders will be imported, as well.  Non-dendro files are
       ignored.  The entire import is atomic.

       @param folder the folder to import
       @exception SQLException if there is an SQL exception while
       importing data
    */
    public void dump(String folder) throws SQLException {
	// connection.setAutoCommit(false);

	File f = new File(folder);

	if (f.isDirectory()) {
	    importFolder(f);
	} else {
	    importFile(f);
	}

	// connection.commit();
	// connection.setAutoCommit(true);
    }

    // import a folder
    private void importFolder(File folder) throws SQLException {
	if (folder.isHidden())
	    return;

	File files[] = folder.listFiles();
	for (int i=0; i<files.length; i++) {
	    if (files[i].isDirectory())
		importFolder(files[i]);
	    else
		importFile(files[i]);
	}
    }

    // import a file
    private void importFile(File file) throws SQLException {
	if (file.isHidden())
	    return;

	connection.setAutoCommit(false);

	try {
	    Sample s = new Sample(file.getPath());
	    saveSample(s);
	} catch (WrongFiletypeException wfte) {
	    // ignore non-dendro files
	} catch (IOException ioe) {
	    // -- WHAT TO DO?
	} finally {
	    connection.commit();
	    connection.setAutoCommit(true);
	}
    }

    private PreparedStatement elStmt = null;
    private void insertElements(int sid, Sample s) throws SQLException {
	// create, if necessary
	if (elStmt == null)
	    elStmt = connection.prepareStatement("INSERT INTO elements " +
						 "VALUES (?, ?);");

	// insert each element, as its filename (for now)
	for (int i=0; i<s.elements.size(); i++) {
	    elStmt.setInt(1, sid);

	    // REFACTOR: need a makeSID() method!
	    int esid = ((Element) s.elements.get(i)).getFilename().hashCode();
	    elStmt.setInt(2, esid);

	    elStmt.executeUpdate();
	}
    }

    private PreparedStatement metaStmt = null;

    private void insertMeta(int sid, Sample s) throws SQLException {
	// get site name/code (actually "directory i'm in")
	File dir = new File((String) s.meta.get("filename"));
	String site = dir.getParentFile().getName();

	if (metaStmt == null) {
	    // build template, by counting number of meta fields
	    StringBuffer line = new StringBuffer("INSERT INTO metadata " +
						 "VALUES (?, ?");
	    line.append(", ?, ?, ?"); // start, end, span
	    Iterator i = MetadataTemplate.getFields();
	    while (i.hasNext()) {
		line.append(", ?");
		i.next(); // (and ignore value)
	    }
	    line.append(");");

	    metaStmt = connection.prepareStatement(line.toString());
	}

	metaStmt.setInt(1, sid);

	// this may look ugly, but it is correct.  (see
	// Year.intValue() for details.)
	metaStmt.setInt(2, Integer.parseInt(s.range.getStart().toString()));
	metaStmt.setInt(3, Integer.parseInt(s.range.getEnd().toString()));
	metaStmt.setInt(4, s.range.span());

	metaStmt.setString(5, site);

	int n = 0;
	Iterator i = MetadataTemplate.getFields();
	List extraComments = new ArrayList();
	while (i.hasNext()) {
	    String variable = ((Field) i.next()).getVariable();
	    n++;
	    Object v = s.meta.get(variable);

	    // ASSUME: metadata have only Numbers, Strings, and nulls.

	    /*
	      PROBLEM: at this point, there are 2 sets of types: the
	      types that s.meta has, and the types that metaStmt
	      expects (which I can query with metaStmt.getMetaData() -
	      getColumnCount(), getColumnType(), getColumnName(), ...).

	      SOLUTION: MetadataTemplate needs to hold types for each
	      value, which will be used for schema creation, and also
	      for here.  the only problem remaining will be old files
	      that have bad values

	      TEMPORARY: if you find a non-number ("10?") in
	      var="sapwood", make it into "10", and add the line
	      "Sapwood: '10?'" to var="comments".

	      ALSO: need to verify one-of types.  if {"+","++"} is
	      expected, and "++?" is found, we need to mangle that,
	      too.

	      ALSO: ?'s aren't getting set to NULL in the corina
	      loader, so they're getting put in the database as '?' 
	      chars.  bad bad.

	      ALSO: sometimes CHAR-sized fields have had strings put
	      in them, like pith="Near".  what to do?  check the first
	      char -- if it's a valid char, use that, else use NULL;
	      either way, put the whole "var=value" in the comments,
	      as well.
	    */

	    // TEMPORARY hack:
	    if ((variable.equals("sapwood") ||
		 variable.equals("index_type") ||
		 variable.equals("unmeas_pre") ||
		 variable.equals("unmeas_post")) && !(v instanceof Number)) {
		try {
		    v = new Integer((String) v);
		    // got an int?  ok!
		} catch (NumberFormatException nfe) {
		    // no int?  ouch!
		    // temporary temporary hack hack: null.
		    extraComments.add(/*i18n:*/variable + ": \"" + v + "\"");
		    v = null;
		}
	    }
	    if ((variable.equals("pith") ||
		 variable.equals("dating") ||
		 variable.equals("reconciled") ||
		 variable.equals("continuous") ||
		 variable.equals("format") ||
		 variable.equals("type")) &&
		(v!=null && v.toString().length() > 1)) {
		extraComments.add(/*i18n:*/variable + ": \"" + v + "\"");
		v = v.toString().substring(0, 1);
	    }

	    if (v instanceof Number)
		metaStmt.setInt(5+n, ((Number) v).intValue());
	    else if (v == null)
		metaStmt.setNull(5+n, Types.INTEGER);
	        // setNull wants a type?  evil!  dumb!
	    else {
		String str = (String) v;
		// System.out.println("str='" + str + "'");
		// TODO: escape things like ' too?

		// add temp data
		if (variable.equals("comments"))
		    for (int ii=0; ii<extraComments.size(); ii++)
			str += "\n" + extraComments.get(ii);

		// BUG: this is weird!
		str = corina.util.StringUtils.substitute(str, "\n", "n");
		str = corina.util.StringUtils.substitute(str, "\r", "r");

		metaStmt.setString(5+n, str);
	    }
	    // ROBUSTNESS: am i positive it's a String?
	}

	try {
	    metaStmt.executeUpdate();
	} catch (SQLException e) {
	    // can't update |filename|
	    System.out.println("sql/e=" + e);
	    e.printStackTrace();
	    throw e;
	} catch (Exception e) {
	    System.out.println("e=" + e);
	    e.printStackTrace();
	}
    }

    // insert any List as decadal data, into a table with the given name.
    private PreparedStatement dataInsertStmt = null;
    private void insertData(int sid, Sample s,
			    List data, char type) throws SQLException {
	// if there's no data, there's no reason to do anything.
	// (plus we hit a bug below if we actually try.)
	if (data.size() == 0)
	    return;

	// set up prepared statement; every line starts with sid
	if (dataInsertStmt == null) {
	    dataInsertStmt = connection.prepareStatement("INSERT INTO data " +
							 "VALUES " +
							 "(?, ?, ?, " +
							 "?, ?, ?, ?, ?, " +
							 "?, ?, ?, ?, ?)");
	}

	dataInsertStmt.setInt(1, sid);

	dataInsertStmt.setString(2, String.valueOf(type));

	// starting sample: fill nulls
	for (int i=0; i<s.range.getStart().column(); i++)
	    dataInsertStmt.setNull(i+4, Types.INTEGER);

	// "INSERT INTO data VALUES (%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d);"
	Range r = s.range;
	for (Year y=r.getStart(); y.compareTo(r.getEnd())<=0; y=y.add(+1)) {

	    // new line: second field is always year (decade)
	    if (r.startOfRow(y))
		dataInsertStmt.setInt(3, Integer.parseInt(y.toString()));

	    // if NOT first line, add NULL for year zero
	    if (y.isYearOne() && !r.getStart().equals(y))
		dataInsertStmt.setNull(4, Types.INTEGER);

	    // add datum
	    int index = y.diff(r.getStart());
	    dataInsertStmt.setInt(y.column() + 4,
				  ((Integer) data.get(index)).intValue());

	    // end of line?  send it in.
	    try {
	    if (y.column()==9)
		dataInsertStmt.executeUpdate();
	    } catch (Exception e) {
		System.out.println("e=" + e);
		System.out.println("s=" + s);
		System.out.println("fn=" + s.meta.get("filename"));
	    }
	}

	// finish sample: fill nulls
	if (r.getEnd().column() != 9) {
	    for (int i=r.getEnd().column()+1; i<10; i++)
		dataInsertStmt.setNull(i+4, Types.INTEGER);
	    dataInsertStmt.executeUpdate();
	}
    }

    // ======================================================================
    // higher-level stuff, formerly in DBBrowser.java
    //

    // return Elements (almost -- no load ability)
    public List getElements(String site) throws SQLException {
	// send the SELECT to the database
	if (getElStmt == null) {
	    getElStmt = connection.prepareStatement("SELECT * " +
						    "FROM meta " +
						    "WHERE UPPER(site) = ?;");
	}
	getElStmt.setString(1, site.toUpperCase());
	ResultSet rs = getElStmt.executeQuery();

	// need to sort?  no, display component will sort however the
	// user wants.

	// BUG: need to GROUP BY to prevent duplicates?

	// from results, put each element in a list.
	List list = new ArrayList();
	while (rs.next()) {;
	    Element e = new Element(""); // need some filename...

	    // read range
	    e.setRange(readRange(rs));

	    // read other fields
	    e.details = readMeta(rs);

	    // DESIGN: add stamp to corner of database icon to mean
	    // "connected"!

	    // DESIGN: allow users to run arbitrary SQL statements on
	    // a connected database

	    list.add(e);
	    rs.next();
	}
	return list;
    }
    private PreparedStatement getElStmt = null;

    // return Elements (almost -- no load ability).
    // search for string in title, comments.  (FIXME: search all fields)
    // REFACTOR: this is almost identical to getElements(site), except for the statement.
    public List getElements(String site, String search) throws SQLException {
	// send the SELECT to the database
	if (getElemSearchStmt == null) {
	    getElemSearchStmt = connection.prepareStatement("SELECT * FROM meta " +
							  "WHERE UPPER(site) = ? AND " +
							  "(UPPER(title) LIKE ? OR " +
							  "UPPER(comments) LIKE ?);");
	}
	getElemSearchStmt.setString(1, site.toUpperCase());
	getElemSearchStmt.setString(2, "'%" + search.toUpperCase() + "%'");
	getElemSearchStmt.setString(3, "'%" + search.toUpperCase() + "%'");
	ResultSet rs = getElemSearchStmt.executeQuery();

	// need to sort?  no, display component will sort however the user wants.
	// BUG: need to GROUP BY to prevent duplicates?

	// from results, put each element in a list.
	List list = new ArrayList();
	while (rs.next()) {;
	    Element e = new Element(""); // need some filename...

	    // read range
	    e.setRange(readRange(rs));

	    // read other fields -- FIXME: use addAll() instead
	    e.details = readMeta(rs);

	    // DESIGN: add stamp to corner of database icon to mean
	    // "connected"!

	    // DESIGN: allow users to run arbitrary SQL statements on
	    // a connected database

	    list.add(e);
	    rs.next();
	}
	return list;
    }
    private PreparedStatement getElemSearchStmt = null;
}
