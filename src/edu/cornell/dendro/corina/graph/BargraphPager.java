package corina.graph;

import corina.Year;
import corina.Range; // ...
import corina.Element; // !

import java.util.List;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.print.*;

// (Book is-a Pageable)
public class BargraphPager extends Book {

    // bargraph to print
    private BargraphFrame bargraph;

    // computed values
    private int axisHeight;
    private int barHeight;
    private int barSpacing;
    private int barsPerPage;
    private int centuryWidth;
    private int centuriesPerPage;

    public BargraphPager(BargraphFrame bargraph, PageFormat pageFormat) {
	this.bargraph = bargraph;

	Rectangle page = getImageableArea(pageFormat);

	// compute bars per page, vertically
	axisHeight = 20; // FIXME
	barHeight = 18; // FIXME -- BUG, too: doesn't print anything if this is 18 or 20 (24 fine)
	barSpacing = 2; // FIXME
	barsPerPage = (page.height - axisHeight) / barHeight; // it's 26, right now

	// compute years per page, horizontally
	centuryWidth = 100; // FIXME
	centuriesPerPage = page.width / centuryWidth; // Q: this ok, or should i really compute Years?

	int firstBar = 0;
	int x=0; // DEBUG!
	while (firstBar < bargraph.bars.size()) {
	    // for each set of bars, use number of horiz pages required

	    // last bar on this page (row of pages)
	    int lastBar = Math.min(firstBar+barsPerPage, bargraph.bars.size()-1);

	    // firstYear = min(bargraph.bars[i].range.start)
	    Year firstYear = ((Element) bargraph.bars.get(0)).getRange().getStart();
	    for (int i=1; i<lastBar; i++) {
		Year tmp = ((Element) bargraph.bars.get(i)).getRange().getStart();
		firstYear = Year.min(firstYear, tmp);
	    }
	    // (TODO: it should back off to the century, though, right?)

	    // check: if this page doesn't hold all of those ranges, go again
	    while (coversAllRanges(bargraph.bars,
				   firstBar, lastBar,
				   firstYear, firstYear.add(centuriesPerPage*100))) {
		append(new BargraphPrinter(firstBar, firstYear), pageFormat);
		firstYear = firstYear.add(centuriesPerPage*100);
	    }

	    firstBar += barsPerPage;
	}
    }

    // are the elements bars[bar1..barN] covered by the range year1..yearN?
    private boolean coversAllRanges(List bars, int bar1, int barN, Year year1, Year yearN) {
	Range r = new Range(year1, yearN);

	for (int i=bar1; i<=barN; i++) {
	    Element e = (Element) bars.get(i);
	    if (e.getRange().intersection(r).span() == 0)
		return false;
	}

	return true;
    }

    // why doesn't PageFormat just use a rectangle?  dumb.
    private static Rectangle getImageableArea(PageFormat pf) {
	return new Rectangle((int) pf.getImageableX(),
			     (int) pf.getImageableY(),
			     (int) pf.getImageableWidth(),
			     (int) pf.getImageableHeight());
    }

    private class BargraphPrinter implements Printable {
	// start vertically down at firstBar, horizontally across at firstYear
	BargraphPrinter(int firstBar, Year firstYear) {
	    this.firstBar = firstBar;
	    this.firstYear = firstYear;
	}

	private int firstBar;
	private Year firstYear;

	public int print(Graphics g,
			 PageFormat pageFormat,
			 int pageIndex) throws PrinterException {

	    try { // DEBUG!

	    // ignore pageIndex here?

	    Graphics2D g2 = (Graphics2D) g;

	    // measure page -- left/right/top/bottom
	    Rectangle size = getImageableArea(pageFormat);
	    int l = size.x;
	    int r = l + size.width;
	    int t = size.y;
	    int b = t + size.height;

	    int h = t + axisHeight; // if i use HorizontalAxis's paint() here, translate it

	    // print horizontal axis on top, and century lines down the page
	    {
		g2.setColor(Color.black);
		// g2.setFont(AXIS_FONT);
		// g2.setStroke(AXIS_STROKE)
		g2.drawLine(l, h-1, r, h-1);

		Range range = new Range(firstYear, centuriesPerPage * 100); // !!!

		// WRITEME: draw centuries
		Year d = range.getStart().cropToCentury();
		while (d.compareTo(range.getEnd()) <= 0) {
		    // why not contains()?  because i can't guarantee initial value of d is in range

		    int x = l + d.diff(range.getStart());
		    int width = g2.getFontMetrics().stringWidth(d.toString()); // PERF: 2 toString()s
		    if (x - width/2 > r) // off screen to the right, we're done
			break;
		    g2.drawString(d.toString(), x - width/2, h-5); // 5?  where'd these come from?

		    g2.setStroke(new BasicStroke(0.1f));
		    g2.drawLine(x, h, x, b);

		    d = d.nextCentury();
		}
	    }

	    // print all bars
	    // g2.setColor(Color.black); // DEBUG
	    g2.setStroke(new BasicStroke(0.5f));
	    for (int i=0; i<barsPerPage; i++) {
		// not that many bars!
		if (firstBar + i >= bargraph.bars.size())
		    break;

		// the bar, and its title
		Element bar = (Element) bargraph.bars.get(firstBar + i);
		String title = (String) bar.details.get("title"); // BUG: assumes exists, nonnull!

		// edges of box
		int x1 = l + bar.getRange().getStart().diff(firstYear);
		int x2 = x1 + bar.getRange().span();
		int y1 = t + axisHeight + barHeight*i + barSpacing*(i+1);
		int y2 = y1 + barHeight;

		int ascent = g2.getFontMetrics().getAscent();
		int lineHeight = g2.getFontMetrics().getHeight();
		int capitalHeight = ascent - (lineHeight - ascent); // ??

		// outline
		g2.setColor(Color.white);
		g2.fillRect(x1, y1, x2-x1, y2-y1);
		g2.setColor(Color.black);
		g2.drawRect(x1, y1, x2-x1, y2-y1);

		// title
		int baseline = (y1+y2)/2 + capitalHeight/2;
		g2.setColor(Color.black);
		g2.drawString(title, x1+(barHeight-capitalHeight)/2, baseline);
	    }

	    } catch (RuntimeException re) { // DEBUG!
		System.out.println("re=" + re);
		re.printStackTrace();
	    }

	    return PAGE_EXISTS;
	}
    }
}
