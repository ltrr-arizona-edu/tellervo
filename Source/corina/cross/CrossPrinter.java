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

import corina.Year;

import java.text.DecimalFormat;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Printable;
import java.awt.print.PageFormat;

/*
  left to do:
  - highlight/outline significant scores in all-scores-table
  - compute total pages for "page n of m" in corner?
  --- (wait, i don't know how many pages it'll be!  no, it's easy to just count 'em, right?)
  - print with same sorting as on-screen?
  - print sigs before all?  let user choose?
  - make font/sizes settable?
  - i18n
  - integrate rendering with CrossFrame?
  - integrate with Editor to print samples?
  - bug: quit too soon doesn't finish printing.
  --- (how to solve?  start thread for printing, in "printing" threadgroup, "quit" joins 'em all)
*/

public class CrossPrinter implements Printable {

    // font sizes.  use "serif" for the font: it looks a lot nicer,
    // you can actually see styles like bold text, and postscript
    // knows it so a 2-page crossdate takes 80KB instead of 3MB (and
    // it'll print *much* faster)
    private void big() {
	g2.setFont(new Font("serif", Font.PLAIN, 18));
    }
    private void medium() {
	g2.setFont(new Font("serif", Font.PLAIN, 12));
    }
    private void small() {
	g2.setFont(new Font("serif", Font.PLAIN, 9));
    }
    private void small(boolean bold) {
	g2.setFont(new Font("serif", (bold ? Font.BOLD : Font.PLAIN), 9));
    }

    // page margins
    private double left, right, top, bottom;

    // next pixel row to draw
    private double position;

    // last page of this document, or -1 if not-yet-known
    private int lastPage = -1;

    // the cross to print, and its histogram
    private Cross cross;
    private Histogram h;
    private Histogram.Bucket b[];

    // pct formatter (not user-settable), for histogram only
    private DecimalFormat pctFmt = new DecimalFormat("0.0%");

    // number of rows: scores, sig, distro, total
    private int scoreRows;
    private int sigRows;
    private int distroRows;
    private int totalRows;

    // l10n
    private ResourceBundle msg = ResourceBundle.getBundle("CrossdatingBundle");

    // formatter
    DecimalFormat formatter;

    // a new crossdate printer
    public CrossPrinter(Cross c) {
	cross = c;
	h = new Histogram(c);
	b = h.getBuckets();

	// count some rows
	scoreRows = c.range.getEnd().row() - c.range.getStart().row() + 1;
	sigRows = c.highScores.size();
	distroRows = cross.getBuckets().length;
	totalRows = 1 + 2 + 1 + 1+scoreRows + 1 + 1+sigRows + 1 + 1+distroRows + 1;

	// formatter
	formatter = new DecimalFormat(cross.getFormat());
    }

    // number of rows the last page we printed had.
    private int previousPageRows = 0;

    // print() assigns this right away, and print() is the only public
    // method (the constructor doesn't use it), so we're safe.
    private Graphics2D g2;

    public int print(Graphics g, PageFormat pf, int page) {
	// store margins
	left = pf.getImageableX();
	top = pf.getImageableY();
	right = left + pf.getImageableWidth();
	bottom = top + pf.getImageableHeight();

	// store graphics
	g2 = (Graphics2D) g;

	// start at the top
	position = top;

	// strategy: print lines from start (until position+height>bottom)

	// already done?  say so
	if (lastPage != -1 && page > lastPage)
	    return NO_SUCH_PAGE;

	// what page to draw?
	int startHere = 0;

	// never printed this page before?  then i need to know what's on it...
	int lastKnownPage = startRows.size() - 1;
	if (page > lastKnownPage) {
	    // compute page start/end?  or just draw it for the first time?

	    // where's it start?
	    if (page > 0)
		startHere = ((Integer) startRows.get(lastKnownPage)).intValue() + previousPageRows;
	    startRows.add(new Integer(startHere));
	} else {
	    // print it before, let's try to remember where we started last time
	    startHere = ((Integer) startRows.get(page)).intValue();
	}

	// print the page number (make it 1-indexed for people)
	printPageNr(page+1);

	// now just draw starting with |startHere| until there's no
	// more room
	int n = 0;

	// this checks if the *last* line could fit on this page, not
	// whether the *next* one can, but 99% of the time, it'll be
	// fine.  the line heights are close enough it'll probably
	// never even be noticable.
	while ((position + lineHeight()) < bottom) {

	    // done?
	    if (startHere >= totalRows) {
		lastPage = page;
		return PAGE_EXISTS;
	    }

	    printRow((Graphics2D) g, startHere++);
	    n++;
	}

	// if this is the first time we've drawn this page, better
	// record how many rows it was.  how?
	previousPageRows = n;

	// done
	return PAGE_EXISTS;
    }

    // starting row for page /i/ -- List of Integers
    java.util.List startRows = new ArrayList();

    static final int ROW_EXISTS = 10;
    static final int NO_SUCH_ROW = 36;

    void printPageNr(int page) {
	small();
	String nr = "Page " + page; // of /n/???
	drawStringRight(nr, right);

	// page 1 starts with "T-score" (or whatever the score is), so
	// the top-right corner is free, so we don't need to do
	// anything.  on other pages, there will be data there, so
	// skip down.
	if (page != 1)
	    newLine();
    }

    // print row |row|.  Whatever It Takes.
    void printRow(Graphics2D g, int row) {
	/*
	  what are the rows?
	  - title: Crossdate
	  -   line: Fixed: ...
	  -   line: Moving: ...
	  - section: All Scores
	  -   table: scores
	  - section: Significant Scores
	  -   table: sig scores
	  - section: Distribution of Scores
	  -   table: histogram table
	  - footer: printed by ...
	 */

	if (row == 0)
	    printTitle(cross.getName());
	else if (row == 1)
	    printLabel("Fixed: ", cross.fixed.toString());
	else if (row == 2)
	    printLabel("Moving: ", cross.moving.toString());
	else if (row == 3)
	    printSection("All Scores");
	else if (row <= 4 + scoreRows)
	    printScores(row - 4);
	else if (row == 5 + scoreRows)
	    printSection("Significant Scores");
	else if (row <= 6 + scoreRows + sigRows)
	    printSigs(row - (6 + scoreRows));
	else if (row == 7 + scoreRows + sigRows)
	    printSection("Distribution of Scores");
	else if (row <= 8 + scoreRows + sigRows + distroRows)
	    printDistro(row - (8 + scoreRows + sigRows));
	else if (row == 9 + scoreRows + sigRows + distroRows)
	    printByLine();

	// ... else something's wrong, so ignore it.
    }

    void printTitle(String text) {
	big();
	g2.drawString(text, (float) left, (float) position + lineHeight());
	newLine();
    }

    void printSection(String text) {
	medium();
	g2.drawString(text, (float) left, (float) position + lineHeight());
	newLine();
    }

    // n = 0 is title ("year, 0, 1, ..., 9"), n>0 is row n-1
    void printScores(int n) {
	small();
	float colWidth = (float) ((right - left) / 11.);
	if (n == 0) {
	    String yr = "Year";
	    drawStringRight(yr, left+colWidth);

	    for (int i=0; i<10; i++) {
		drawStringRight(String.valueOf(i), left+colWidth*(i+2));
	    }
	} else {
	    // column 0: the decade
	    int row_min = cross.range.getStart().row();
	    String decade=null;
	    if (n == 1)
		decade = cross.range.getStart().toString();
	    else if (n-1 + row_min == 0)
		decade = "1"; // special case
	    else
		decade = (new Year(10 * (n-1 + row_min))).toString();
	    drawStringRight(decade, left+colWidth);

	    // rest of the columns
	    for (int i=0; i<10; i++) {
		String val="";

		Year y = new Year(10 * (n-1 + row_min) + i);
		if (!cross.range.contains(y))
		    continue;
		else
		    val = formatter.format(cross.data[y.diff(cross.range.getStart())]);

		drawStringRight(val, left+colWidth*(i+2));
	    }
	}

	newLine();
    }

    // n = 0 is title ("no, fixed, moving, t-score, overlap"), n>0 is row n-1
    void printSigs(int n) {
	small();
	float colWidth = (float) ((right - left) / 5.);
	if (n == 0) {
	    String headers[] = new String[] { "No.", "Fixed", "Moving", "T-Score", "Overlap" };
	    for (int i=0; i<headers.length; i++) {
		drawStringRight(headers[i], left+colWidth*(i+1));
	    }
	} else {
	    // straight from CrossFrame's getValueAt():
	    int row = n-1;
	    String vals[] = new String[] {
		String.valueOf(((Score) cross.highScores.get(row)).number),
		((Score) cross.highScores.get(row)).fixedRange.toString(),
		((Score) cross.highScores.get(row)).movingRange.toString(),
		formatter.format(((Score) cross.highScores.get(row)).score),
		String.valueOf(((Score) cross.highScores.get(row)).span),
	    };

	    for (int i=0; i<5; i++) {
		drawStringRight(vals[i], left+colWidth*(i+1));
	    }
	}

	newLine();
    }

    // n = 0 is title ("t-score, number, percent, histogram"), n>0 is row n-1
    void printDistro(int n) {
	small();
	float colWidth = (float) ((right - left) / 4.);
	if (n == 0) {
	    String headers[] = new String[] { cross.getName(), "Number", "Percent", "Histogram" };
	    for (int i=0; i<headers.length; i++) {
		drawStringRight(headers[i], left+colWidth*(i+1));
	    }
	} else {
	    int row = n-1;

	    // col 0: score
	    String score = formatter.format(b[row].low) + " - " + formatter.format(b[row].high);
	    g2.drawString(score, (float) left, (float) position+lineHeight());

	    // col 1: number (right-aligned), but only if >0
	    if (b[row].number > 0) {
		String number = String.valueOf(b[row].number);
		drawStringRight(number, left+colWidth*2);

		// col 2: percent (right-aligned?), but also only if >0
		String pct = pctFmt.format(b[row].pct);
		drawStringRight(pct, left+colWidth*3);

		// col 3: histogram -- only if n>0?
		int x = (int) (left + colWidth*3) + 72/4;
		int y = (int) (position + lineHeight()); // not /2?  no, that's full  height, incl. leading, etc.  FIXME
		int dx = (int) (((double) b[row].number / (double) cross.data.length) * (double) colWidth);
		g2.drawLine(x, y, x+dx, y);
	    }
	}

	newLine();
    }

    // used for "Fixed:" and "Moving:" labels at the top
    void printLabel(String label, String value) {
	small(true);
	drawStringRight(label, left+72);

	small();
	g2.drawString(value, (float) left + 72, (float) position + lineHeight());

	newLine();
    }

    // print a "printed by ... at ..." line.  BROKEN.
    void printByLine() {
	small();
	String byline = "Printed by " + System.getProperty("user.name") + ", " + new Date();
	g2.drawString(byline, (float) left, (float) position + lineHeight());
	newLine();
    }

    // compute the line height for the current font.  (in C, this
    // would be a macro.)
    private int lineHeight() {
	return g2.getFontMetrics().getHeight();
    }

    // draw some text to the left and below (right,position)
    private void drawStringRight(String s, double right) {
	int width = g2.getFontMetrics().stringWidth(s);
	g2.drawString(s, (float) right - width, (float) position + lineHeight());
    }

    // increment position by lineheight
    private void newLine() {
	position += lineHeight();
    }

}
