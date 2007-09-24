package edu.cornell.dendro.corina.db;

import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.Element;
import edu.cornell.dendro.corina.gui.Layout;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Hashtable;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

// DBBrowser: what you use to decide which samples to show, when using
// the db, instead of a file browser, which is clumsy.

/*
  possible layout:

  ----
  Site: [ZKB |v|]                            Search: (        )
  ----

  notes:
  -- site popup is a list/popup, depending on component height
  -- (search field stays on top, of course, i think.)
*/

public class DBBrowser extends JPanel {
    public DBBrowser(Connection conn /* ??? */) throws SQLException {
	// store connection
	this.c = conn;

	setLayout(new BorderLayout());

	add(new JLabel("Connecting to database..."));

	// TODO: make error messages MUCH more helpful/descriptive.

	// do the rest of this stuff after getSites() returns
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		// TODO: make this a list/popup dual
		// a list of sites
		Vector sites;
		try {
		    long t1,t2;
		    System.out.print("listing sites...");
		    t1 = System.currentTimeMillis();
		    sites = getSites();
		    t2 = System.currentTimeMillis();
		    System.out.println("done!  dt=" + (t2-t1) + " ms");
		} catch (SQLException se) {
		    System.out.println("exception! -- " + se);
		    return;
		}
		final JList sitesList = new JList(sites);
		add(new JScrollPane(sitesList), BorderLayout.WEST);

		// BUG: need to restrict selection to exactly one site?

		final DB db = new DB(c);

		sitesList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
			    if (e.getValueIsAdjusting())
				return;

			    String site = (String) sitesList.getSelectedValue();

			    System.out.println("site " + site + " selected");

			    try {
				long t1, t2;
				t1 = System.currentTimeMillis();
				List samples;
				{
				    samples = db.getElements(site);
				    // don't close db -- that would close c!
				}
				t2 = System.currentTimeMillis();
				System.out.println("got elements (dt=" + (t2-t1) + ")");

				// DEBUG: print out sample titles
				for (int i=0; i<samples.size(); i++) {
				    Element el = (Element) samples.get(i);
				    System.out.println("got sample title=" + el.details.get("id"));
				}

				/*
				// REFACTOR (run in different thread): load samples
				for (int i=0; i<samples.size(); i++) {
				    int sid = ((Integer) samples.get(i)).intValue();
				    Sample s = SQLize.load(sid, c);
				    System.out.println("loaded sample title=" + s.meta.get("id"));
				}
				*/
			    } catch (SQLException se) {
				System.out.println("error querying: " + se);
				se.printStackTrace();
			    }

			    // TODO: update view below
			    // STRATEGY:
			    // -- make (empty) elements from all sid's
			    // -- create browser table
			    // -- one at a time, call SQLize.load() (rename!) to load data
			    // -- (add closure to Element to do this more cleanly?)
			}
		    });

		// remove old label
		remove(0);

		// a search field
		JTextField field = new JTextField("", 10);
		add(Layout.flowLayoutL("Search: ", field), BorderLayout.EAST);
		// TODO: on type, update view below

		// icky
		invalidate();
		repaint();
	    }
	});
    }
    private Connection c;

    // make a list of all available sites
    Vector getSites() throws SQLException {
	// send the SELECT to the database -- only done once, so no real need for a PS
	Statement stmt = c.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT m.site FROM meta m GROUP BY site;");

	// from results, put each one in a list
	Vector list = new Vector();
	while (rs.next())
	    list.add(rs.getString(1));
	// Q: need to sort list now?
	stmt.close();
	return list;
    }

    // search for a string in all meta fields
    // IDEAL: search visible fields -- wouldn't this require the i18n to live in the rdbms, also?
    // FIXME: return sids
    /*
    List getSamples(String search) throws SQLException {
	// send the SELECT to the database
	PreparedStatement stmt = c.prepareStatement("???"); // WRITEME!
	ResultSet rs = stmt.executeQuery();
	stmt.close();

	// from results, put each element in a list
	List list = new ArrayList();
	while (rs.next())
	    list.add(null); // WRITEME: rs.get???());
	// need to sort?  no, display-component will sort it as needed.
	return list;
    }
*/

    // DESIGN: return int[] (sids)?
    // PERFORMANCE: listing sid's, then loading each one separately, is much slower than
    // simply selecting * from meta to begin with.  how to deal with this?
    List getSamples(String site) throws SQLException {
	// send the SELECT to the database
	PreparedStatement stmt = c.prepareStatement("SELECT sid FROM meta WHERE UPPER(site) = ?;");
	stmt.setString(1, site.toUpperCase());
	ResultSet rs = stmt.executeQuery();

	// need to sort?  no, display component will sort however the user wants.
	// BUG: need to GROUP BY?

	// how many?
	rs.last(); // BUG: for some databases, result sets are "FORWARD ONLY" (!)
	int num = rs.getRow();

	// from results, put each element in a list.
	List list = new ArrayList();
	rs.first(); // BUG: fails for some databases
	for (int i=0; i<num; i++) {
	    list.add(new Integer(rs.getInt(1)));
	    rs.next();
	}
	stmt.close();
	return list;
    }

    // strategy:
    // -- on creation, "SELECT m.site FROM meta m GROUP BY site"
    // -- on site selected, "SELECT m.<all (visible?) meta> FROM meta m WHERE m.site = <selected site>"
    // -- on search, "SELECT m.<all (visible?) metadata> FROM meta m WHERE <something? looks like search term>"

    // Q: need to mark one Source as "primary" = where to get the site info from?
    // A: no, samples in each Source get their site info from their own Source (as you see above).
}
