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
import corina.print.Line;
import corina.print.EmptyLine;
import corina.print.TextLine;
import corina.print.ThinLine;
import corina.print.ByLine;
import corina.print.TabbedLineFactory;
import corina.print.Printer;
import corina.ui.I18n;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.MessageFormat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.print.Printable;
import java.awt.print.PageFormat;

// TODO:
// -- refactor lineWithBar (formerly HistoLine)
// -- get rid of ScoresLine: use TLF instead
// -- the fixed and moving columns (at ***)
// should respect the user's current selection
// (fixed-as-fixed, etc.) -- i'll need to pass that in.
// -- this doesn't print %conf (should be easy to add -- also at ***)

// NOT ME:
// -- crossdate print dialog: if you cancel, really cancel!
// -- perf: measure how long pagination takes on 688 sites
// -- move histogram into cross, computed lazily?
// -- rename Score.span -- Score.overlap; make private, with accessor
// -- rename Cross.getName()?  seems weird.
// -- rename Cross to Crossdate

/*
  old TODO list:
  - integrate table rendering with CrossFrame, Editor

  - race condition: quit too soon doesn't finish printing.
  --- (how to solve?  start thread for printing, in "printing" threadgroup,
      "quit" joins 'em all)

  - finish converting to TabbedLineFactory
  - get histogram from cross, where it belongs anyway.

  - need a way to export the crossdate so word can read it.  (gah.)
    maybe edit->copy is easiest, but what format?  HTML?  RTF?
  - (word can import from excel very easily, and nobody has word but
    not excel.  just let them export to excel: tab-delimited text.)

  - BUG: if cross.run() throws an illegalargumentexception
  (which it can now do, to mean "can't do this"), i should deal with
  it nicely here.
*/

/**
   Print out crossdates.

   <p>Prints the list of significant scores, table of all scores, and
   histogram, or any combination of these sections, if you don't want
   everything.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
 */
public class CrossdatePrinter extends Printer {
    // the cross to print, and its histogram
    private Cross cross;
    private Histogram histo=null;

    /**
       Print all parts of the crossdate (significant scores, all
       scores, histogram).

       @param crossdate the crossdate to print
    */
    public CrossdatePrinter(Cross crossdate) {
        this(crossdate, true, true, true);
    }

    /**
       Print only some parts of the crossdate.

       @param c the crossdate to print
       @param sigScores if true, print the list of statistically
       significant scores
       @param allScores if true, print the table of all of the scores
       @param histogram if true, print the histogram
    */
    public CrossdatePrinter(Cross crossdate,
			    boolean sigScores,
			    boolean allScores,
			    boolean histogram) {
        cross = crossdate;
        if (histogram)
            histo = new Histogram(crossdate); // OOPS: isn't this part of cross?  (well, it should be...)

        // header
	printHeader();

	// significant scores
        if (sigScores) {
	    printSignificantScores();
        }

	// all scores
        if (allScores) {
	    printAllScores();
        }

	// histogram
        if (histogram) {
	    printHistogram();
        }

	// footer
	lines.add(new EmptyLine());
        lines.add(new ThinLine(0.0f, 0.3f));
        lines.add(new ByLine());
    }

    private void printHeader() {
        lines.add(new TextLine(cross.getName(), Line.TITLE_SIZE));

	TabbedLineFactory f = new TabbedLineFactory("12% < 1% >");
	lines.add(f.makeLine(I18n.getText("fixed") + ": \t" + cross.getFixed()));
	lines.add(f.makeLine(I18n.getText("moving") + ": \t" + cross.getMoving()));

        lines.add(new EmptyLine());
    }

    private void printSignificantScores() {
	lines.add(new TextLine(I18n.getText("sig_scores"), Line.SECTION_SIZE));
	TabbedLineFactory f = new TabbedLineFactory("5% < 20% ^ 25% ^ 30% < 20% <");

	// header
	lines.add(f.makeLine(I18n.getText("number") + "\t" +
			     I18n.getText("fixed") + "\t" +
			     I18n.getText("moving") + "\t" +
			     cross.getName() + "\t" +
			     I18n.getText("overlap")));

	DecimalFormat fmt = new DecimalFormat(cross.getFormat());

	for (int i=0; i<cross.highScores.size(); i++) {

	    // ***

	    TopScores.HighScore s = (TopScores.HighScore) cross.highScores.get(i);
	    lines.add(f.makeLine(s.number + "\t" +
				 s.fixedRange + "\t" +
				 s.movingRange + "\t" +
				 fmt.format(s.score) + "\t" +
				 s.span));
	}

	lines.add(new EmptyLine());
    }

    private void printAllScores() {
	lines.add(new TextLine(I18n.getText("all_scores"), Line.SECTION_SIZE));
	// String spec = "10% <";
	// for (int i=0; i<10; i++) spec += "9% <";
	// -- 10/9/9/9/... is better, but until i convert ScoresLine, it should be 9.09/9.09/...
	String spec = "9.09% <";
	for (int i=0; i<10; i++) spec += "9.09% <";
	TabbedLineFactory f = new TabbedLineFactory(spec);
	lines.add(f.makeLine("Year \t 0 \t 1 \t 2 \t 3 \t 4 \t 5 \t 6 \t 7 \t 8 \t 9"));

	for (int i=0; i<cross.getRange().rows(); i++)
	    lines.add(new ScoresLine(i));
	/*
	  for (int i=0; i<cross.getRange().rows(); i++) {
	  // -- WRITEME
	  lines.add(f.makeLine("Year \t 0 \t 1 \t 2 \t 3 \t 4 \t 5 \t 6 \t 7 \t 8 \t 9"));
	  }
	*/

	lines.add(new EmptyLine());
    }

    private void printHistogram() {
	lines.add(new TextLine(I18n.getText("histogram"), Line.SECTION_SIZE));

	// note: make sure these 3 widths add up to 50%,
	// which is where the histogram gets drawn (below,
	// search for "0.5").
	TabbedLineFactory f = new TabbedLineFactory("15% ^ 17.5% ^ 17.5% >");
	lines.add(f.makeLine(cross.getName() + "\t" +
			     I18n.getText("quantity") + "\t" +
			     I18n.getText("histogram")));

	int histoRows = histo.getNumberOfBuckets();
	for (int i=0; i<histoRows; i++) {
	    // WAS: lines.add(new HistoLine(histo, i));

	    String score = histo.getBucketRange(i);
	    int num = histo.getBucketItems(i);
	    String number = (num == 0 ? "" : String.valueOf(num));

	    final Line textLine = f.makeLine(score + "\t" +
					     number + "\t" +
					     "");
	    final int row = i;

	    // make a new line, which is just like |textLine|,
	    // but draws a box, starting at 50% across the page,
	    // representing the histogram.
	    // (unfortunately, i can't extend any class: only
	    // those i create myself.  so this is a bit weird.)
	    Line lineWithBar = new Line() {
		    public void print(Graphics g, PageFormat pf, float y) {
			textLine.print(g, pf, y);

			// draw box

			Graphics2D g2 = (Graphics2D) g;

			// 50% across the viewable page
			int histoGuide = (int) (pf.getImageableX() +
				       (float) (pf.getImageableWidth() * 0.50));

			// taken directly from HistoLine: REFACTOR!
			g2.setStroke(THIN_LINE);
			int ascent = g2.getFontMetrics().getAscent();
			int y0 = (int) (y + g2.getFontMetrics().getHeight() - ascent);
			float frac = (float) histo.getBucketItems(row) /
			             (float) histo.getFullestBucket();
			float right = (float) (pf.getImageableX() +
					       pf.getImageableWidth());
			int dx = (int) (frac * (right - histoGuide));
			g2.drawRect((int) histoGuide - 10, y0,
				    dx, g2.getFontMetrics().getHeight());
			// WHY -10?
		    }
		    public int height(Graphics g) {
			return textLine.height(g);
		    }
		};

	    lines.add(lineWithBar);
	}
	lines.add(new EmptyLine());
    }

    // line width for histogram boxes
    private final static Stroke THIN_LINE = new BasicStroke(0.5f);

    //
    // custom lines
    //

    // a row (decade) of scores
    private class ScoresLine implements Line {
        private int row;
        public ScoresLine(int row) {
            this.row = row;
        }
        public void print(Graphics g, PageFormat pf, float y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;
            float colWidth = (float) (pf.getImageableWidth() / 11.);
            g2.setFont(NORMAL);

            // decade
            int row_min = cross.getRange().getStart().row();
            String decade = null;
            if (row == 0) // REFACTOR!
                decade = cross.getRange().getStart().toString();
            else if (row + row_min == 0)
                decade = "1"; // special case
            else
                decade = (new Year(10 * (row + row_min))).toString();
            int width = g2.getFontMetrics().stringWidth(decade);
            g2.drawString(decade, (float) (pf.getImageableX()+colWidth-width), baseline);

	    // format
	    DecimalFormat fmt = new DecimalFormat(cross.getFormat());

            // data
            for (int i=0; i<10; i++) {
                Year yr = new Year(10 * (row + row_min) + i); // DUPLICATE CODE!  I SMELL IT!
                if (cross.getRange().contains(yr)) {
		    float x = cross.getScore(yr);
                    String datum = fmt.format(x);
                    width = g2.getFontMetrics().stringWidth(datum);

                    // background, if significant
                    // REFACTOR: better would be cross.isSignificant(Year),
		    // which can check the overlap, too
                    if (x > cross.getMinimumSignificant()) {
                        final int EPS = 2; // extra area around just-the-text
                        int ascent = g.getFontMetrics(NORMAL).getAscent();
                        Color old = g2.getColor();
			// FIXME: use Prefs
                        g2.setColor(Color.getColor("corina.grid.highlightcolor"));
                        // FIXME: ugly ugly ugly!
                        g2.setStroke(new BasicStroke(0)); // !!!
                        g2.fillRect((int) (pf.getImageableX() + colWidth*(i+2)-width - EPS),
				    (int) (baseline - ascent - EPS),
                                    (int) (width + 2*EPS),
				    (int) (ascent + 2*EPS));
                        g2.setColor(old);
                    }

                    // text
                    g2.drawString(datum, (float) (pf.getImageableX()+colWidth*(i+2)-width), baseline);
                }
            }
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }
}
