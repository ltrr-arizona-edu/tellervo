package corina.graph;

import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PageFormat;

import java.awt.Graphics;
import java.awt.Graphics2D;

// pageable, not printable, because we know we're just 1 page, and we want the user to know this, too.
public class GraphPrinter implements Pageable {

/*
printing multi-screen-plots on paper like (some other program) --

-- landscape?  nah, let user decide.

-- full width

-- divide height by numsamples

-- respect xoffsets, but not yoffsets?

-- solid horizontal line across bottom

-- every 100 years, dotted vertical line and century label at bottom

-- (range is century-floor of min-year to century-ceiling of max-year)

-- draw sample titles above each graph

-- (warning if samples don't line up, or one is left at 1001, etc.?)
 */

    // a 1-page graph
    private static class GraphPage implements Printable {
        GraphPage() {
            /*
             -- compute range (e.g., 1412-1990)
             ---- expand range to centuries (e.g., 1400-2000)
             ---- compute scaling factor?
             */
        }
        public int print(Graphics g, PageFormat pf, int pageNr) {
            /*
             ---- set up the graphics object to scale automatically?

             -- for each century,
             ---- draw a horizontal black line at the bottom (unless last century)
             ---- draw a dotted vertical line at the end (solid, if first century)
             ---- write the label, centered below the line

             -- for each sample,
             ---- write the label, left-aligned, above its first year
             ---- draw it, x-scaled properly, and y-scaled so it normally (if <200, or 200%) won't run into other samples
             */
            return (pageNr==0 ? PAGE_EXISTS : NO_SUCH_PAGE);
        }
    }

    public Printable getPrintable(int pageNumber) {
        return new GraphPage(); // ???
    }
    public PageFormat getPageFormat(int pageNumber) {
        return null; // how to say 'default'?
    }
    public int getNumberOfPages() {
        return 1;
    }
}
