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

import corina.Range;
import corina.Sample;
import corina.Element;
import corina.Preview;
import corina.Previewable;
import corina.files.WrongFiletypeException;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.text.DecimalFormat;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

/**
   A crossdating grid.  An N-by-N grid of samples (files), where
   column <i>i</i> row <i>j</i> holds the scores (and any other
   requested information, like the overlap) when crossing file
   <i>i</i> against file <i>j</i>.  The top half of the grid is empty
   (it would be the same as the lower half), and the diagonal has, by
   convention, the length of sample <i>i</i> in the <i>i</i>th
   diagonal cell.

   A Grid might look similar to this when printed:<p>

   <table border="0" padding="0" spacing="0" bgcolor="lightblue">
   <tr><td>

   <table border="1">

	<tr><td></td><td>SPI2A.IND</td><td></td><td></td></tr>

	<tr><td>SPI2A.IND</td><td>n=54</td><td>SPI3A.IND</td><td></td></tr>

	<tr><td>SPI3A.IND</td><td>
		t=0.00<br>
		tr=47.2%<br>
		d=0.00<br>
		n=54
	</td><td>n=170</td><td>SPI4A.IND</td></tr>

	<tr><td>SPI4A.IND</td><td>
		t=0.22<br>
		tr=67.2%<br>
		d=0.03<br>
		n=52
	</td><td>
		t=1.63<br>
		tr=55.6%<br>
		d=0.09<br>
		n=55
	</td><td>n=55</td></tr>

   </table>

   </tr></td>
   </table>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class Grid implements Runnable, Previewable {

    // inputs
    private List files;
    private int num; // number of active files

    // outputs
    private Cell cell[][];
    private Exception error=null;

    // formatters
    private static DecimalFormat f1, f2, f3;
    static {
	f1 = new DecimalFormat(new TScore().getFormat());
	f2 = new DecimalFormat(new Trend().getFormat());
	f3 = new DecimalFormat(new DScore().getFormat());
    }

    // i18n
    private static ResourceBundle msg = ResourceBundle.getBundle("CrossdatingBundle");

    // (used when creating graph of these samples)
    public List getFiles() {
	return files;
    }

    // ----------------------------------------
    public interface Cell {
	public abstract void print(Graphics2D g2, int x, int y, double scale);
	public abstract String toXML();
    }
    public class EmptyCell implements Cell {
	public void print(Graphics2D g2, int x, int y, double scale) {
	    // do nothing
	}
	public String toXML() {
	    return "<empty/>";
	}
    }
    public class HeaderCell implements Cell {
	private String name;
	public HeaderCell(String name) {
	    // crop directory
	    int index = name.lastIndexOf(File.separatorChar);
	    this.name = name.substring(index+1);
	}
	public void print(Graphics2D g2, int x, int y, double scale) {
	    // filename
	    g2.drawString(name, x+EPSILON, y+(int)((getCellHeight()/2-getLineHeight()/2)*scale));
	}
	public String toXML() {
	    return "<header name=\"" + name + "\"/>";
	}
    }
    public class HeaderRangeCell implements Cell {
	private String name;
	private Range range;
	public HeaderRangeCell(String name, Range range) {
	    // crop directory
	    int index = name.lastIndexOf(File.separatorChar);
	    this.name = name.substring(index+1);
	    this.range = range;
	}
	public void print(Graphics2D g2, int x, int y, double scale) {
	    // filename
	    g2.drawString(name, x+EPSILON, y+(int)((getCellHeight()/2)*scale));

	    // range
	    g2.drawString(range.toString(), x+EPSILON, y+(int)((getCellHeight()/2 + getLineHeight())*scale));
	}
	public String toXML() {
	    return "<header name=\"" + name + "\" range=\"" + range + "\"/>";
	}
    }
    public class CrossCell implements Cell {
	private double t, tr, d;
	private int n;
	private boolean isSig;
	public CrossCell(double t, double tr, double d, int n) {
	    this.t = t;
	    this.tr = tr;
	    this.d = d;
	    this.n = n;

	    // this isn't great: the is-significant function is
	    // hardcoded.  users should be able to decide what tests
	    // to use for significance: t, tr, d, n, or any
	    // combination.  (creating a dud TScore object for every
	    // cell isn't great, either.)
	    isSig = (t > new TScore().getMinimumSignificant());
	}
	public void print(Graphics2D g2, int x, int y, double scale) {
	    // fill with highlight.
	    if (Boolean.getBoolean("corina.grid.highlight") && isSig) {
		Color oldColor = g2.getColor();
		g2.setColor(Color.getColor("corina.grid.highlightcolor"));
		g2.fillRect(x, y, (int) (getCellWidth()*scale), (int) (getCellHeight()*scale));
		g2.setColor(oldColor);
	    }

	    // box
	    g2.drawRect(x, y, (int) (getCellWidth()*scale), (int) (getCellHeight()*scale));

	    // little/no overlap: just show the overlap
	    if (n < Cross.OVERLAP) {
		g2.drawString("n=" + n, x+EPSILON, y+(int)((getCellHeight()/2-getLineHeight()/2)*scale));
		return;
	    }

	    // cross
	    g2.drawString("t=" + f1.format(t), x+EPSILON, y+(int)(getLineHeight()*scale)-EPSILON);
	    g2.drawString("tr=" + f2.format(tr), x+EPSILON, y+(int)(2*getLineHeight()*scale)-EPSILON);
	    g2.drawString("D=" + f3.format(d), x+EPSILON, y+(int)(3*getLineHeight()*scale)-EPSILON);
	    g2.drawString("n=" + String.valueOf(n), x+EPSILON, y+(int)(4*getLineHeight()*scale)-EPSILON);
	}
	// in toXML, store full precision, with no %'s -- this means
	// later we won't have too few digits, if the user decides she
	// wants more, and we won't have to worry about parsing it
	// incorrectly with NumberFormat.parse().  the users never
	// need to look at a *.grid file, either, so they won't care.
	public String toXML() {
	    return "<cross t=\"" + t + "\" tr=\"" +
		                   tr + "\" d=\"" +
		                   d + "\" n=\"" + n + "\"/>";
	}
    }
    public class LengthCell implements Cell {
	private int length;
	LengthCell(int length) {
	    this.length = length;
	}
	public void print(Graphics2D g2, int x, int y, double scale) {
	    // box? -- no box for you!  (the box nazi, of course.)

	    // length
	    g2.drawString("n=" + length, x+EPSILON, y+(int)((getCellHeight()/2-getLineHeight()/2)*scale));
	}
	public String toXML() {
	    return "<length n=\"" + length + "\"/>";
	}
    }
    // ----------------------------------------

    // ----------------------------------------
    // print one page of a grid
    private static class GridPage implements Printable {
	private PageFormat pf;
	private Grid grid;
	private int startRow, endRow, startCol, endCol;
	public GridPage(PageFormat pf, Grid grid,
			int startRow, int endRow,
			int startCol, int endCol) {
	    this.pf = pf;
	    this.grid = grid;
	    this.startRow = startRow;
	    this.endRow = endRow;
	    this.startCol = startCol;
	    this.endCol = endCol;
	}
	public int print(Graphics g, PageFormat pf, int pageNr)
	    throws PrinterException
	{
	    // ASSUME: pageIndex==0, because single GridPages are
	    // created by GridPager.

	    Graphics2D g2 = (Graphics2D) g;
	    g2.setColor(Color.black);
	    g2.setStroke(new BasicStroke(0.1f)); // what's a good thickness?

	    // set font (for all cells)
	    if (System.getProperty("corina.grid.font") != null)
		g2.setFont(Font.getFont("corina.grid.font"));

	    // figure out stop row, col: end of page, or end of grid,
	    // whichever comes first
	    int stopRow = Math.min(endRow, grid.cell.length-1);
	    int stopCol = Math.min(endCol, grid.cell[0].length-1);

	    // print each one
	    for (int x=startCol; x<=stopCol; x++) {
		for (int y=startRow; y<=stopRow; y++) {
		    Cell c = grid.cell[y][x];
		    c.print(g2,
			    ((int) pf.getImageableX()) +
			                 (x-startCol)*getCellWidth(),
			    ((int) pf.getImageableY()) +
			                 (y-startRow)*getCellHeight(),
			    1.0); // always print to paper with scale=1.0
		}
	    }

	    return Printable.PAGE_EXISTS;
	}
    }

    // ----------------------------------------
    // print all pages of a grid (using GridPage)
    private static class GridPrinter implements Pageable {
	private Grid grid;
	private int size;
	private int rowsPerPage, colsPerPage;
	private PageFormat pf;
	public GridPrinter(Grid grid, PageFormat pf) {
	    this.grid = grid;
	    this.size = grid.size() + 1;
	    this.pf = pf;

	    // examine the size of the page
	    rowsPerPage = ((int) getPageFormat(0).getImageableHeight()) /
		             getCellHeight();
	    colsPerPage = ((int) getPageFormat(0).getImageableWidth()) /
		             getCellWidth();
	}
	public int getNumberOfPages() {
	    // numPages = (cols / colsPerPage) * (rows / rowsPerPage)
	    int n = (int) Math.ceil((double) size / colsPerPage) *
		    (int) Math.ceil((double) size / rowsPerPage);
	    return n;
	}
	public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
	    return pf;
	}
	public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
	    int pagesWide = (int) Math.ceil((double) size / (double) colsPerPage);

	    int x = pageIndex % pagesWide;
	    int y = pageIndex / pagesWide;

	    return new GridPage(pf, grid,
				y*rowsPerPage, y*rowsPerPage + rowsPerPage-1,
				x*colsPerPage, x*colsPerPage + colsPerPage-1);
	}
    }
    // ----------------------------------------

    // external print interface
    public Pageable makeHardcopy(PageFormat pf) {
	return new GridPrinter(this, pf);
    }

    /** Construct a Grid from a List of Elements.  Elements with
	<code>active=false</code> are ignored.
	@param elements the List of Elements to use */
     public Grid(List elements) {
	// copy set
	files = elements;

	// number of active samples in the grid
	num = 0;
	for (int i=0; i<files.size(); i++)
	    if (((Element) files.get(i)).active)
		num++;

	// create outputs
	cell = new Cell[num+1][num+1];
    }

    /** Construct a Grid from an existing file.  Cells are loaded from
        the previously-calculated values; the user must "refresh" the
        display (<code>run()</code>) to update these values.
	@param filename the file to load
	@exception WrongFiletypeException if this file isn't a Grid
	@exception FileNotFoundException if the file can't be found
	@exception IOException if a low-level I/O exception occurs */
    public Grid(String filename) throws WrongFiletypeException,
                                        FileNotFoundException,
					IOException {
	// load the file
	load(filename);
    }

    /** The number of samples in this Grid.  Add one to this value to
	get the number of cells high or wide the grid is.
	@return the number of samples in this Grid */
    public int size() {
	return num;
    }

    /** Get the 2-dimensional array of Cells that make up this Grid.
	@return the Cells */
    public Cell[][] getCells() {
	return cell;
    }

    /** Compute the cells of this grid. */
    public void run() {
	// step 1: load all samples into a buffer.  for reference, on
	// a P3/1000, 256MB, over a 10b2 network (limiting factor),
	// Win2000, Sun JDK1.3, computing a full grid from PIK's '96
	// Gordion chronology (188 elements) used to take 2min 10sec,
	// but with this buffer takes only 5 seconds.  moral: I/O is
	// REALLY SLOW, and memory is cheap!  use it!
	Sample buffer[] = new Sample[num];
	int read=0;
	for (int i=0; i<files.size(); i++) {
	    // skip inactive elements
	    if (!((Element) files.get(i)).active)
		continue;

	    // it's active: try to load
	    try {
		buffer[read] = ((Element) files.get(i)).load();
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
	for (int row=0; row<num; row++) {
	    // "load" fixed
	    Sample fixed = buffer[row];

	    // set headers -- if you want straight-across headers (as
	    // opposed to down-the-diagonal headers),
	    // s/[row][row+1]/[0][row+1]/
	    String filename = (String) fixed.meta.get("filename");
	    cell[row+1][0] = new HeaderCell(filename);
	    cell[row][row+1] = new HeaderRangeCell(filename, fixed.range);

	    // set length
	    cell[row+1][row+1] = new LengthCell(fixed.data.size());

	    for (int col=0; col<row; col++) {
		// "load" moving
		Sample moving = buffer[col];

		int n = fixed.range.overlap(moving.range);
		if (n > 0) {
		    TScore _t = new TScore(fixed, moving);
		    Trend _tr = new Trend(fixed, moving);
		    DScore _d = new DScore(_t, _tr);

		    double t = _t.single();
		    double tr = _tr.single();
		    double d = _d.single();

		    cell[row+1][col+1] = new CrossCell(t, tr, d, n);
		} else {
		    cell[row+1][col+1] = new CrossCell(0.0, 0.0, 0.0, 0);
		}
	    }
	}

	// step 3: set all unused grid cells to EmptyCell.  i'm lazy
	// -- let's just look for null cells.  (i really should know a
	// priori what cells will be null here, but after loading /n/
	// samples from disk, looking for nulls is really fast.)
	EmptyCell e = new EmptyCell(); // lots of these: FLYWEIGHT!
	for (int row=0; row<num+1; row++)
	    for (int col=0; col<num+1; col++)
		if (cell[row][col] == null)
		    cell[row][col] = e;

	// contents of buffer can now be GC'd.  whew.
    }

    /** Get the error that occurred while computing the grid.  The
	run() method in Runnable can't throw any exceptions, so we
	just store them here for later use.
	@return an Exception, if one occurred, else null */
    public Exception getError() {
	return error;
    }

    // the following methods should NOT be made into constants, and
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
	Font myFont = Font.getFont("corina.grid.font", new Font("sansserif", Font.PLAIN, 12));

	// i don't think this is quite kosher...
	h = myFont.getSize();
	return  4*(h + 2*EPSILON);
    }
    private static int getLineHeight() {
	return getCellHeight() / 4;
    }

    private static final int EPSILON = 2; // a wee bit: 2 points (pixels)

    /** A short preview for file dialogs.  Displays "Crossdating
	Grid", and lists the first few elements.
	@return a preview component for this grid */
    public String getHTMLPreview() {
	// "Crossdating grid" (they don't have titles, nor will they:
	// that's what filenames are for)
	String preview = "<html><b>" + msg.getString("crossdating_grid") + "</b>";

	// number of elements
	preview += "<p>(" + files.size() + " " + msg.getString("total") + ")<ul>";

	// list up to 5
	for (int i=0; i<files.size(); i++) {
	    if (i == 4 && files.size() > 5) {
		preview += "<li>...</ul>";
		break;
	    }
	    preview += "<li>" + new File(((Element) files.get(i)).filename).getName();
	}

	return preview;
    }

    public Preview getPreview() {
	Preview p = new Preview();

	p.title = msg.getString("crossdating_grid");
	p.items = new ArrayList();
	p.items.add(files.size() + " " + msg.getString("total") + ")");

	// up to 5
	for (int i=0; i<files.size(); i++) {
	    if (i==4 && files.size()>5) {
		p.items.add("...");
		break;
	    }
	    p.items.add(new File(((Element) files.get(i)).filename).getName());
	}

	return p;
    }

    /** A SAX2 handler for loading saved grid files. */
    public class GridHandler extends DefaultHandler {
	boolean readAnything=false, isGrid=false;
	int row=0, col=0; // current row and column
	EmptyCell e=new EmptyCell(); // flyweight for empty cells
	public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
	    // something has been read!  make sure it's a grid
	    if (!readAnything) {
		if (!name.equals("grid"))
		    throw new SAXException("Not a grid!"); // can't i do better?  wfte?
		readAnything = true;
	    }

	    // if starting inputs, create list for files
	    if (name.equals("input")) {
		files = new ArrayList();
		return;
	    }

	    // if a sample (input section), add to list
	    if (name.equals("sample")) {
		files.add(new Element(atts.getValue("filename"))); // --> doesn't care about inactive files?
		return;
	    }

	    // if starting outputs, create array for cells
	    if (name.equals("output")) {
		cell = new Cell[num+1][num+1];
		return;
	    }

	    // if an empty cell, use flyweight
	    if (name.equals("empty")) {
		cell[row][col] = e;
		return;
	    }

	    // if a header cell...
	    if (name.equals("header")) {
		String r = atts.getValue("range"); // check for range="a-b"
		if (r != null)
		    cell[row][col] = new HeaderRangeCell(atts.getValue("name"), new Range(r));
		else 
		    cell[row][col] = new HeaderCell(atts.getValue("name"));
		return;
	    }

	    // if a length cell...
	    if (name.equals("length")) {
		cell[row][col] = new LengthCell(Integer.parseInt(atts.getValue("n")));
		return;
	    }

	    // if a cross cell...
	    if (name.equals("cross")) {
		cell[row][col] = new CrossCell(Double.parseDouble(atts.getValue("t")),
					       Double.parseDouble(atts.getValue("tr")),
					       Double.parseDouble(atts.getValue("d")),
					       Integer.parseInt(atts.getValue("n")));
		return;
	    }
	}
	public void endElement(String uri, String name, String qName) {
	    // if ending input section, compute num
	    if (name.equals("input")) {
		num = files.size();
		return;
	    }

	    // if ending a cell, increment row
	    if (name.equals("empty") || name.equals("header") ||
		name.equals("length") || name.equals("cross")) {
		col++;
	    }

	    // if ending a row, reset col=0, and increment row
	    if (name.equals("row")) {
		col = 0;
		row++;
		return;
	    }
	}
    }

    /** Load a grid, saved in XML format.
	@param filename the target to load
	@exception WrongFiletypeException if this file isn't a Grid
	@exception FileNotFoundException if there is no file by this name
	@exception IOException if an I/O exception occurs while trying to load */
    public void load(String filename) throws WrongFiletypeException,
                                             FileNotFoundException,
					     IOException {
	try {
	    // make a new XML parser
	    XMLReader xr = XMLReaderFactory.createXMLReader();

	    // ... configure it to use a my SampleHandler ...
	    GridHandler loader = new GridHandler();
	    xr.setContentHandler(loader);
	    xr.setErrorHandler(loader);

	    // ... and feed it the file
	    FileReader r = new FileReader(filename);
	    xr.parse(new InputSource(r));
	} catch (SAXException se) {
	    throw new WrongFiletypeException();
	}
    }

    /** Save this grid in XML format.
	@param filename the target to save to
	@exception IOException if an I/O exception occurs while trying to save */
    public void save(String filename) throws IOException {
	// open for writing
	BufferedWriter w = new BufferedWriter(
			       new FileWriter(filename));

	// XML header
	w.write("<?xml version=\"1.0\"?>");
	w.newLine();

	w.newLine();

	// begin grid
	w.write("<grid>");
	w.newLine();

	w.newLine();

	// input: filenames
	w.write("  <input>");
	w.newLine();
	for (int i=0; i<files.size(); i++) {
	    w.write("    <sample filename=\"" + files.get(i) + "\"/>");
	    w.newLine();
	}
	w.write("  </input>");
	w.newLine();

	w.newLine();

	// output: cells
	w.write("  <output>");
	w.newLine();

	// a row
	for (int r=0; r<cell.length; r++) {
	    w.write("    <row>");
	    w.newLine();

	    // cells in that row
	    for (int c=0; c<cell[r].length; c++) {
		w.write("      " + cell[r][c].toXML());
		w.newLine();
	    }

	    w.write("    </row>");
	    w.newLine();
	}

	w.write("  </output>");
	w.newLine();

	w.newLine();

	// end grid
	w.write("</grid>");
	w.newLine();

	// close
	w.close();
    }
}
