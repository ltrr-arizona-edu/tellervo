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
import corina.print.ByLine;
import corina.print.PrintableDocument;
import corina.gui.Bug;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.MessageFormat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.print.Printable;
import java.awt.print.PageFormat;

/*
 left to do:
 - (start sections on new pages?)
 - (make font/sizes settable?)
 - integrate table rendering with CrossFrame, Editor
 - race condition: quit too soon doesn't finish printing.
 --- (how to solve?  start thread for printing, in "printing" threadgroup, "quit" joins 'em all)

 - try to abstract out center/right-aligned text printing
 - try to consolidate header/data lines, which have redundant tab-processing
 - get histogram from cross, where it belongs anyway.
 - (page numbers disappeared; be prepared to add 'em back if users complain)

 - need a way to export the crossdate so word can read it.  (gah.)  maybe edit->copy is easiest, but what format?  HTML?

 - BUG: if cross.run() throws an illegalargumentexception (which it can now do, to mean "can't do this"),
 i should deal with it nicely here.
*/

public class CrossPrinter implements PrintableDocument {
    // the cross to print, and its histogram
    private Cross cross;
    private Histogram histo=null;

    // l10n
    private ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

    // formatter
    private DecimalFormat fmt;

    // a new crossdate printer with everything.  (do i really need this?)
    public CrossPrinter(Cross c) {
        this(c, true, true, true);
    }

    private List lines;
    public List getLines() {
	return lines;
    }

    // a new crossdate printer, with user-selected stuff
    public CrossPrinter(Cross c, boolean sigScores, boolean allScores, boolean histogram) {
        cross = c;
        if (histogram)
            histo = new Histogram(c); // isn't this part of cross?  (well, it should be...)

        // formatter
        fmt = new DecimalFormat(cross.getFormat());

        // start stuffing Lines into a List
        lines = new ArrayList();
        lines.add(new TextLine(cross.getName(), Line.TITLE_SIZE));
        lines.add(new LabelLine(msg.getString("fixed") + ": ", cross.fixed.toString()));
        lines.add(new LabelLine(msg.getString("moving") + ": ", cross.moving.toString()));
        lines.add(new EmptyLine());
        if (sigScores) {
            lines.add(new TextLine(msg.getString("sig_scores"), Line.SECTION_SIZE)); // was 12, now 14?
            lines.add(new SigsHeaderLine());
            int sigRows = c.highScores.size();
            for (int i=0; i<sigRows; i++)
                lines.add(new SigsLine(i));
            lines.add(new EmptyLine());
        }
        if (allScores) {
            lines.add(new TextLine(msg.getString("all_scores"), Line.SECTION_SIZE)); // was 12, now 14?
            lines.add(new ScoresHeaderLine());
            int scoreRows = c.range.getEnd().row() - c.range.getStart().row() + 1; // LoD: belongs in Range.java!
            for (int i=0; i<scoreRows; i++)
                lines.add(new ScoresLine(i));
            lines.add(new EmptyLine());
        }
        if (histogram) {
            lines.add(new TextLine(msg.getString("histogram"), Line.SECTION_SIZE)); // was 12, now 14?
            lines.add(new HistoHeaderLine());
            int histoRows = histo.countBuckets();
            for (int i=0; i<histoRows; i++)
                lines.add(new HistoLine(histo, i));
            lines.add(new EmptyLine());
        }
        lines.add(new ByLine());
    }

    // the "Year  0  1  2 ..." line across the top of the scores
    class ScoresHeaderLine implements Line {
        public void print(Graphics g, PageFormat pf, float y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;
            float colWidth = (float) (pf.getImageableWidth() / 11.);
            g2.setFont(NORMAL);

            // "year"
            String yr = msg.getString("year");
            int width = g2.getFontMetrics().stringWidth(yr);
            g2.drawString(yr, (float) (pf.getImageableX()+colWidth-width), baseline);

            // numbers
            for (int i=0; i<10; i++) {
                yr = String.valueOf(i);
                width = g2.getFontMetrics().stringWidth(yr);
                g2.drawString(yr, (float) (pf.getImageableX()+colWidth*(i+2)-width), baseline);
            }
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }

    // a row (decade) of scores
    class ScoresLine implements Line {
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
            int row_min = cross.range.getStart().row();
            String decade = null;
            if (row == 0) // REFACTOR!
                decade = cross.range.getStart().toString();
            else if (row + row_min == 0)
                decade = "1"; // special case
            else
                decade = (new Year(10 * (row + row_min))).toString();
            int width = g2.getFontMetrics().stringWidth(decade);
            g2.drawString(decade, (float) (pf.getImageableX()+colWidth-width), baseline);

            // data
            for (int i=0; i<10; i++) {
                Year yr = new Year(10 * (row + row_min) + i); // DUPLICATE CODE FROM SOMEWHERE!  I SMELL IT!
                if (cross.range.contains(yr)) {
                    double x = cross.data[yr.diff(cross.range.getStart())];
                    String datum = fmt.format(x);
                    width = g2.getFontMetrics().stringWidth(datum);

                    // background, if significant
                    // REFACTOR: better would be cross.isSignificant(Year), which can check the overlap, too
                    if (x > cross.getMinimumSignificant()) {
                        final int EPS = 2; // extra area around just-the-text
                        int ascent = g.getFontMetrics(NORMAL).getAscent();
                        Color old = g2.getColor();
                        g2.setColor(Color.getColor("corina.grid.highlightcolor"));
                        // ugly ugly ugly!
                        g2.setStroke(new BasicStroke(0f)); // !!!
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

    // "#", "Fixed", "Moving", "T-Score", "Overlap" headers for the sigs table
    class SigsHeaderLine implements Line {
        public void print(Graphics g, PageFormat pf, float y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(NORMAL);
            float span = (float) pf.getImageableWidth();

            // #
            int width = g2.getFontMetrics().stringWidth(msg.getString("number"));
            g2.drawString(msg.getString("number"), (float) (pf.getImageableX()+span*0.05-width), baseline);

            // "Fixed"
            width = g2.getFontMetrics().stringWidth(msg.getString("fixed"));
            g2.drawString(msg.getString("fixed"), (float) (pf.getImageableX()+span*0.25-width/2), baseline);

            // "Moving"
            width = g2.getFontMetrics().stringWidth(msg.getString("moving"));
            g2.drawString(msg.getString("moving"), (float) (pf.getImageableX()+span*0.50-width/2), baseline);

            // "T-Score" (or some such)
            width = g2.getFontMetrics().stringWidth(cross.getName());
            g2.drawString(cross.getName(), (float) (pf.getImageableX()+span*0.80-width), baseline);

            // "Overlap"
            width = g2.getFontMetrics().stringWidth(msg.getString("overlap"));
            g2.drawString(msg.getString("overlap"), (float) (pf.getImageableX()+span*1.00-width), baseline);
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }

    class SigsLine implements Line {
        private int row;
        public SigsLine(int row) {
            this.row = row;
        }
        public void print(Graphics g, PageFormat pf, float y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(NORMAL);
            Score s = (Score) cross.highScores.get(row);
            float span = (float) pf.getImageableWidth();

            // #
            String x = String.valueOf(s.number);
            int width = g2.getFontMetrics().stringWidth(x);
            g2.drawString(x, (float) (pf.getImageableX()+span*0.05-width), baseline);

            // ALERT ALERT ALERT!!!  the fixed and moving columns should respect the user's
            // current selection -- i'll need to pass that in ...
            
            // "Fixed"
            x = s.fixedRange.toString();
            width = g2.getFontMetrics().stringWidth(x);
            g2.drawString(x, (float) (pf.getImageableX()+span*0.25-width/2), baseline);

            // "Moving"
            x = s.movingRange.toString();
            width = g2.getFontMetrics().stringWidth(x);
            g2.drawString(x, (float) (pf.getImageableX()+span*0.50-width/2), baseline);

            // "T-Score" (or some such)
            x = fmt.format(s.score);
            width = g2.getFontMetrics().stringWidth(x);
            g2.drawString(x, (float) (pf.getImageableX()+span*0.80-width), baseline);

            // "Overlap"
            x = String.valueOf(s.span);
            width = g2.getFontMetrics().stringWidth(x);
            g2.drawString(x, (float) (pf.getImageableX()+span*1.00-width), baseline);
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }

    class HistoHeaderLine implements Line {
        public void print(Graphics g, PageFormat pf, float y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;
            float colWidth = (float) (pf.getImageableWidth() / 5.);
            g2.setFont(NORMAL);

            // guides
            int rangeGuide = (int) (pf.getImageableX() + (float) (pf.getImageableWidth() * 0.15)); // center guide for ranges
            int histoGuide = (int) (pf.getImageableX() + (float) (pf.getImageableWidth() * 0.50)); // left guide for histo
            int qtyGuide = (rangeGuide + histoGuide) / 2; // center guide for qtys

            int width;
            width = g2.getFontMetrics().stringWidth(cross.getName());
            g2.drawString(cross.getName(), (float) (rangeGuide - width/2), baseline);

            width = g2.getFontMetrics().stringWidth(msg.getString("quantity"));
            g2.drawString(msg.getString("quantity"), (float) (qtyGuide - width/2), baseline);

            width = g2.getFontMetrics().stringWidth(msg.getString("histogram"));
            float right = (float) (pf.getImageableX() + pf.getImageableWidth());
            g2.drawString(msg.getString("histogram"), (float) ((histoGuide+right)/2 - width/2), baseline);
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }

    class HistoLine implements Line {
        private Histogram histo;
        private int row;
        public HistoLine(Histogram histo, int row) {
            this.histo = histo;
            this.row = row;
        }
        public void print(Graphics g, PageFormat pf, float y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;
            float colWidth = (float) (pf.getImageableWidth() / 5.);
            g2.setFont(NORMAL);

            // guides
            int rangeGuide = (int) (pf.getImageableX() + (float) (pf.getImageableWidth() * 0.15)); // center guide for ranges
            int histoGuide = (int) (pf.getImageableX() + (float) (pf.getImageableWidth() * 0.50)); // left guide for histo
            int qtyGuide = (rangeGuide + histoGuide) / 2; // center guide for qtys

            // col 0: score
            String score = histo.getRange(row);
            int width = g2.getFontMetrics().stringWidth(score);
            g2.drawString(score, (float) (rangeGuide - width/2), baseline);

            // col 1: number (right-aligned), but only if >0
            if (histo.getNumber(row) > 0) {
                String number = String.valueOf(histo.getNumber(row));
                width = g2.getFontMetrics().stringWidth(number);
                g2.drawString(number, (float) (qtyGuide - width/2), baseline);

                // col 2: histogram -- only if n>0
                g2.setStroke(new BasicStroke(0.5f)); // new!
                int ascent = g2.getFontMetrics().getAscent();
                int y0 = (int) (y + g2.getFontMetrics().getHeight() - ascent);
                float frac = (float) histo.getNumber(row) / (float) histo.getFullestBucket();
                float right = (float) (pf.getImageableX() + pf.getImageableWidth());
                int dx = (int) (frac * (right - histoGuide));
                g2.drawRect((int) histoGuide - 10, y0, dx, g2.getFontMetrics().getHeight());
                // "-10" here prevents it from flowing off the right side, which Mac OS X
                // really doesn't like (but strangely, only when you print just the histogram)
                // -- nope, it doesn't.  at least not always.
            }
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }

    // a "Label: Value" line; used for printing the fixed/moving samples
    class LabelLine implements Line {
        private String label, value;
        private static final int TAB = 72; // 1" -- where to break between label and value
        public LabelLine(String label, String value) {
            this.label = label;
            this.value = value;
        }
        public void print(Graphics g, PageFormat pf, float y) {
            // baseline for both
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            // label
            g2.setFont(NORMAL);
            int width = g.getFontMetrics().stringWidth(label);
            g2.drawString(label, (float) (pf.getImageableX()+TAB-width), baseline);

            // value
            g2.setFont(NORMAL);
            g2.drawString(value, (float) (pf.getImageableX() + TAB), baseline);
        }
        public int height(Graphics g) {
            return g.getFontMetrics(NORMAL).getHeight();
        }
    }
}
