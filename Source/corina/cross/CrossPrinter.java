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
 - add space between sections
 - start sections on new pages?
 - make font/sizes settable?
 - integrate table rendering with CrossFrame, Editor?  (yes, please!)
 - race: quit too soon doesn't finish printing.
 --- (how to solve?  start thread for printing, in "printing" threadgroup, "quit" joins 'em all)

 stuff to do that's specific to the new refactoring:
 - compute page breaks before any rendering is done
 - (page numbers disappeared; be prepared to add 'em back)
- draw sigscores is the order they're sorted on-screen
- highlight sigscores in the allscores table
- try to abstract out center/right-aligned text printing
- try to consolidate header/data lines, which have redundant tab-processing
- waaaay too many new Font() calls.  (at least string literals are interned...)
- use an em-dash for ranges?  (can i do this everywhere, or just for printouts?)
- get histogram from cross, where it belongs anyway.

- need a way to export the crossdate so word can read it.  (gah.)  maybe edit->copy is easiest, but what format?
    */

public class CrossPrinter implements Printable {
    // page margins
    private double left, right, top, bottom;

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
    
    // a new crossdate printer, with user-selected stuff
    public CrossPrinter(Cross c, boolean sigScores, boolean allScores, boolean histogram) {
        cross = c;
        if (histogram)
            histo = new Histogram(c); // isn't this part of cross?  (well, it should be...)

        // formatter
        fmt = new DecimalFormat(cross.getFormat());

        // -- start stuffing Lines into a List
        lines = new ArrayList();
        lines.add(new TitleLine(cross.getName(), 18));
        lines.add(new LabelLine(msg.getString("fixed") + ": ", cross.fixed.toString()));
        lines.add(new LabelLine(msg.getString("moving") + ": ", cross.moving.toString()));
        lines.add(new EmptyLine());
        if (sigScores) {
            lines.add(new TitleLine(msg.getString("sig_scores"), 12));
            lines.add(new SigsHeaderLine());
            int sigRows = c.highScores.size();
            for (int i=0; i<sigRows; i++)
                lines.add(new SigsLine(i));
            lines.add(new EmptyLine());
        }
        if (allScores) {
            lines.add(new TitleLine(msg.getString("all_scores"), 12));
            lines.add(new ScoresHeaderLine());
            int scoreRows = c.range.getEnd().row() - c.range.getStart().row() + 1;
            for (int i=0; i<scoreRows; i++)
                lines.add(new ScoresLine(i));
            lines.add(new EmptyLine());
        }
        if (histogram) {
            lines.add(new TitleLine(msg.getString("histogram"), 12));
            lines.add(new HistoHeaderLine());
            int histoRows = histo.countBuckets();
            for (int i=0; i<histoRows; i++)
                lines.add(new HistoLine(histo, i));
            lines.add(new EmptyLine());
        }
        lines.add(new ByLine());
        // -- for now, wait until print() to measure them
    }

    // print() assigns this right away, and print() is the only public
    // method (the constructor doesn't use it), so we're safe.
    private Graphics2D g2;

    private List firstRowOfPage = null; // null before print() gets called.
    
    public int print(Graphics g, PageFormat pf, int page) {
        // store margins
        left = pf.getImageableX();
        top = pf.getImageableY();
        right = left + pf.getImageableWidth();
        bottom = top + pf.getImageableHeight();

        // store graphics
        g2 = (Graphics2D) g;

        // next pixel row to draw: start at the top
        double position = top;

        // never printed before ... paginate
        if (firstRowOfPage == null) {
            firstRowOfPage = new ArrayList();
            firstRowOfPage.add(new Integer(0));
            int row=0;

            double ruler = top;
            while (row < lines.size()) {
                Line l = (Line) lines.get(row);
                if (ruler + l.height(g2) > bottom) {
                    firstRowOfPage.add(new Integer(row));
                    ruler = top;
                }
                ruler += l.height(g2);
                row++;
            }
        }

        // not a real page
        if (page >= firstRowOfPage.size())
            return NO_SUCH_PAGE;
        
        // print rows of page
        int row = ((Integer) firstRowOfPage.get(page)).intValue();
        while (row < lines.size()) {
            Line l = (Line) lines.get(row);
            if (position + l.height(g2) < bottom) {
                l.print(g2, position);
                position += l.height(g2);
                row++;
            } else {
                break; // ... inelegant
            }
        }
        return PAGE_EXISTS;
    }

    interface Line {
        public void print(Graphics g, double y);
        public int height(Graphics g);
    }
    
    class TitleLine implements Line {
        private String title;
        private int size;
        public TitleLine(String title, int size) {
            this.title = title;
            this.size = size;
        }
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            // title
            g2.setFont(new Font("serif", Font.PLAIN, size)); // too many news!
            g2.drawString(title, (float) left, baseline); // |left| is from enclosing class
        }
        public int height(Graphics g) {
            return g.getFontMetrics(new Font("serif", Font.PLAIN, size)).getHeight();
        }
    }

    // the "Year  0  1  2 ..." line across the top of the scores
    class ScoresHeaderLine implements Line {
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;
            float colWidth = (float) ((right - left) / 11.);
            g2.setFont(new Font("serif", Font.PLAIN, 9));

            // "year"
            String yr = msg.getString("year");
            int width = g2.getFontMetrics().stringWidth(yr);
            g2.drawString(yr, (float) (left+colWidth-width), baseline);

            // numbers
            for (int i=0; i<10; i++) {
                yr = String.valueOf(i);
                width = g2.getFontMetrics().stringWidth(yr);
                g2.drawString(yr, (float) (left+colWidth*(i+2)-width), baseline);
            }
        }
        public int height(Graphics g) {
            return g.getFontMetrics(new Font("serif", Font.PLAIN, 9)).getHeight();
        }
    }

    // a row (decade) of scores
    class ScoresLine implements Line {
        private int row;
        public ScoresLine(int row) {
            this.row = row;
        }
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;
            float colWidth = (float) ((right - left) / 11.);
            g2.setFont(new Font("serif", Font.PLAIN, 9));

            // decade
            int row_min = cross.range.getStart().row();
            String decade = null;
            if (row == 0)
                decade = cross.range.getStart().toString();
            else if (row + row_min == 0)
                decade = "1"; // special case
            else
                decade = (new Year(10 * (row + row_min))).toString();
            int width = g2.getFontMetrics().stringWidth(decade);
            g2.drawString(decade, (float) (left+colWidth-width), baseline);

            // data
            for (int i=0; i<10; i++) {
                Year yr = new Year(10 * (row + row_min) + i); // DUPLICATE CODE FROM SOMEWHERE!
                if (cross.range.contains(yr)) {
                    String datum = fmt.format(cross.data[yr.diff(cross.range.getStart())]);
                    width = g2.getFontMetrics().stringWidth(datum);
                    g2.drawString(datum, (float) (left+colWidth*(i+2)-width), baseline);
                }
            }
        }
        public int height(Graphics g) {
            return g.getFontMetrics(new Font("serif", Font.PLAIN, 9)).getHeight();
        }
    }

    // "#", "Fixed", "Moving", "T-Score", "Overlap" headers for the sigs table
    class SigsHeaderLine implements Line {
        public void print(Graphics g, double y) {
            // baseline
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("serif", Font.PLAIN, 9));
            double span = (right - left);

            // #
            int width = g2.getFontMetrics().stringWidth(msg.getString("number"));
            g2.drawString(msg.getString("number"), (float) (left+span*0.05-width), baseline);

            // "Fixed"
            width = g2.getFontMetrics().stringWidth(msg.getString("fixed"));
            g2.drawString(msg.getString("fixed"), (float) (left+span*0.25-width/2), baseline);

            // "Moving"
            width = g2.getFontMetrics().stringWidth(msg.getString("moving"));
            g2.drawString(msg.getString("moving"), (float) (left+span*0.50-width/2), baseline);

            // "T-Score" (or some such)
            width = g2.getFontMetrics().stringWidth(cross.getName());
            g2.drawString(cross.getName(), (float) (left+span*0.80-width), baseline);

            // "Overlap"
            width = g2.getFontMetrics().stringWidth(msg.getString("overlap"));
            g2.drawString(msg.getString("overlap"), (float) (left+span*1.00-width), baseline);
        }
        public int height(Graphics g) {
            return g.getFontMetrics(new Font("serif", Font.PLAIN, 9)).getHeight();
        }
    }

class SigsLine implements Line {
    private int row;
    public SigsLine(int row) {
        this.row = row;
    }
    public void print(Graphics g, double y) {
        // baseline
        float baseline = (float) (y + height(g));
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("serif", Font.PLAIN, 9));
        Score s = (Score) cross.highScores.get(row);
        double span = (right - left);
        
        // #
        String x = String.valueOf(s.number);
        int width = g2.getFontMetrics().stringWidth(x);
        g2.drawString(x, (float) (left+span*0.05-width), baseline);

        // "Fixed"
        x = s.fixedRange.toString();
        width = g2.getFontMetrics().stringWidth(x);
        g2.drawString(x, (float) (left+span*0.25-width/2), baseline);

        // "Moving"
        x = s.movingRange.toString();
        width = g2.getFontMetrics().stringWidth(x);
        g2.drawString(x, (float) (left+span*0.50-width/2), baseline);

        // "T-Score" (or some such)
        x = fmt.format(s.score);
        width = g2.getFontMetrics().stringWidth(x);
        g2.drawString(x, (float) (left+span*0.80-width), baseline);

        // "Overlap"
        x = String.valueOf(s.span);
        width = g2.getFontMetrics().stringWidth(x);
        g2.drawString(x, (float) (left+span*1.00-width), baseline);
    }
    public int height(Graphics g) {
        return g.getFontMetrics(new Font("serif", Font.PLAIN, 9)).getHeight();
    }
}

class HistoHeaderLine implements Line {
    public void print(Graphics g, double y) {
        // baseline
        float baseline = (float) (y + height(g));
        Graphics2D g2 = (Graphics2D) g;
        float colWidth = (float) ((right - left) / 5.);
        g2.setFont(new Font("serif", Font.PLAIN, 9));

        // guides
        int rangeGuide = (int) (left + (float) ((right - left) * 0.15)); // center guide for ranges
        int histoGuide = (int) (left + (float) ((right - left) * 0.50)); // left guide for histo
        int qtyGuide = (rangeGuide + histoGuide) / 2; // center guide for qtys

        int width;
        width = g2.getFontMetrics().stringWidth(cross.getName());
        g2.drawString(cross.getName(), (float) (rangeGuide - width/2), baseline);

        width = g2.getFontMetrics().stringWidth(msg.getString("quantity"));
        g2.drawString(msg.getString("quantity"), (float) (qtyGuide - width/2), baseline);

        width = g2.getFontMetrics().stringWidth(msg.getString("histogram"));
        g2.drawString(msg.getString("histogram"), (float) ((histoGuide+right)/2 - width/2), baseline);
    }
    public int height(Graphics g) {
        return g.getFontMetrics(new Font("serif", Font.PLAIN, 9)).getHeight();
    }
}

class HistoLine implements Line {
    private Histogram histo;
    private int row;
    public HistoLine(Histogram histo, int row) {
        this.histo = histo;
        this.row = row;
    }
    public void print(Graphics g, double y) {
        // baseline
        float baseline = (float) (y + height(g));
        Graphics2D g2 = (Graphics2D) g;
        float colWidth = (float) ((right - left) / 5.);
        g2.setFont(new Font("serif", Font.PLAIN, 9));

        // guides
        int rangeGuide = (int) (left + (float) ((right - left) * 0.15)); // center guide for ranges
        int histoGuide = (int) (left + (float) ((right - left) * 0.50)); // left guide for histo
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
            double frac = (double) histo.getNumber(row) / (double) histo.getFullestBucket();
            int dx = (int) (frac * (right - histoGuide));
            g2.drawRect((int) histoGuide - 10, y0, dx, g2.getFontMetrics().getHeight());
            // "-10" here prevents it from flowing off the right side, which Mac OS X
            // really doesn't like (but strangely, only when you print just the histogram)
            // -- nope, it doesn't.  at least not always.
        }
    }
    public int height(Graphics g) {
        return g.getFontMetrics(new Font("serif", Font.PLAIN, 9)).getHeight();
    }
}

    // a "Label: Value" line, where label is bold; used for printing the fixed/moving samples
    class LabelLine implements Line {
        private String label, value;
        private static final int TAB = 72; // 1" -- where to break between label and value
        public LabelLine(String label, String value) {
            this.label = label;
            this.value = value;
        }
        public void print(Graphics g, double y) {
            // baseline for both
            float baseline = (float) (y + height(g));
            Graphics2D g2 = (Graphics2D) g;

            // label
            g2.setFont(new Font("serif", Font.PLAIN, 9)); // too many news!
            int width = g.getFontMetrics().stringWidth(label);
            g2.drawString(label, (float) left+TAB-width, baseline); // |left| is from enclosing class

            // value
            g2.setFont(new Font("serif", Font.PLAIN, 9)); // (no news is good news)
            g2.drawString(value, (float) left + TAB, baseline);
        }
        public int height(Graphics g) {
            return g.getFontMetrics(new Font("serif", Font.PLAIN, 9)).getHeight();
        }
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

class ByLine implements Line {
    public void print(Graphics g, double y) {
        // baseline
        float baseline = (float) (y + height(g));
        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(new Font("serif", Font.PLAIN, 9));
        g2.drawString(makeByLine(), (float) left, baseline);
    }
    public int height(Graphics g) {
        return g.getFontMetrics(new Font("serif", Font.PLAIN, 9)).getHeight();
    }
}

class EmptyLine implements Line {
    private int height=18; // 1/4"
    public EmptyLine() {
    }
    public EmptyLine(int space) {
        height = space;
    }
    public void print(Graphics g, double y) {
    }
    public int height(Graphics g) {
        return height;
    }
}
}
