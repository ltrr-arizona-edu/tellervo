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

package corina.print;

import java.awt.print.Printable;
import java.awt.print.PageFormat;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.List;
import java.util.ArrayList;

/**
   A class which produces (AWT) Printables from (Corina)
   PrintableDocuments.

   <p>It's just a bridge: all of the lines you add to the
   <code>lines</code> List get printed.</p>

    <h2>Left to do:</h2>
    <ul>
        <li>If this was a Pageable instead of a Printable, then
        the user could see how many pages will be printed before
        actually printing them.  (See end of source file.)
    </ul>

   @see java.awt.print.Pageable

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public abstract class Printer implements Printable {

    /** Subclasses should add lines to this. */
    protected List lines = new ArrayList();

    /** Construct a new Printer, with no lines. */
    public Printer() {
	// not needed, except as a place to add a javadoc tag
    }

    // lines[firstRowOfPage[0]...firstRowOfPage[1]-1]
    // make up the first page, etc.  null = haven't paginated yet.
    private List firstRowOfPage = null;

    // borders of page
    private float top, left, bottom, right;

    /**
       Print (a page of) this document.  Users don't need to use this:
       it's the Printable interface, which the Java printing system
       calls.

       @param g the Graphics object to draw it to
       @param pf the PageFormat to use
       @param page which page of the document to print
    */
    public int print(Graphics g, PageFormat pf, int page) {
	// store margins
	left = (float) pf.getImageableX();
	right = left + (float) pf.getImageableWidth();
	top = (float) pf.getImageableY();
	bottom = top + (float) pf.getImageableHeight();

	// grab space at bottom for page numbers
	bottom -= g.getFontMetrics().getHeight();

	// never printed before ... paginate
	if (firstRowOfPage == null)
	    paginate(g);

	// not a real page
	if (page >= firstRowOfPage.size())
	    return NO_SUCH_PAGE;

	// print rows of page
	printPage(g, page, pf);

	// print page number
	printPageNumber(g, page);

	// all done.
	return PAGE_EXISTS;
    }

    // paginate: go through all the lines, and figure out
    // where to put the page breaks
    private void paginate(Graphics g) {
	// row 0 starts the first page
	firstRowOfPage = new ArrayList();
	firstRowOfPage.add(new Integer(0));

	// distance from the top of the page that i am.
	float ruler = top;

	for (int row=0; row<lines.size(); row++) {
	    Line line = (Line) lines.get(row);

	    if (ruler + line.height(g) > bottom) {
		firstRowOfPage.add(new Integer(row));
		ruler = top;
	    }

	    ruler += line.height(g);
	}
    }

    // print all the rows of this page
    private void printPage(Graphics g, int page,
			   PageFormat pf) {
	float position = top;
	int row = ((Integer) firstRowOfPage.get(page)).intValue();
	while (row < lines.size()) {
	    Line l = (Line) lines.get(row);

	    if (position + l.height(g) > bottom)
		return;

	    l.print(g, pf, position);
	    position += l.height(g);
	    row++;
	}
    }

    // print a page number
    private void printPageNumber(Graphics g, int page) {
	String pageNum = String.valueOf(page + 1);
	Graphics2D g2 = (Graphics2D) g;
	g2.setFont(Line.NORMAL);
	int pageNumWidth = g2.getFontMetrics().stringWidth(pageNum);
	int pageNumHeight = g2.getFontMetrics().getHeight();
	g2.drawString(pageNum,
		      (right+left)/2 - pageNumWidth/2,
		      bottom + pageNumHeight);
    }

    /*
      stuff about pageable, as promised:

      -- Pageables can find out the PageFormat before having to report
         the number of pages (for example, see Grid.GridPrinter)

      -- basically, getNumberOfPages() needs to call paginate(), but
         then it's fine

      -- paginate() assumes storeMargins() has been called, which
         needs the PageFormat

      -- structure: Printer extends Pageable; inner PagePrinter
         extends Printable

      -- so the only real issue is getting a Graphics object for
         paginate()

      -- can i just take a Graphics from a BufferedImage, and say
         g2.transform(pf.getMatrix())?

      -- (i'd also need to change something about the interface here,
         too, since i don't take a PageFormat on construction, yet.)
    */
}
