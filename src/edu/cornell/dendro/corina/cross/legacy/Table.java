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

package edu.cornell.dendro.corina.cross.legacy;

import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.print.*; // !!!

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import java.awt.print.Printable;

import javax.swing.table.AbstractTableModel;

/**
 A crossdating table: a table of samples, crossdates, and other
 information, from an 1-by-N cross.

 <p>A sample table might look like this when printed:</p>

 <blockquote class="paper">

 <h3>Crossdating table for Zonguldak, Karabuk Spring 99 Master</h3>
 <table border="1" cellspacing="0" width="100%">
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

 </blockquote>

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id$ */
public class Table extends AbstractTableModel {
	// HMM: i'm not sure i like one of my core data classes extending
	// a swing class...

	// OBSOLETE: old random comment that i don't want to delete yet:
	// (loop for r in table do (write (row-format r text)))
	// -- or something like that

	/*
	  TODO:
	  -- make it use the crossdates that CDK picked, not Cross.DEFAULTs

	  ORGANIZATION:
	  -- 3 views of Sequences: CrossdateView, TableView, GridView
	  ---- fourth view, SamplesView?  this was once an idea.  [cmd 0]
	  -- 3 views of Crossdates: SignificantScoresView, AllScoresView, HistogramView
	  -- each view is printable
	  -- each view has menus/menuitems?
	  -- Sequence has load/save, which saves:
	  ---- list of fixed filenames
	  ---- list of moving filenames
	  ---- which view you're looking at
	  ---- any parameters for that view
	  ---- (goal: new cross, save, close, open, right back where you were)

	  e.g.,
	  <?xml version="1.0"?>
	  <crossdate view="grid">
	    <fixed>
	  <sample filename="abc.123"/>
	  <sample filename="abc.123"/>
	    </fixed>
	    <moving>
	  <sample filename="abc.123"/>
	  <sample filename="abc.123"/>
	    </moving>
	  </crossdate>
	 */

	// input
	private Sample singleton;
	private ElementList ss; // List of Elements to use

	// output
	private List<Table.Row> rows; // of Table.Row

	/*
	  A row of the 1-by-N table, which holds the sample's title, and
	  crossdating scores.

	  (It's just a Single object with a title, and the ability to
	  format itself into a line of text.)

	  (Single holds all crossdate scores, the overlap, and the
	  distance, if available.)
	 */
	private static class Row extends Single {
		/** The row's (sample's) title. */
		private String title;

		// (make private, with getTitle() method?  make this toString()?)
		
		public Row(Sample fixed, Sample moving, Element movingElement) {
			super(fixed, moving); // here's where all the computations get done
			title = moving.toString();

			this.movingFilename = moving.getMeta("filename").toString();
			this.movingElement = movingElement;
		}

		private String movingFilename;
		private Element movingElement;

		// return the moving sample
		public String getMovingSampleFilename() {
			return movingFilename;
		}
		
		public Element getMovingElement() {
			return movingElement;
		}
		
	}

	// get the sample in a certain row
	public String getFilenameOfRow(int row) {
		Row r = (Row) rows.get(row);
		return r.getMovingSampleFilename();
	}

	// get the sample in a certain row
	public Element getElementOfRow(int row) {
		Row r = (Row) rows.get(row);
		return r.getMovingElement();
	}

	//
	// table model
	//

	// -- these methods should be the only way to get at the stuff above here
	// FIXME: use a local array, ALGORITHMS?, instead of hardcoding Cross.DEFAULT_CROSSDATES.
	// -- FUTURE: get ALGORITHMS from the cmd-E dialog, just like the normal crossdate view does.
	// -- REFACTOR: CrossFrame should be split into CrossFrame and CrossPane!

	public int getColumnCount() {
		// title, scores*, overlap, distance
		return 6;
	}

	public int getRowCount() {
		return rows.size();
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return I18n.getText("sample");

		case 1:
			return I18n.getText("tscore"); // ***
		case 2:
			return I18n.getText("rvalue");
		case 3:
			return I18n.getText("trend");

		case 4:
			return I18n.getText("overlap");
		case 5:
			return I18n.getText("distance");
		default:
			throw new IllegalArgumentException(); // never happens
		}
	}

	public Object getValueAt(int row, int col) {
		Row r = (Row) rows.get(row);

		switch (col) {
		case 0:
			return r.title;

		case 1:
			return (r.n == 0 ? "" : r.formatT()); // ***
		case 2:
			return (r.n == 0 ? "" : r.formatR());
		case 3:
			return (r.n == 0 ? "" : r.formatTrend());

		case 4:
			return String.valueOf(r.n);
		case 5:
			return r.distanceAsString();
		default:
			throw new IllegalArgumentException(); // never happens
		}
	}

	//
	// constructor
	//

	// new c'tor: takes any (fixed, moving[]) combo from a seq.
	public Table(Element fixed, ElementList moving) throws IOException {
		super();
		// set singleton -- fixed sample
		singleton = fixed.load();

		// set ss -- all moving samples (excluding the fixed one)
		ss = new ElementList();
		for (Element m : moving) {
			if (!fixed.equals(m))
				ss.add(m);
		}

		// -- create table list
		rows = new ArrayList<Table.Row>();

		// -- compute table?  no, not yet -- compute() computes table
		compute(); // no, do this later!
		// hmm...
		// -- update table on-screen as it's computed?
		// -- need a clever way to handle ioexceptions in run()
		// ---- dummy 'breakage' element singleton?
	}

	//
	// compute
	//

	// run all computations for the table.  computes t, trend, d,
	// overlap, and distance (if available) between sites.
	private void compute() {
		for (Element e : ss) {
			// skip inactive elements -- DUMB: elements aren't
			// "inactive", only elements-in-sums are.
			//if (!e.isActive())
			//	continue;
			// not useful: ss is assembled above regardless of active flag

			try {
				Sample movingSample = e.load();

				// add new row to table
				rows.add(new Row(singleton, movingSample, e));
			} catch (IOException ioe) {
				// can't load it?  ignore it!  -- DO SOMETHING BETTER!
			}
		}
	}

	//
	// formatting
	//

	/** The title of this table.  The format is <code>"Crossdating
	    Table for sampleTitle"</code> (for the title
	    <code>sampleTitle</code> of the singleton).
	@return the title of this table */
	@Override
	public String toString() {
		return I18n.getText("crossdating_table") + " for " + singleton;
		// WHAT is this used for?  -- saveHTML, printing, ...?
	}

	// write the header row, given start-of-row, between-cells,
	// and end-of-line strings
	private String formatHeaderRow(String startOfRow, String betweenCells, String endOfLine) {
		StringBuffer buf = new StringBuffer();
		buf.append(startOfRow);

		int n = getColumnCount();
		for (int i = 0; i < n; i++) {
			buf.append(getColumnName(i));
			if (i < n - 1)
				buf.append(betweenCells);
		}

		buf.append(endOfLine);
		return buf.toString();
	}

	// write the row, given start-of-row, between-cells,
	// and end-of-line strings
	private String formatRow(int row, String startOfRow, String betweenCells, String endOfLine) {
		StringBuffer buf = new StringBuffer();
		buf.append(startOfRow);

		int n = getColumnCount();
		for (int i = 0; i < n; i++) {
			buf.append(getValueAt(row, i));
			if (i < n - 1)
				buf.append(betweenCells);
		}

		buf.append(endOfLine);
		return buf.toString();
	}

	//
	// exporting
	//

	// save as HTML -- XHTML 1.1, in fact
	public void saveHTML(String filename) throws IOException {
		BufferedWriter w = null;

		try {

			// open file, etc.
			w = new BufferedWriter(new FileWriter(filename));

			// write header.  newlines don't really do anything in HTML,
			// and browsers are used to dealing with all sorts of crazy
			// newlines, so i'll just use \n instead of w.newLine(), which
			// makes this method far more readable.

			// XHTML1.1 -- i got this doctype
			// from http://www.alistapart.com/stories/doctype/

			// BUT: isn't xhtml1.1 virtually unreadable if served correctly?
			// how much different is xhtml1.0?
			w.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" " + "\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");

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
			// FIXME!  is range a column, or not?
			/* for (int i=0; i<Cross.DEFAULT_CROSSDATES.length; i++) // ***
			   w.write("   <colgroup align=\"left\"/>\n");
			   w.write("   <colgroup align=\"left\"/>\n");
			   w.write("   <colgroup align=\"left\"/>\n");
			 */
			for (int i = 1; i < getColumnCount(); i++)
				w.write("   <colgroup align=\"left\"/>\n");

			w.write("\n");
			w.write("   <thead>\n");
			w.write("   <tr>\n");

			w.write("      <th width=\"50%\">" + getColumnName(0) + "</th>\n");
			for (int i = 1; i < getColumnCount(); i++)
				w.write("      <th width=\"10%\">" + getColumnName(i) + "</th>\n");
			// FIXME: make width of columns (100 - 50) / (getColumnCount()-1)?

			w.write("   </tr>\n");
			w.write("\n");
			w.write("   <tbody>\n");

			// write lines
			for (int i = 0; i < getRowCount(); i++) {
				w.write("   <tr class=\"" + (i % 2 == 1 ? "odd" : "even") + "\">\n");
				w.write(formatRow(i, "<td>", "</td><td>", "</td>"));
				w.write("   </tr>\n");
			}

			w.write("   </tbody>\n");
			w.write("</table>\n");
			w.write("\n");
			w.write("</body>\n");
			w.write("\n");
			w.write("</html>\n");

		} finally {
			// close file
			if (w != null)
				try {
					w.close();
				} catch (IOException ioe) {
					// ignore, now
				}
		}
	}

	// save as plaintext -- for spreadsheets, stats programs (like matlab),
	// really old printers, or for inserting into a table (in a word
	// processor) for your own formatting.
	public void saveText(String filename) throws IOException {
		BufferedWriter w = null;

		try {
			// open (buffered) file
			w = new BufferedWriter(new FileWriter(filename));
			String newLine = System.getProperty("line.separator");

			// write header
			w.write(formatHeaderRow("", "\t", newLine));

			// write lines
			for (int i = 0; i < getRowCount(); i++)
				w.write(formatRow(i, "", "\t", newLine));
			// REFACTOR: this looks very similar to above...
		} finally {
			// close file
			if (w != null)
				try {
					w.close();
				} catch (IOException ioe) {
					// ignore, now
				}
		}
	}

	//
	// printing
	//

	// hack!
	public Printable print() {
		return new TablePrinter(this);
	}

	// TODO: this is not a static inner class, but it probably shouldn't
	// be.  instead, it shouldn't need |t| passed to it: it should simply
	// call the enclosing class's methods (it does anyway).  the only
	// catch is toString() -- how do you specify "enclosing class's
	// implementation of this method"?
	private class TablePrinter extends Printer {
		TablePrinter(Table t) {
			// title
			lines.add(new TextLine(t.toString(), Line.TITLE_SIZE));
			// FIXME: i really want a TitleLine right now!
			lines.add(new EmptyLine());

			// table header
			StringBuffer headerSpec = new StringBuffer("> 50% ");
			// EXTRACT 50%, (all widths?)

			// BETTER: get column widths from jtable
			// -- no, but make them 50/(n-1)%
			for (int i = 1; i < getColumnCount(); i++)
				headerSpec.append("> 10%");
			TabbedLineFactory f = new TabbedLineFactory(headerSpec.toString());
			lines.add(f.makeLine(formatHeaderRow("", "\t", "")));
			lines.add(new ThinLine(0.0f, 1.0f));

			// lines of table
			for (int i = 0; i < getRowCount(); i++)
				lines.add(f.makeLine(formatRow(i, "", "\t", "")));

			// REFACTOR: this looks just like Table.saveHTML/saveText()

			// print by-line. -- REFACTOR: addByLine()?  or just make
			// ByLine = empty+thin+text?
			// DONE IN: Browser, CrossPrinter, Table, SitePrinter,
			// SamplePrinter (sort of)
			lines.add(new EmptyLine());
			lines.add(new ThinLine(0.0f, 0.3f));
			lines.add(new ByLine());
		}
	}

	// old to-do list for tableprinter:

	// TODO: make widths = (100 - 50) / (cols - 1)
	// TODO: extract 50% const!
	// TODO: make Table use any number of crossdates -- get rid of formatT(), etc.
	// TODO: make join(strings, start, tab, end)?
}
