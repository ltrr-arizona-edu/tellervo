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

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.Pageable;
import java.awt.print.PrinterException;

public class TablePager implements Pageable {

    private class TablePrinter implements Printable {
	private final static int HEADER_HEIGHT = 72;
	private int r0, rn;
	public TablePrinter(int rowMin, int rowMax) {
	    this.r0 = rowMin;
	    this.rn = rowMax;
	}
	public int print(Graphics g, PageFormat pf, int pageIndex)
	    throws PrinterException
	{
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setColor(Color.black);
	    g2.setStroke(new BasicStroke(0.1f));

	    // margins, and column positions
	    final int EPSILON = 2;
	    final int X_MARGIN = ((int) pf.getImageableX());
	    final int Y_MARGIN = ((int) pf.getImageableY());
	    int x1 = (int) (X_MARGIN + 0), // make this an array, and use tablemodel?
		x2 = (int) (X_MARGIN + 72*4.00),
		x3 = (int) (X_MARGIN + 72*4.75),
		x4 = (int) (X_MARGIN + 72*5.50),
		x5 = (int) (X_MARGIN + 72*6.25),
		x6 = (int) (X_MARGIN + 72*7.00); // 72pt == 1"

	    // set font to corina.table.title.font
	    if (System.getProperty("corina.table.title.font") != null)
		g2.setFont(Font.getFont("corina.table.title.font"));

	    // print header
	    g2.drawString(t.toString(),
			  X_MARGIN, Y_MARGIN+ROW_HEIGHT);

	    // set font to corina.table.row.font
	    if (System.getProperty("corina.table.row.font") != null)
		g2.setFont(Font.getFont("corina.table.row.font"));

	    // row titles
	    g2.drawString("Title",	x1+EPSILON, HEADER_HEIGHT-EPSILON);
	    g2.drawString("T-Score",	x2+EPSILON, HEADER_HEIGHT-EPSILON);
	    g2.drawString("Trend",	x3+EPSILON, HEADER_HEIGHT-EPSILON);
	    g2.drawString("D-Score",	x4+EPSILON, HEADER_HEIGHT-EPSILON);
	    g2.drawString("Overlap",	x5+EPSILON, HEADER_HEIGHT-EPSILON);
	    g2.drawString("Distance",	x6+EPSILON, HEADER_HEIGHT-EPSILON);
	    g2.drawLine(X_MARGIN, HEADER_HEIGHT, // x0, y0, x1, y1
			X_MARGIN + (int) pf.getImageableWidth(), HEADER_HEIGHT);

	    // print each row
	    for (int i=r0; i<=rn && i<t.table.size(); i++) {
		Table.Row r = (Table.Row) t.table.get(i);

		int y = HEADER_HEIGHT + ROW_HEIGHT * (1 + i - r0) - EPSILON;

		// draw line/box?

		g2.drawString(r.title,				x1+EPSILON, y);
		g2.drawString(t.f1.format(r.t),			x2+EPSILON, y);
		g2.drawString(t.f2.format(r.tr),		x3+EPSILON, y);
		g2.drawString(t.f3.format(r.d),			x4+EPSILON, y);
		g2.drawString(String.valueOf(r.overlap),	x5+EPSILON, y);
		if (r.dist != null)
		    g2.drawString(r.dist + " km",		x6+EPSILON, y);
	    }

	    return Printable.PAGE_EXISTS;
	}
    }

    private PageFormat pf;
    private Table t;
    private int rows;
    private int rowsPerPage;

    public TablePager(Table t, PageFormat pf) {
	this.t = t;
	this.pf = pf;
	this.rows = t.table.size();

	// examine the size of the page
	int height = (int) getPageFormat(0).getImageableHeight();
	height -= TablePrinter.HEADER_HEIGHT;
	rowsPerPage = height / ROW_HEIGHT;
    }
    public int getNumberOfPages() {
	return (int) Math.ceil((double) rows / rowsPerPage);
    }
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
	return pf;
    }
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
	return new TablePrinter(rowsPerPage*pageIndex,
				rowsPerPage*(pageIndex+1) - 1);
    }

    // use 50% more than needed for just-the-text
    private static final int ROW_HEIGHT = (int) (1.5 * Font.getFont("corina.table.row.font").getSize());

}
