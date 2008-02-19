package edu.cornell.dendro.corina.search;

import edu.cornell.dendro.corina.Element;

import java.io.File;

import java.util.List;
import java.util.ArrayList;

public class Search implements Runnable {

    public static final int ANY = 0;
    public static final int ALL = 1;
    private int type = ANY;

    public void setType(int type) {
	if (type==ANY || type==ALL)
	    this.type = type;
	else
	    throw new IllegalArgumentException("type must be ANY or ALL (got: " + type + ")");
    }

    private List criteria;

    private File folder = new File(System.getProperty("user.dir"));
    public void setFolder(String filename) {
	this.folder = new File(filename);
    }

    public Search() {
	criteria = new ArrayList();
    }

    // add a criterion, sorted for performance
    public void addCriterion(Criterion c, boolean fast) {
	// sort them as they come in: if they depend on metadata, add
	// to end; if they're just filename/moddate, add to start

	// DESIGN: this changes the order they are in the list.  if the user wants to
	// look at or edit it again, they should be in the same order.   so instead,
	// i should just record "is it fast?" as a flag -- it should only matter when
	// running the search, not when printing, for example.

	if (fast)
	    criteria.add(0, c);
	else
	    criteria.add(c);
    }

    public void run() {
	matches = new ArrayList();

	System.out.println("searching folder " + folder);

	System.out.println("if i was querying a database, i might say:");
	System.out.println("   " + toSQL());
	// (WRITEME: actually do it...)
	System.out.println("if i was timing it, i might brag about how many files i'd searched, by:");
	System.out.println("   SELECT COUNT(*) FROM meta;");

	// relative performance:
	    // DB: 0.1sec for 990 records ~= 10,000 records/sec (or all 50,000 samples in ~5sec)
	    // FS: 1.6sec for 84 files ~= 50 files/sec (or 200 times slower, or all 50,000 samples in ~15min)

	System.out.println("if i was the tooltip for a data source, i might say:");
	System.out.println("   " + toString());

	long t1 = System.currentTimeMillis();
	search(folder);
	long t2 = System.currentTimeMillis();
	float time = ((t2-t1) / 1000f);
	System.out.println("(search took " + time + " sec, for " + count + " files and folders)");

	System.out.println("done ... " + matches.size() + " matches:");
	for (int i=0; i<matches.size(); i++) {
	    System.out.println("-- " + matches.get(i));
	}
    }

    // number of files searched
    private int count=0;

    private void search(File folder) {
	// System.out.println("-- searching folder " + folder);

	// list files
	File children[] = folder.listFiles();

	// update count
	count += children.length; // add now, or after?  best: as-i-go.

	// test each file in this folder
	for (int i=0; i<children.length; i++) {
	    if (children[i].isDirectory()) {
		search(children[i]);
	    } else {
		testFile(children[i]);
	    }
	}
    }

    // TODO: add keyboard shortcuts for +/- (accel +, accel -?)

    // try all criteria.
    // remember: ANY = stop(pass) on true, ALL = stop(fail) on false.
    private void testFile(File f) {
	// test element |e|
	Element e = new Element(f.getPath());
	testElement(e);
    }

    // test an element, and add it to |matches| if it meets all necessary criteria.
    // note: e.isSample() calls slow it down noticably, but not horribly, if the
    // result set is small.
    private void testElement(Element e) {
	for (int i=0; i<criteria.size(); i++) {
	    Criterion c = (Criterion) criteria.get(i);
	    boolean test = c.test(e);

	    // ANY: if one is true, done
	    if ((type==ANY) && (test==true)) {
		if (e.isSample())
		    matches.add(e.filename);
		return;
	    }

	    // ALL: if one is false, done
	    if ((type==ALL) && (test==false)) {
		return;
	    }
	}

	// when done: if ALL, add it
	if (type == ALL)
	    if (e.isSample())
		matches.add(e.filename);
    }

    // list of filenames (should it be elements?)
    private List matches;

    /*
      all results go into |matches|
      -- need to be able to tell somebody when a hit is found
      -- need event listeners?
      -- (will, later, anyway, for smart lists, right?)

      future:
      -- in search(), use Summary, so reading an entire folder is O(1), not O(n)
      -- (pretty much everything else is the same, it's just much faster)
    */

    // construct an SQL query that corresponds to this search
    public String toSQL() {
	String combiner = (type == ANY ? "OR" : "AND");
	StringBuffer sql = new StringBuffer("SELECT * FROM meta m ");
	if (criteria.size() > 0)
	    sql.append("WHERE ");

	for (int i=0; i<criteria.size(); i++) {
	    Criterion c = (Criterion) criteria.get(i);
	    sql.append(c.toSQL());
	    if (i < criteria.size() - 1)
		sql.append(" " + combiner + " ");
	}

	// GROUP BY sid?

	sql.append(";");

	return sql.toString();
    }

    @Override
	public String toString() {
	StringBuffer r = new StringBuffer();
	// BUG?: what if criteria.size()==0?

	for (int i=0; i<criteria.size(); i++) {
	    Criterion c = (Criterion) criteria.get(i);
	    r.append(c.toString());
	    if (i < criteria.size() - 1)
		r.append(", ");
	    if (i == criteria.size() - 2)
		r.append((type==ANY ? "or" : "and") + " ");
	}

	// BUG: use "Author", not "author" (look up in table)
	// BUG: use "Raw", not "R" (look up in table)

	return r.toString();
    }
}
