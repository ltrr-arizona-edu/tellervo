package edu.cornell.dendro.corina.map.layers;

import edu.cornell.dendro.corina.map.Layer;
import edu.cornell.dendro.corina.map.Point3D;
import edu.cornell.dendro.corina.map.Projection;
import edu.cornell.dendro.corina.map.MapFile;
import edu.cornell.dendro.corina.map.Palette;

import edu.cornell.dendro.corina.tridas.Location;

import java.awt.Graphics2D;
import java.awt.Color;
import java.io.IOException; // !!!

import java.util.Iterator;

public class MapLayer extends Layer {

    private final static float MY_DASHES[] = new float[] { 5, 1, 1, 1 };

    // may be slow -- draws to g2, but doesn't change buf, used where you need to
    // draw to an arbitrary g2, but performance isn't critical, e.g., printing;
    // subclasses implement this.
    @Override
	public void draw(Graphics2D g2, Projection r) {
	// use strokes appropriate for this zoom: thicker lines for closer zoom
	// NORMAL: g2.setStroke(new BasicStroke(r.view.zoom/10f));
	// g2.setStroke(new BasicStroke(2)); // TEMP: THICK, for maryanne's presentations

	// FIXME: get from Palette

	try {
	    clip(g2, r);
            drawMap(g2, r);
	    unclip(g2);
	} catch (IOException ioe) {
	    // ???
	    System.out.println("ERROR: ioe loading map!");
	}
    }

    // points to draw (x[i],y[i]), and number of points this time (n)
    private int x[] = new int[64];
    private int y[] = new int[64];
    private int n;

    private void drawMap(Graphics2D g2, Projection r) throws IOException {
	// small things, but things i won't want to realloc each time
	Location loc = new Location();
	Point3D vec = new Point3D();

        Iterator headers = MapFile.getHeaders();

        while (headers.hasNext() && !abort) {
            MapFile.Header h = (MapFile.Header) headers.next();
            int vis = h.isVisible(r);
            if (vis == MapFile.VISIBLE_NO) // use a switch? with fall-through?
                continue;

            // ok, we know we're going to draw something.
            Color c = Palette.getColor(h);
            if (c == null)
                continue; // (...maybe)  (hey, don't use nulls for error handling!)
	    // OBSOLETE: can't return null now!
            g2.setColor(c);
	    g2.setStroke(Palette.getStroke(h));

	    // FOR PRINTING ON 12/10/2002 ONLY: plain ol' grayscale
	    // -- c = ColorUtils.grayscale(c)

            if (vis == MapFile.VISIBLE_YES) {
                // we'll have to render the whole thing.
		// get the data -- hopefully cached -- and render it.
                // what i'd prefer:
		// -- vis is just a bool, RIGHT HERE render min/max, and maybe use that.

                // get data, and count them
                n = h.getNumberOfPoints();

                // make sure x/y arrays big enough to hold n points
		ensureArraysBigEnough();

		// HIRES: float xx[] = new float[n];
		// HIRES: float yy[] = new float[n];

                // foreach point, render it into (x,y).
                h.projectData(r, x, y);

                // cut out trivial moves
		cutOutTrivialMoves();

                // draw it
                g2.drawPolyline(x, y, n);
		// HIRES: drawPolyline(g2, xx, yy, n);
            } else if (vis == MapFile.VISIBLE_POINT) {
                // score!  this whole segment is one pixel, so don't even bother to load it.

                loc.setLatitudeAsSeconds(h.getMinLatitude()); // WEIRD!
                loc.setLongitudeAsSeconds(h.getMinLongitude());
                r.project(loc, vec);
		// REDUNDANT: isVisible() projects both corners -- why can't i get at that result?

                int x = (int) vec.getX(), y = (int) vec.getY();
                g2.drawLine(x, y, x+1, y+1); // (is +1,+1 what i want?)
                // how many points does this actually draw?  (not many.)
		// -- worse, do i even care about them?
            }
        }
    }

    // like g2.drawPolyline(x,y,n), but with floats.
    private void drawPolyline(Graphics2D g2, float x[], float y[], int n) {

	// TODO: need to scale stroke, too!

	// scale g2 by 1/10(?)
	g2.scale(1/DETAIL, 1/DETAIL);

	// scale x/y by 10x
	// PERF: makes new arrays -- should re-use them
	int xi[] = new int[n];
	int yi[] = new int[n];
	for (int i=0; i<n; i++) {
	    xi[i] = (int) (x[i] * DETAIL); // BUG: round-to-nearest?
	    yi[i] = (int) (y[i] * DETAIL);
	}

	// draw x/y as polyline
	g2.drawPolyline(xi, yi, n);

	// scale g2 back by 10x
	g2.scale(DETAIL, DETAIL);
    }

    // normal DPI is 72
    // -- if i'll want to export at 300dpi, i'll need 300/72=4 here.
    // -- if you want 600dpi, use 8 here.
    // (what if the user might resize the map in illustrator?  uh-oh...)
    private static final float DETAIL = 10;

    // make x/y arrays big enough to hold n points;
    // realloc x/y, if needed: round up to power of 2, to minimize reallocations.
    private void ensureArraysBigEnough() {
	if (n > x.length) {
	    int arraySize = x.length;
	    while (arraySize < n) {
		arraySize *= 2;
	    }
	    x = new int[arraySize];
	    y = new int[arraySize];
	}
    }

    // cut out trivial moves
    // BUG: this sometimes fails!
    private void cutOutTrivialMoves() {

	// 0 looks jagged, 1 looks smoother, 2 looks much smoother;
	// 2 has some issues with continuity when zooming, though -- 1 less so, 0 none.
	final int threshold = 2; // "detail" -- detail = 5->2? (no need to go finer)
	// threshold=2 removes a lot of lines, and looks much cleaner -- USE THIS NORMALLY
	// threshold=1 and even =0 remove quite a few lines, but i can't see any real improvement
	// BUT: you will probably want threshold=1 for printouts, so don't throw it away!

	int to = 0;
	int lastX = x[0], lastY = y[0];
	for (int from=0; from<n; from++) {

	    if (/*from==0 || */from==n-1 ||
		(distance(lastX, lastY, x[from], y[from]) > threshold)) {

		// add this line
		lastX = x[to+1] = x[from];
		lastY = y[to+1] = y[from];

		to++;
	    }
	}
	// running stats on saved/total segments, it looks like we're saving around
	// 96% of lines with thresh=2 and 98% with thresh=5.  yow.
	n = to;
    }

    // pythagorean distance
    private static final double distance(double x1, double y1,
					 double x2, double y2) {
	return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
}
