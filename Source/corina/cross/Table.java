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

package corina.cross;

import corina.Sample;
import corina.Element;
import corina.cross.Score;
import corina.site.Site;
import corina.site.SiteDB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.text.DecimalFormat;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
   A crossdating table: a table of samples, crossdates, and other
   information, from an 1-by-N cross.<p>

   A sample table might look similar to this when printed:<p>

   <table border="0" padding="0" spacing="0" bgcolor="lightblue">
   <tr><td>

   <h3>Crossdating table for: Zonguldak, Karabuk Spring 99 Master</h3>
   <table border="1">
	<tr>
		<th>Sample</th>
		<th>T-Score</th>
		<th>Trend</th>
		<th>D-Score</th>
		<th>Overlap</th>
		<th>Distance</th>
	</tr>
	<tr>
		<td>Istanbul, Belgrade Forest</td>
		<td>2.36</td>
		<td>76.5%</td>
		<td>0.62</td>
		<td>n=136</td>
		<td>49 km</td>
	</tr>
   </table>

   </tr></td>
   </table>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Table implements Runnable { //, Printable, Pageable {

    // input
    private Sample singleton;
    private List ss;

    // i18n
    private ResourceBundle msg = ResourceBundle.getBundle("CrossdatingBundle");

    // get rid of these formatters eventually.  scores should be able
    // to format themselves.
    static DecimalFormat f1, f2, f3;
    static {
	f1 = new DecimalFormat(new TScore().getFormat());
	f2 = new DecimalFormat(new Trend().getFormat());
	f3 = new DecimalFormat(new DScore().getFormat());
    }

    // output
    List table; // of Table.Row // PUBLIC!

    /**
       A row of the 1-by-N table, which holds the sample's title,
       crossdating scores, and other information.

       @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
       @version $Id$ */
    public class Row {
	/** The row's (master's) title. */
	public String title;

	/** The distance to this site, if able to be calculated, else
            null. */
	public Integer dist;

	// change these from doubles to Scores.  they need to be able
	// to print themselves.
	public double t=0.0;
	public double tr=0.0;
	public double d=0.0;

	/** Number of years overlap with this master. */
	public int overlap;
    }

    /** Construct a new Table.
	@param s Sample to compare all others to
	@param ss List of Elements to make table rows from
	@exception IOException if the sample <code>s</code> cannot be
	loaded */
    public Table(String s, List ss)
	throws IOException
    {
	singleton = new Sample(s);
	this.ss = ss;
	table = new ArrayList();
    }

    /** The title of this table.  The format is <code>"Crossdating
        Table for sampleTitle"</code> (for the title
        <code>sampleTitle</code> of the singleton).
	@return the title of this table */
    public String toString() {
	return msg.getString("table") + ": " + singleton.toString();
    }

    /** Run all computations for the table.  Computes T, trend, D,
	overlap, and distance (if applicable) between sites.  */
    public void run() {
	Site site1;
	try {
	    site1 = SiteDB.getSiteDB().getSite(singleton);
	} catch (Exception e) {
	    site1 = null;
	}

	for (int i=0; i<ss.size(); i++) {
	    // skip inactive elements
	    if (!((Element) ss.get(i)).active)
		continue;

	    // load element into s2
	    String f = ((Element) ss.get(i)).filename;
	    Sample s2;
	    try {
		s2 = new Sample(f);
	    } catch (IOException ioe) {
		continue;
	    }

	    // new row
	    Row r = new Row();
	    r.title = s2.toString();

	    // fill in crosses
	    r.overlap = s2.range.overlap(singleton.range);
	    if (r.overlap > 0) {
		TScore t = new TScore(singleton, s2);
		r.t = t.single();

		Trend tr = new Trend(singleton, s2);
		r.tr = tr.single();

		DScore d = new DScore(t, tr);
		r.d = d.single();
	    }

	    // distance
	    try {
		Site site2 = SiteDB.getSiteDB().getSite(s2);
		r.dist = new Integer(site1.location.distanceTo(site2.location));
	    } catch (Exception e) {
		r.dist = null;
	    }

	    // add to table
	    table.add(r);
	}
    }

    // save as HTML
    public void saveHTML(String filename) throws IOException {
	// open file, etc.
	BufferedWriter w = new BufferedWriter(new FileWriter(filename));

	// write header.  newlines don't really do anything in HTML,
	// and browsers are used to dealing with all sorts of crazy
	// newlines, so i'll just use \n and be concise instead of
	// w.newLine().
	w.write("<html>\n");
	w.write("<head>\n");
	w.write("<title>" + toString() + "</title>\n");
	w.write("</head>\n");
	w.write("<body bgcolor=\"#ffffff\">\n"); // specify all colors!
	w.write("<h1>" + toString() + "</h1>\n");
	w.write("<table border=\"0\" width=\"100%\">\n");
	w.write("<tr>\n");
	w.write("<th>" + msg.getString("title") + "</th>" +
		"<th>" + msg.getString("tscore") + "</th>" +
		"<th>" + msg.getString("trend") + "</th>" +
		"<th>" + msg.getString("dscore") + "</th>" +
		"<th>" + msg.getString("overlap") + "</th>" +
		"<th>" + msg.getString("distance") + "</th>\n");
	w.write("</tr>\n");

	// write lines
	for (int i=0; i<table.size(); i++) {
	    // get table row
	    Table.Row r = (Table.Row) table.get(i);

	    // write html table row
	    w.write("<tr>\n");
	    w.write("<td>" + r.title + "</td>" +
		    "<td>" + f1.format(r.t) + "</td>" +
		    "<td>" + f2.format(r.tr) + "</td>" +
		    "<td>" + f3.format(r.d) + "</td>" +
		    "<td>" + r.overlap + "</td>" +
		    "<td>" + (r.dist==null ? "" : (r.dist + " " + msg.getString("km"))) + "</td>\n");
	    w.write("</tr>\n");
	}

	// write HTML footer
	w.write("</table>\n");
	w.write("</body>\n");
	w.write("</html>\n");

	// close file
	w.close();
    }

    // save as plaintext
    public void saveText(String filename) throws IOException {
	// open file, etc.
	BufferedWriter w = new BufferedWriter(new FileWriter(filename));

	// write header
	w.write(msg.getString("title") + "\t" +
		msg.getString("tscore") + "\t" +
		msg.getString("trend") + "\t" +
		msg.getString("dscore") + "\t" +
		msg.getString("overlap") + "\t" +
		msg.getString("distance"));
	w.newLine();

	// write lines
	for (int i=0; i<table.size(); i++) {
	    Table.Row r = (Table.Row) table.get(i);
	    w.write(r.title + "\t" +
		    f1.format(r.t) + "\t" +
		    f2.format(r.tr) + "\t" +
		    f3.format(r.d) + "\t" +
		    r.overlap + "\t" +
		    (r.dist==null ? "" : (r.dist + " " + msg.getString("km"))));
	    w.newLine();
	}

	// close file
	w.close();
    }

}
