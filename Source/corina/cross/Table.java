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
import corina.site.SiteNotFoundException;

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
    private ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

    // get rid of these formatters eventually.  scores should be able
    // to format themselves.
    static DecimalFormat f1, f2, f3;
    static {
        // REFACTOR: the crosses to use should be user-pickable, so this is b-a-d.
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

        /** The distance to this site, if able to be calculated, else null. */
        public Integer dist;

        // change these from doubles to Scores.  (they should be able
        // to print themselves.)
        public double t=0.0;
        public double tr=0.0;
        public double d=0.0;
        // they shouldn't be hard-coded like this, either.  what about the wj cross?  r-score?

        /** Number of years overlap with this master. */
        public int overlap;

        // write the row, given a format string
        // -- cache mf to bypass parsing?  nah, thread safety goes out the window.  better just pass in the msgfmt.
        public String toString(String format) {
            return null; // writeme?
        }
    }

    /** Construct a new Table.
	@param s Sample to compare all others to
	@param ss List of Elements to make table rows from
	@exception IOException if the sample <code>s</code> cannot be loaded
        @deprecated */
    public Table(String s, List ss) throws IOException {
        singleton = new Sample(s);
        this.ss = ss;
        table = new ArrayList();
    }

    public Table(Sequence seq) throws IOException {
        // what to do?
        // -- set singleton -- first fixed sample
        String fixed = (String) seq.getAllFixed().get(0); // use current view?
        singleton = new Sample(fixed);
        // -- set ss -- all moving samples (excluding the fixed one)
        ss = new ArrayList();
        for (int i=0; i<seq.getAllMoving().size(); i++) {
            String moving = (String) seq.getAllMoving().get(i);
            if (!fixed.equals(moving))
                ss.add(new Element(moving));
        }
        // -- create table list
        table = new ArrayList();
        // -- compute table?  no, not yet -- run() computes table
        run(); // no, do this later!
        // hmm...
        // -- update table on-screen as it's computed, like browser does
        // -- need a clever way to handle ioexceptions in run() -- dummy 'breakage' element singleton?
        // how?
        // -- sequence needs getfixed(), getmoving() methods
    }
    
    /** The title of this table.  The format is <code>"Crossdating
        Table for sampleTitle"</code> (for the title
        <code>sampleTitle</code> of the singleton).
	@return the title of this table */
    public String toString() {
	return msg.getString("table") + ": " + singleton.toString();
        // should be: "Crossdate: table for blah (range)"
    }

    /** Run all computations for the table.  Computes T, trend, D,
	overlap, and distance (if applicable) between sites.  */
    public void run() {
        Site site1;
        try {
            site1 = SiteDB.getSiteDB().getSite(singleton);
        } catch (SiteNotFoundException snfe) {
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

            // fill in crosses, if they overlap
            r.overlap = s2.range.overlap(singleton.range);
            if (r.overlap > 0) {
                r.t = new TScore(singleton, s2).single();
                r.tr = new Trend(singleton, s2).single();
                r.d = new DScore(t, tr).single();
            }

            // distance
            try {
                Site site2 = SiteDB.getSiteDB().getSite(s2);
                r.dist = new Integer(site1.location.distanceTo(site2.location));
            } catch (SiteNotFoundException snfe) {
                r.dist = null;
            }

            // add to table
            table.add(r);
        }
    }

    // true, this [saveHTML] is simple enough it shouldn't be in its own class.  therefore, for exportdialog,
    // i need to be able to REGISTER THIS METHOD as a saver for this type.  exportdialog, then,
    // is given any object and presents any savers it knows about:

    /*
     static {
         ExportDialog.register(Sample.class, "Tucson.save()");
         ExportDialog.register(Table.class, "Table.saveHTML()"); // how does it get the name of this format, then?
         (duh, tell it)
             ExportDialog.register(Table.class, "Table.saveHTML()", "HTML");
     }
     ...
     and then to use, simply:
     ExportDialog.export(myTable); -- does myTable.getClass(), checks its registry, lists "Text" and "HTML"
     ...
     ALSO: i should be able to simply declare a "default" filetype, so file->save doesn't need any special code.
     ExportDialog.register(Sample.class, "Corina.save()", true);
     -- what if the format depends on the view, like cross/table/grid?  (duh, it shouldn't)

     ...
     -- exporters for crossdates are "HTML (x)", for x = { sigs, all, histo, table, grid }
     */
    
    // save as HTML -- XHTML 1.1, in fact
    public void saveHTML(String filename) throws IOException {
        // open file, etc.
        BufferedWriter w = new BufferedWriter(new FileWriter(filename));

        // write header.  newlines don't really do anything in HTML,
        // and browsers are used to dealing with all sorts of crazy
        // newlines, so i'll just use \n and be concise instead of
        // w.newLine().

       // XHTML1.1 -- this one's from http://www.alistapart.com/stories/doctype/
        w.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" " +
                "\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");

        w.write("<html>\n");
        w.write("\n");
        w.write("<head>\n");
        w.write("   <title>" + toString() + "</title>\n");
        w.write("   <style>\n");
        w.write("      <!--\n");
        w.write("         td, th { text-align: left }\n");
        w.write("         tr.odd { background-color: #eef }\n");
        w.write("         tr.even { background-color: #fff }\n");
        w.write("      -->\n");
        w.write("   </style>\n");
        w.write("</head>\n");
        w.write("\n");
        w.write("<body>\n");
        w.write("\n");
        w.write("<table border=\"0\" cellspacing=\"4\" cellpadding=\"4\" rules=\"groups\">\n");
        w.write("   <caption>" + toString() + "</caption>\n");
        w.write("\n");
        w.write("   <colgroup align=\"left\" span=\"2\"/>\n");
        w.write("   <colgroup align=\"left\"/>\n");
        w.write("   <colgroup align=\"left\"/>\n");
        w.write("   <colgroup align=\"left\"/>\n");
        w.write("   <colgroup align=\"left\"/>\n");
        w.write("   <colgroup align=\"left\"/>\n");
        w.write("\n");
        w.write("   <thead>\n");
        w.write("   <tr>\n");
        w.write("      <th width=\"40%\">" + msg.getString("title") + "</th>\n");
        w.write("      <th width=\"10%\">" + msg.getString("range") + "</th>\n");
        w.write("      <th width=\"10%\">" + msg.getString("tscore") + "</th>\n");
        w.write("      <th width=\"10%\">" + msg.getString("trend") + "</th>\n");
        w.write("      <th width=\"10%\">" + msg.getString("dscore") + "</th>\n");
        w.write("      <th width=\"10%\">" + msg.getString("overlap") + "</th>\n");
        w.write("      <th width=\"10%\">" + msg.getString("distance") + "</th>\n");
        w.write("   </tr>\n");
        w.write("\n");
        w.write("   <tbody>\n");

        // write lines
        for (int i=0; i<table.size(); i++) {
            // get table row
            Table.Row r = (Table.Row) table.get(i);

            // write html table row -- (why isn't this a method of row, then?)
            w.write("   <tr class=\"" + (i%2==1 ? "odd" : "even") + "\">\n");
            w.write("<td>" + r.title + "</td>" + // see below; start=<tr><td>, end=</td></tr>\n, spacer=</td><td> (!)
                    "<td>" + f1.format(r.t) + "</td>" +
                    "<td>" + f2.format(r.tr) + "</td>" +
                    "<td>" + f3.format(r.d) + "</td>" +
                    "<td>" + r.overlap + "</td>" +
                    "<td>" + (r.dist==null ? "" : (r.dist + " " + msg.getString("km"))) + "</td>\n");
            w.write("   </tr>\n");
        }

        // even better than writerow(start,end,spacer): use messageformat.
        // writerow("{1}\t{2}\t{3}\t{4}\t{5}\t{6}");
        // (they're all strings; 1-2-3-4-5-6 = title-t-tr-d-overlap-dist but you're of course welcome to use them out-of-order)
        // writerow("<tr><td>{1}</td><td>{2}</td><td>{3}</td><td>{4}</td><td>{5}</td></tr>");
        // -- more printf-like, and much more readable.  yay!
        // -- can do the title this way with a static method, too.
        
        w.write("   </tbody>\n");
        w.write("</table>\n");
        w.write("\n");
        w.write("</body>\n");
        w.write("\n");
        w.write("</html>\n");

        // close file
        w.close();
    }

    // save as plaintext -- for spreadsheets, text-only (dot-matrix) printers,
    // or for inserting into a table (in a word processor) for your own formatting.
    public void saveText(String filename) throws IOException {
        // open (buffered) file
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
            w.write(r.title + "\t" + // again, method of row?  yes, with start="", end="", spacer="\t"
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
