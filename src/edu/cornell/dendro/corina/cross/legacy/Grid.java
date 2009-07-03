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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.cornell.dendro.corina.Preview;
import edu.cornell.dendro.corina.Previewable;
import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.formats.WrongFiletypeException;
import edu.cornell.dendro.corina.logging.CorinaLog;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementFactory;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.StringUtils;

/**
   A crossdating grid.

   <p>All of the samples are listed down the left side, and also
   across the top.  If you go right from a sample on the left, and
   down from a different sample no the top, the cell where they meet
   contains their crossdate (t, trend, d, overlap).  If you trace
   right from a sample, and down from the same sample, that cell (on
   the diagonal) has the length of the sample, by convention.</p>

   <p>A Grid might look similar to this when printed:</p>

<blockquote class="paper">

   <table border="1" cellspacing="0">

	<tr>
	  <td> &nbsp; <br> &nbsp; <br> &nbsp; <br> &nbsp; </td>
	  <td>SPI2A.IND     </td>
	  <td> &nbsp; <br> &nbsp; <br> &nbsp; <br> &nbsp; </td>
	  <td> &nbsp; <br> &nbsp; <br> &nbsp; <br> &nbsp; </td>
	</tr>

	<tr>
	  <td>SPI2A.IND </td>
	  <td>n=54            </td>
	  <td>SPI3A.IND </td>
	  <td> &nbsp; <br> &nbsp; <br> &nbsp; <br> &nbsp; </td>
	</tr>

	<tr>
	  <td>SPI3A.IND       </td>
	  <td>t=0.00 <br> tr=47.2% <br> d=0.00 <br> n=54 </td>
	  <td>       n=170 </td>
	  <td>SPI4A.IND </td>
	</tr>

	<tr>
	  <td>SPI4A.IND </td>
	  <td>t=0.22 <br> tr=67.2% <br> d=0.03 <br> n=52 </td>
	  <td>t=1.63 <br> tr=55.6% <br> d=0.09 <br> n=55 </td>
	  <td>n=55     </td>
	</tr>

   </table>

</blockquote>

   <h2>Left to do</h2>
   <ul>
     <li>clean up this class: many many lines are longer than 80 characters
     <li>font handling code is sometimes inefficient (lots of "new Font(...)")
     <li>font handling code is sometimes incorrect (nudge factors
         instead of measuring ascents)
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Grid implements Runnable, Previewable {
	private static final CorinaLog log = new CorinaLog(Grid.class);

	// inputs
	private ElementList elements;

	private int num; // number of active files

	// outputs
	private Cell cell[][];

	private Exception error = null;

	// (used when creating graph of these samples)
	public ElementList getElements() {
		return elements;
	}

	// ----------------------------------------
	public interface Cell {
		public abstract void print(Graphics2D g2, int x, int y, int width, int height, float scale);

		public abstract String toXML();
		public abstract String toString();
	}

	public static class EmptyCell implements Cell {
		public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
			// do nothing
		}

		public String toXML() {
			return "<empty/>";
		}
		
		@Override
		public String toString() {
			return null;
		}
	}

	public class HeaderCell implements Cell {
		protected String name;

		public HeaderCell(String name) {
			// crop directory
			int index = name.lastIndexOf(File.separatorChar);
			this.name = name.substring(index + 1);
		}

		// awful hack to allow us to use popup menus referencing this cell...
		Sample fixed;
		public Sample getFixed() { return fixed; }
		
		public HeaderCell(Sample fixed) {
			String name = fixed.getDisplayTitle();
			
			// crop directory
			int index = name.lastIndexOf(File.separatorChar);
			this.name = name.substring(index + 1);
			this.fixed = fixed;
		}
		
		public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
			// clip this cell 
			g2.setClip(x, y, width, height);
			
			// filename
			g2.drawString(name, x + EPS,
						  y + (int) ((getCellHeight() / 2 - getLineHeight() / 2) * scale));
		}

		public String toXML() {
			return "<header name=\"" + name + "\"/>";
		}
		
		@Override
		public String toString() {
			return name;
		}
	}

	public class HeaderRangeCell extends HeaderCell {
		private Range range;

		public HeaderRangeCell(String name, Range range) {
			super(name);
			this.range = range;
		}

		public HeaderRangeCell(Sample fixed) {
			super(fixed);
			this.range = fixed.getRange();
		}

		@Override
		public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
			// clip this cell 
			g2.setClip(x, y, width, height);
			
			// filename
			g2.drawString(name, x + EPS, y
					+ (int) ((getCellHeight() / 2) * scale));

			// range
			g2.drawString(range.toString(), x + EPS, y
					+ (int) ((getCellHeight() / 2 + getLineHeight()) * scale));
		}

		@Override
		public String toXML() {
			return "<header name=\"" + name + "\" range=\"" + range + "\"/>";
		}
		
		@Override
		public String toString() {
			return name.toString() + " " + range.toString();
		}
	}
	
	// (when that's done, t/tr/d can be unified between sequence and onecross)
	// hey, cross.single() only makes sense in the context of a onecross, right?  score!
	public class CrossCell extends Single implements Cell {
		
		Sample fixed, moving;
		
		public CrossCell(Sample fixed, Sample moving) {
			super(fixed, moving);
			this.fixed = fixed;
			this.moving = moving;
		}
		
		// awful hack to allow us to use popup menus referencing this cell...
		public Sample getFixed() { return fixed; }
		public Sample getMoving() { return moving; }

		public CrossCell(float t, float tr, float d, float r, int n) {
			// (this is only used for xml loading now -- ok?)
			super(t, tr, d, r, n);
		}
		
		public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
			// fill with highlight -- would the user ever NOT want this?  well, yes, possibly.
			if (Boolean.valueOf(App.prefs.getPref(Prefs.GRID_HIGHLIGHT))
					.booleanValue()
					&& isSignificant()) {
				Color oldColor = g2.getColor();
				g2.setColor(App.prefs.getColorPref(Prefs.GRID_HIGHLIGHTCOLOR,
						Color.green));
				g2.fillRect(x, y, (int) (getCellWidth() * scale),
						(int) (getCellHeight() * scale));
				g2.setColor(oldColor);
			}

			// box
			g2.drawRect(x, y, (int) (getCellWidth() * scale),
					(int) (getCellHeight() * scale));

			// little/no overlap: just show the overlap
			if (n < 10) { // Cross.getMinimumOverlap()) {
				g2.drawString("n=" + n, x + EPS,
							  y + (int) ((getCellHeight() / 2 - getLineHeight() / 2) * scale));
				return;
			}

			// cross
			// REFACTOR: {"t=" + blah.format(t)} should be simply Score.toString()?
			// TODO: need Cross.getShortName() (tscore -> "t") method
			switch (gridFormat) {
			case GF_1:
				g2.drawString("t=" + formatT() + ", r=" + formatR(), x + EPS, y
						+ (int) (getLineHeight() * scale) - EPS);
				g2.drawString("tr=" + formatTrend(), x + EPS, y
						+ (int) (2 * getLineHeight() * scale) - EPS);
				g2.drawString("D=" + formatD(), x + EPS, y
						+ (int) (3 * getLineHeight() * scale) - EPS);
				g2.drawString("n=" + String.valueOf(n), x + EPS, y
						+ (int) (4 * getLineHeight() * scale) - EPS);
				break;
				
			case GF_2:
				g2.drawString("t=" + formatT(), x + EPS, y
						+ (int) (getLineHeight() * scale) - EPS);
				g2.drawString("r=" + formatR(), x + EPS, y
						+ (int) (2 * getLineHeight() * scale) - EPS);
				g2.drawString("tr=" + formatTrend(), x + EPS, y
						+ (int) (3 * getLineHeight() * scale) - EPS);
				g2.drawString("n=" + String.valueOf(n), x + EPS, y
						+ (int) (4 * getLineHeight() * scale) - EPS);
				break;				
			}
		}

		// in toXML, store full precision, with no %'s -- this means
		// later we won't have too few digits, if the user decides she
		// wants more, and we won't have to worry about parsing it
		// incorrectly with NumberFormat.parse().  the users never
		// need to look at a *.grid file, either, so they won't care.
		// (...later: *.cross? *.xdate? *.xd?  i like *.xdate)
		
		// err... cross already has this?
		
		@Override
		public String toString() {
			String s;
			
			if(n < 10)
				return "n=" + String.valueOf(n);
			
			switch (gridFormat) {
			case GF_1:
				s = "t=" + formatT() + ", " +
					"tr=" + formatTrend() + ", " +
					"D=" + formatD() + ", " +					
					"n=" + String.valueOf(n);
				break;
				
			case GF_2:
				s = "t=" + formatT() + ", " +
					"r=" + formatR() + ", " +
					"tr=" + formatTrend() + ", " +
					"n=" + String.valueOf(n);				
				break;
				
			default:
				// this should never happen.
				s = null;
			}
			
			return s;
		}
	}

	public static class LengthCell implements Cell {
		private int length;

		LengthCell(int length) {
			this.length = length;
		}

		public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
			// box? -- no box for you!  (the box nazi, of course.)

			// length
			g2.drawString("n=" + length, x + EPS,
						  y + (int) ((getCellHeight() / 2 - getLineHeight() / 2) * scale));
		}

		public String toXML() {
			return "<length n=\"" + length + "\"/>";
		}
		
		@Override
		public String toString() {
			return String.valueOf(length);
		}
	}

	// ----------------------------------------

	// cell factory -- given XML tag name and attributes
	private Cell makeCell(String name, Attributes atts) { // can't be static: cells aren't static
		if (name.equals("header")) {
			String r = atts.getValue("range"); // check for range="a-b"
			if (r != null)
				return new HeaderRangeCell(atts.getValue("name"), new Range(r));
			else
				return new HeaderCell(atts.getValue("name"));
		} else if (name.equals("length")) {
			return new LengthCell(Integer.parseInt(atts.getValue("n")));
		} else if (name.equals("cross")) {
			return new CrossCell(Float.parseFloat(atts.getValue("t")), Float
					.parseFloat(atts.getValue("tr")), Float.parseFloat(atts
					.getValue("d")), Float.parseFloat(atts.getValue("r")),
					Integer.parseInt(atts.getValue("n"))); // exception?
		} else {
			throw new IllegalArgumentException();
		}
	}

	// ----------------------------------------
	// print one page of a grid
	private static class GridPage implements Printable {
		private Grid grid;

		private int startRow, endRow, startCol, endCol;

		public GridPage(Grid grid, int startRow, int endRow, int startCol,
				int endCol) {
			this.grid = grid;
			this.startRow = startRow;
			this.endRow = endRow;
			this.startCol = startCol;
			this.endCol = endCol;
		}

		public int print(Graphics g, PageFormat pf, int pageNr)
				throws PrinterException {
			// WAS: if (pageNr != 0) return NO_SUCH_PAGE;

			// no, pageNr is 1 for the second page!  (did the docs say it would?)
			// now: ignore pageNr here

			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(0.1f)); // what's a good thickness?

			// set font (for all cells)
			// FIXME: use Prefs
			if (App.prefs.getPref("corina.grid.font") != null)
				g2.setFont(Font.decode(App.prefs.getPref("corina.grid.font")));

			// figure out stop row, col: end of page, or end of grid,
			// whichever comes first
			int stopRow = Math.min(endRow, grid.cell.length - 1);
			int stopCol = Math.min(endCol, grid.cell[0].length - 1);

			// print each one
			for (int x = startCol; x <= stopCol; x++) {
				for (int y = startRow; y <= stopRow; y++) {
					Cell c = grid.cell[y][x];
					Shape oldclip = g2.getClip();
					c.print(g2, ((int) pf.getImageableX()) + (x - startCol)
							* getCellWidth(), ((int) pf.getImageableY())
							+ (y - startRow) * getCellHeight(), 
							getCellWidth(), getCellHeight(), 1.0f); // always print to paper with scale=1.0
					
					// remove any clipping set by the cell...
					g2.setClip(oldclip);
				}
			}

			return PAGE_EXISTS;
		}
	}

	// ----------------------------------------
	// print all pages of a grid (using GridPage)
	private static class GridPrinter implements Pageable {
		private Grid grid;

		private int size;

		private int rowsPerPage, colsPerPage, pagesWide, pagesTall, numPages;

		private PageFormat pf;

		public GridPrinter(Grid grid, PageFormat pf) {
			this.grid = grid;
			this.size = grid.size() + 1; // size() is #samples; +1 for headers
			this.pf = pf;

			// examine the size of the page
			rowsPerPage = ((int) pf.getImageableHeight()) / getCellHeight();
			colsPerPage = ((int) pf.getImageableWidth()) / getCellWidth();

			// pagesWide = cols / colsPerPage
			pagesWide = (int) Math.ceil((float) size / colsPerPage);

			// pagesTall = rows / rowsPerPage
			pagesTall = (int) Math.ceil((float) size / rowsPerPage);

			numPages = pagesWide * pagesTall;
		}

		public int getNumberOfPages() {
			return numPages;
		}

		public PageFormat getPageFormat(int pageIndex)
				throws IndexOutOfBoundsException {
			return pf;
		}

		public Printable getPrintable(int pageIndex)
				throws IndexOutOfBoundsException {
			// is this right?  strange...
			if (pageIndex >= numPages)
				throw new IndexOutOfBoundsException();

			int x = pageIndex % pagesWide;
			int y = pageIndex / pagesWide;

			return new GridPage(grid, y * rowsPerPage, y * rowsPerPage
					+ rowsPerPage - 1, x * colsPerPage, x * colsPerPage
					+ colsPerPage - 1);
		}
	}

	// ----------------------------------------

	// external print interface
	public Pageable makeHardcopy(PageFormat pf) {
		return new GridPrinter(this, pf);
	}

	/**
	 Construct a Grid from a List of Elements.  Elements with
	 <code>active=false</code> are ignored.

	 @param elements the List of Elements to use
	 */
	public Grid(ElementList elements) {
		// copy set
		this.elements = elements.toListClassCopy(elements, CachedElement.class);

		// number of active samples in the grid
		// (count-if files #'active)
		num = 0;
		for (Element e : this.elements) 
			if(this.elements.isActive(e))
				num++;

		// create outputs
		cell = new Cell[num + 1][num + 1];
	}

	/**
	 Construct a Grid from an existing file.  Cells are loaded from
	 the previously-calculated values; the user must "refresh" the
	 display (<code>run()</code>) to update these values.

	 @param filename the file to load
	 @exception WrongFiletypeException if this file isn't a Grid
	 @exception FileNotFoundException if the file can't be found
	 @exception IOException if a low-level I/O exception occurs
	 */
	public Grid(String filename) throws WrongFiletypeException,
			FileNotFoundException, IOException {
		// load the file
		load(filename);
	}

	public Grid(Sequence seq) {
		// what to do?
		// copy elements (use seq.getAllFixed(), seq.getAllMoving() -- which are the same here?)
		// -- assume seq.getallfixed==seq.getallmoving
		// NO, DON'T!
		// the right way: add all-fixed, then add all-moving, but with no duplicates (quickly!)

		// fast hash-set for dupe detecting
		Set<String> nodupes = new HashSet<String>();

		elements = new ElementList();		

		// Sequences only contain CachedElements

		// add all fixed
		for(Element e : seq.getAllFixed()) {
			nodupes.add(e.getName());
			elements.add(e);
		}

		// add all (non-duplicate) moving
		for(Element e : seq.getAllMoving()) {
			if(nodupes.contains(e.getName()))
				continue;
			
			nodupes.add(e.getName());
			elements.add(e);
		}
		
		// don't worry about active here; fixed and moving only contain active!

		// create cell array
		num = elements.size();
		cell = new Cell[num + 1][num + 1];
		// (later?) run crosses
		run();
	}

	/**
	 The number of samples in this Grid.  Add one to this value to
	 get the number of cells high or wide the grid is.

	 @return the number of samples in this Grid
	 */
	public int size() {
		return num;
	}

	/**
	 Get a Cell from the grid.

	 @param row the row
	 @param column the column
	 @return the cell at (row, column)
	 */
	public Cell getCell(int row, int column) {
		// (cells are immutable, so this is safe.)
		return cell[row][column];
	}
	
	/** Compute the cells of this grid. */
	public void run() {
		// step 1: load all samples into a buffer.  for reference, on
		// a P3/1000, 256MB, over a 10b2 network (limiting factor?),
		// Win2000, Sun JDK1.3, computing a full grid from PIK's '96
		// Gordion chronology (188 elements) used to take 2min 10sec,
		// but with this buffer takes only 5 seconds.  moral: I/O is
		// really slow, and memory is cheap, so use it!
		
		AvgSingle averages = new AvgSingle();
		Sample buffer[] = new Sample[num];
		int read = 0;
		
		for (Element e : elements) {
			// ABSTRACTION: i'd sure like to grab an enumeration of
			// active elements (well, sort of).  what i really want is
			// a filter.  i'll get rid of the active flag someday, but
			// in the meantime, it'd be useful to have
			//    public static List Element.activeOnly(List)

			// skip inactive elements
			if (elements.isActive(e))
				continue;

			// it's active: try to load
			try {
				buffer[read] = e.load();
				read++;
			} catch (IOException ioe) {
				// ArrayIndexOutOfBoundsException on the next line, on
				// right-click-"grid from all" with no loadable
				// samples.
				buffer[read] = null; // can be null!  doesn't handle below!
				read++;
				error = ioe;
				continue;
			}
		}

		// step 2: compute crosses.  buffer now holds only active
		// elements, so we can just run 0..num-1.  (the array |cell|
		// was created in the constructor)
		for (int row = 0; row < num; row++) {
			// "load" fixed
			Sample fixed = buffer[row];

			// ignore nulls here -- kind of a hack
			if (fixed == null || fixed.getDisplayTitle() == null)
				continue;

			// set headers -- if you want straight-across headers (as
			// opposed to down-the-diagonal headers),
			// s/[row][row+1]/[0][row+1]/
			// String filename = (String) fixed.getMeta("filename");
			cell[row + 1][0] = new HeaderCell(fixed);
			cell[row][row + 1] = new HeaderRangeCell(fixed);

			// set length
			cell[row + 1][row + 1] = new LengthCell(fixed.getData().size());

			for (int col = 0; col < row; col++) {
				// "load" moving
				Sample moving = buffer[col];

				// ignore nulls here -- kind of a hack
				if (moving == null || moving.getDisplayTitle() == null)
					continue;

				// run the single cross, and put it in the grid
				Cell crosscell = new CrossCell(fixed, moving);
				cell[row + 1][col + 1] = crosscell;
				averages.addSingle((Single)crosscell);
			}
		}
		
		averages.calculateAverages();
		// place the "averages" at the first free column on row #2...
		for(int col = 0; col < num + 1; col++) {
			if(cell[1][col] == null) {
				cell[1][col] = averages;
				break;
			}
		}

		// step 3: set all unused grid cells to EmptyCell.  i'm lazy
		// -- let's just look for null cells.  (i really should know a
		// priori what cells will be null here, but after loading /n/
		// samples from disk, looking for nulls is really fast.)
		EmptyCell e = new EmptyCell(); // lots of these: FLYWEIGHT!
		for (int row = 0; row < num + 1; row++)
			for (int col = 0; col < num + 1; col++)
				if (cell[row][col] == null)
					cell[row][col] = e;

		// contents of buffer can now be GC'd.  whew.
	}

	/**
	 Get the error that occurred while computing the grid.  The
	 run() method in Runnable can't throw any exceptions, so we just
	 store them here for later use.

	 @return an Exception, if one occurred, else null
	 */
	public Exception getError() {
		return error;
	}

	// NOTE: the following methods should NOT be made into constants, and
	// the property lookups should NOT be cached.  the user MUST be
	// able to change these without creating a new Grid.
	public static int getCellWidth() {
		// cell width = 140% of cell height
		return (int) (getCellHeight() * 1.4);
	}

	public static int getCellHeight() {
		// make it big enough for 4 lines of text
		int h; // height of a line of text

		// font to use
		Font myFont = App.prefs.getFontPref("corina.grid.font", new Font(
				"sansserif", Font.PLAIN, 12));
		// erp ... this calls new font() for the second arg even when it's not needed (!)

		// i don't think this is quite kosher...  (uh, nope.  fixme.
		// look at its ascent.)
		h = myFont.getSize();
		return 4 * (h + 2 * EPS);
	}

	private static int getLineHeight() {
		return getCellHeight() / 4;
	}

	private static final int EPS = 2; // a wee bit: 2 points (pixels)

	/**
	 A short preview for file dialogs.  Displays "Crossdating Grid",
	 and lists the first few elements.

	 @return a preview component for this grid
	 */
	public Preview getPreview() {
		return new GridPreview(this);
	}

	// a preview for grids
	private static class GridPreview extends Preview {
		GridPreview(Grid g) {
			title = I18n.getText("crossdating_grid");
			items = new ArrayList();
			items.add("(" + g.elements.size() + " " + I18n.getText("total") + ")");

			// up to 5
			for (int i = 0; i < g.elements.size(); i++) {
				if (i == 4 && g.elements.size() > 5) {
					items.add("...");
					break;
				}
				String filename = g.elements.get(i).getName();
				items.add(new File(filename).getName());
			}
		}
	}

	/** A SAX2 handler for loading saved grid files. */
	private class GridHandler extends DefaultHandler {
		private boolean readAnything = false;

		private int row = 0, col = 0; // current row and column

		private EmptyCell e = new EmptyCell(); // flyweight for empty cells

		@Override
		public void startElement(String uri, String name, String qName,
				Attributes atts) throws SAXException {
			System.out.println("startElement");
			// something has been read!  make sure it's a grid
			if (!readAnything) {
				if (name.equals("grid")) {
					readAnything = true;
					return;
				}

				// else
				throw new SAXException("Not a grid!");
				// can't i do better?  wfte?
			}

			// if starting inputs, create list for files
			if (name.equals("input")) {
				elements = new ElementList();
				return;
			}

			// if a sample (input section), add to list
			if (name.equals("sample")) {
				elements.add(ElementFactory.createElement(atts.getValue("filename"), 
						CachedElement.class)); // --> doesn't care about inactive files?
				return;
			}

			// if starting outputs, create array for cells
			if (name.equals("output")) {
				cell = new Cell[num + 1][num + 1];
				return;
			}

			// if an empty cell, use flyweight
			if (name.equals("empty")) {
				cell[row][col] = e;
				return;
			}

			try {
				Cell c = makeCell(name, atts);
				cell[row][col] = c;
			} catch (IllegalArgumentException iae) {
				// ignore -- FIXME: this is just plain awkward
			}
		}

		@Override
		public void endElement(String uri, String name, String qName) {
			System.out.println("endElement");
			// if ending input section, compute num
			if (name.equals("input")) {
				num = elements.size();
				return;
			}

			// if ending a cell, increment row
			// ABSTRACTION: isCellName() -- can be used to avoid IAE's above, and here.
			if (name.equals("empty") || name.equals("header")
					|| name.equals("length") || name.equals("cross")) {
				col++;
				return;
			}

			// if ending a row, reset col=0, and increment row
			if (name.equals("row")) {
				col = 0;
				row++;
				return;
			}
		}
	}

	/**
	 Load a grid, saved in XML format.

	 @param filename the target to load
	 @exception WrongFiletypeException if this file isn't a Grid
	 @exception FileNotFoundException if there is no file by this name
	 @exception IOException if an I/O exception occurs while trying
	 to load
	 */
	public void load(String filename) throws WrongFiletypeException,
			FileNotFoundException, IOException {
		try {
			// make a new XML parser
			XMLReader xr = XMLReaderFactory.createXMLReader();

			// ... configure it to use a my SampleHandler ...
			GridHandler loader = new GridHandler();
			xr.setContentHandler(loader);
			xr.setErrorHandler(loader);

			// ... and feed it the file
			System.out.println("reading " + filename + " as xml");
			FileReader r = new FileReader(filename);
			xr.parse(new InputSource(r));
			System.out.println("done parsing");
		} catch (SAXException se) {
			// no! we don't need to see this garbage!
			//se.printStackTrace();
			throw new WrongFiletypeException();
		}
	}

	/**
	 Save this grid in XML format.

	 @param filename the target to save to
	 @exception IOException if an I/O exception occurs while trying to save
	 */
	public void save(String filename) throws IOException {
		// open, and write header
		BufferedWriter w = new BufferedWriter(new FileWriter(filename));
		try {
			w.write("<?xml version=\"1.0\"?>\n"); // can/should i make the encoding explicit here?
			w.write("\n");
			w.write("<grid>\n");
			w.write("\n");

			// input: filenames
			w.write("  <input>\n");
			for (int i = 0; i < elements.size(); i++) {
				w.write("    <sample filename=\"" + elements.get(i) + "\"/>\n");
			}
			w.write("  </input>\n");
			w.write("\n");

			// output: cells
			w.write("  <output>\n");
			for (int r = 0; r < cell.length; r++) {
				w.write("    <row>\n");
				for (int c = 0; c < cell[r].length; c++)
					w.write("      " + cell[r][c].toXML() + "\n");
				w.write("    </row>\n");
			}
			w.write("  </output>\n");
			w.write("\n");

			// end, and close
			w.write("</grid>\n");
		} finally {
			try {
				w.close();
			} catch (IOException ioe) {
				log.error("Error closing writer", ioe);
			}
		}
	}

	/**
	 Save this grid in CSV format.

	 @param filename the target to save to
	 @exception IOException if an I/O exception occurs while trying to save
	 */
	public void saveCSV(String filename) throws IOException {
		// open, and write header
		BufferedWriter w = new BufferedWriter(new FileWriter(filename));
		try {
			for (int r = 0; r < cell.length; r++) {
				for (int c = 0; c < cell[r].length; c++) {
					String s = cell[r][c].toString();
					
					// if it's a null string, it's an empty cell.
					// We don't have empty cells in the middle of the grid, so this is safe and won't
					// push anything out of alignment.
					if(s == null)
						continue;
				
					if(c != 0)
						w.write(",");
					w.write(StringUtils.escapeForCSV(s));
				}
				w.write("\r\n");
			}
		} finally {
			try {
				w.close();
			} catch (IOException ioe) {
				log.error("Error closing writer", ioe);
			}
		}		
	}

	// this class is used for making our average cell...
	private class AvgSingle extends Single implements Cell {
		private int numCrosses;
		private float cumt, cumtr, cumd, cumr;;
		
		public AvgSingle() {
			super();
			numCrosses = 0;
			cumt = cumtr = cumd = cumr = 0.0f;
		}
		
		public void addSingle(Single cross) {
			// not signifigant, ignore it...
			if(cross.n < 10)
				return;
			
			cumt += cross.t;
			cumtr += cross.tr;
			cumd += cross.d;
			cumr += cross.r;
			numCrosses++;
		}
		
		public void calculateAverages() {
			t = cumt / numCrosses;
			tr = cumtr / numCrosses;
			d = cumd / numCrosses;
			r = cumr / numCrosses;
		}
		
		public void print(Graphics2D g2, int x, int y, int width, int height, float scale) {
			// fill with highlight -- would the user ever NOT want this?  well, yes, possibly.
			// box
			g2.drawRect(x, y, (int) (getCellWidth() * scale),
					(int) (getCellHeight() * scale));

			switch(gridFormat) {
			case GF_1:
				g2.drawString("t=" + formatT() + ", r=" + formatR(), x + EPS, y 
						+ (int) (2 * getLineHeight() * scale) - EPS);
				g2.drawString("tr=" + formatTrend(), x + EPS, y
						+ (int) (3 * getLineHeight() * scale) - EPS);
				g2.drawString("D=" + formatD(), x + EPS, y
						+ (int) (4 * getLineHeight() * scale) - EPS);
				g2.drawString("Averages n>=10", x + EPS, y
						+ (int) (1 * getLineHeight() * scale) - EPS);
				break;
				
			case GF_2:
				g2.drawString("t=" + formatT(), x + EPS, y 
						+ (int) (2 * getLineHeight() * scale) - EPS);
				g2.drawString("tr=" + formatTrend(), x + EPS, y
						+ (int) (3 * getLineHeight() * scale) - EPS);
				g2.drawString("r=" + formatR(), x + EPS, y
						+ (int) (4 * getLineHeight() * scale) - EPS);
				g2.drawString("Averages n>=10", x + EPS, y
						+ (int) (1 * getLineHeight() * scale) - EPS);
				break;
			}
		}				
		
		@Override
		public String toString() {
			return null;
		}
	}

	// for our grid format...
	private final static int GF_1 = 1;
	private final static int GF_2 = 2;
	private int gridFormat = App.prefs.getIntPref("corina.cross.gridformat", 1);
	
	public void setFormat(int format) {
		gridFormat = format;
	}
}
