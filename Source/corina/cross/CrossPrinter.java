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
import corina.gui.Bug;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.MessageFormat;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.print.Printable;
import java.awt.print.PageFormat;

/*
 left to do:
 - highlight/outline significant scores in all-scores-table
 - compute total pages for "page n of m" in corner?
 --- (wait, i don't know how many pages it'll be!  no, it's easy to just count 'em, right?)
 - print with same sorting as on-screen?
 - let user choose which to print (all/sigs/histo)
 - add space between sections
 - start sections on new pages
 - make font/sizes settable?
 - integrate rendering with CrossFrame?  (don't bother)
 - integrate with Editor to print samples?  (later)
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
    private Histogram histo;

    // number of rows: scores, sig, distro, total
    private int scoreRows;
    private int sigRows;
    private int distroRows;
    private int totalRows;

    // l10n
    private ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

    // formatter
    DecimalFormat formatter;

    // a new crossdate printer with everything (still needed?)
    public CrossPrinter(Cross c) {
        this(c, true, true, true);
    }

    // a new crossdate printer, with user-selected stuff
    public CrossPrinter(Cross c, boolean sigScores, boolean allScores, boolean histogram) {
        cross = c;
        histo = new Histogram(c); // isn't this part of cross?  (well, it should be...)

        // count some rows
        scoreRows = c.range.getEnd().row() - c.range.getStart().row() + 1;
        sigRows = c.highScores.size();
        distroRows = histo.countBuckets();
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
    List startRows = new ArrayList();

    static final int ROW_EXISTS = 10;
    static final int NO_SUCH_ROW = 36;

    void printPageNr(int page) {
        small();
        String nr = msg.getString("page") + " " + page; // of /n/???
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
            printLabel(msg.getString("fixed") + ": ", cross.fixed.toString());
        else if (row == 2)
            printLabel(msg.getString("moving") + ": ", cross.moving.toString());
        else if (row == 3)
            printSection(msg.getString("all_scores"));
        else if (row <= 4 + scoreRows)
            printScores(row - 4);
        else if (row == 5 + scoreRows)
            printSection(msg.getString("sig_scores"));
        else if (row <= 6 + scoreRows + sigRows)
            printSigs(row - (6 + scoreRows));
        else if (row == 7 + scoreRows + sigRows)
            printSection(msg.getString("histogram"));
        else if (row <= 8 + scoreRows + sigRows + distroRows)
            printHisto(row - (8 + scoreRows + sigRows));
        else if (row == 9 + scoreRows + sigRows + distroRows)
            printByLine();

        // ... else something's wrong, so ignore it.  nah, let's bug the user.
        Bug.bug(new IllegalArgumentException("eep."));
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
            String yr = msg.getString("year");
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
                Year y = new Year(10 * (n-1 + row_min) + i); // DUPLICATE CODE FROM SOMEWHERE!
                if (cross.range.contains(y)) {
                    String val = formatter.format(cross.data[y.diff(cross.range.getStart())]);
                    drawStringRight(val, left+colWidth*(i+2));
                }
            }
        }

        newLine();
    }

    // n = 0 is title ("no, fixed, moving, t-score, overlap"), n>0 is row n-1
    void printSigs(int n) {
        small();
        float colWidth = (float) ((right - left) / 5.);
        if (n == 0) {
            String headers[] = new String[] {
                msg.getString("number"),
                msg.getString("fixed"),
                msg.getString("moving"),
                cross.getName(),
                msg.getString("overlap") };
            for (int i=0; i<headers.length; i++) {
                drawStringRight(headers[i], left+colWidth*(i+1));
            }
        } else {
            // straight from CrossFrame's getValueAt():
            int row = n-1;
            Score s = (Score) cross.highScores.get(row);
            String vals[] = new String[] {
                String.valueOf(s.number),
                s.fixedRange.toString(),
                s.movingRange.toString(),
                formatter.format(s.score),
                String.valueOf(s.span),
            };

            for (int i=0; i<5; i++) {
                drawStringRight(vals[i], left+colWidth*(i+1));
            }
        }

        newLine();
    }

    // n = 0 is title ("t-score, number, histogram"), n>0 is row n-1
    void printHisto(int n) {
        small();
        int rangeGuide = (int) (left + (float) ((right - left) * 0.15)); // center guide for ranges
        int histoGuide = (int) (left + (float) ((right - left) * 0.50)); // left guide for histo
        int qtyGuide = (rangeGuide + histoGuide) / 2; // center guide for qtys
        if (n == 0) {
            // header
            drawStringCenter(cross.getName(), rangeGuide);
            drawStringCenter(msg.getString("quantity"), qtyGuide);
            drawStringCenter(msg.getString("histogram"), (int) ((histoGuide+right)/2));
            
        } else {
            int row = n-1;

            // col 0: score
            String score = histo.getRange(row);
            drawStringCenter(score, rangeGuide);

            // col 1: number (right-aligned), but only if >0
            if (histo.getNumber(row) > 0) {
                String number = String.valueOf(histo.getNumber(row));
                drawStringCenter(number, qtyGuide);

                // col 2: histogram -- only if n>0
                g2.setStroke(new BasicStroke(0.5f));
                int ascent = g2.getFontMetrics().getAscent();
                int y = (int) (position + lineHeight() - ascent);
                double frac = (double) histo.getNumber(row) / (double) histo.getFullestBucket();
                int dx = (int) (frac * (right - histoGuide));
                g2.drawRect((int) histoGuide, y, dx, lineHeight());
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

    // print a "printed by ... at ..." line.
    private String makeByLine() {
        Date date = new Date();
        String dateString = DateFormat.getDateInstance().format(date);
        String timeString = DateFormat.getTimeInstance().format(date);
        String byline = MessageFormat.format(msg.getString("printed_by"),
                                             new Object[] {
                                                 System.getProperty("user.name"),
                                                 dateString,
                                                 timeString,
                                             });
        return byline;
    }

    void printByLine() {
        // draw on very bottom of page
        small();
        g2.drawString(makeByLine(), (float) left, (float) bottom - lineHeight());
        newLine(); // (does this do anything now?)
    }

    // compute the line height for the current font.  (in C, this would be a macro.)
    private int lineHeight() {
        return g2.getFontMetrics().getHeight();
    }

    // draw some text to the right and below (left,position)
    private void drawStringLeft(String s, double left) {
        g2.drawString(s, (float) left, (float) position + lineHeight());
    }

    // draw some text to the left and below (right,position)
    private void drawStringRight(String s, double right) {
        int width = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, (float) right - width, (float) position + lineHeight());
    }
    
    // draw some text centered below (center,position)
    private void drawStringCenter(String s, double center) {
        int width = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, (float) center - width/2, (float) position + lineHeight());
    }
    
    // increment position by lineheight
    private void newLine() {
        position += lineHeight();
    }
}
