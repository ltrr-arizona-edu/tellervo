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

package corina.map;

import corina.map.ToolBox.Tool;
import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteProperties;
import corina.util.ColorUtils;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

import java.awt.Point;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Dimension;

import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JFrame;

// features to consider:
// - tools:
// --- move/edit sites
// --- draw lines (between sites)
// --- draw circles? (range from a site)

public class MapPanel extends JPanel implements Printable {

    private View view;
    
    private ResourceBundle msg = ResourceBundle.getBundle("MapBundle");

    private JFrame _label=null; // label, if desired
    public void setLabel(JFrame label) {
        _label = label;
    }

    public MapPanel() throws IOException {
        view = new View(); // where?
        setBackground(Color.white);

        // disable double-buffering: not really useful, with the BufferedImage
        // RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);

        // initial buffer
        buf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB); // REFACTOR ME
        updateBuffer();
    }

    private BufferedImage buf;
    /* private */ Site s1=null, s2[]=null;

    // small value: diameter of the dot, and distance from dot to text
    private static int EPS = 5;

    // does this need to be synch'd?  might solve a misdraw problem.
    // (no, it doesn't.)
    public void updateBuffer() {
        // switch to WAIT cursor
        Cursor oldCursor = getCursor();
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        // recreate buf
        if (buf.getWidth()!=view.size.width || buf.getHeight()!=view.size.height)
            buf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) buf.getGraphics();

        // aa -- much slower, and doesn't help quality much, if at all.
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                            RenderingHints.VALUE_ANTIALIAS_ON);
// but on mac it's ok, and without it looks horrible

        // set stroke
        g2.setStroke(new BasicStroke(0.4f)); // 1.0f)); // medium: 0.5 to 1.2 is best

        // background
        g2.setColor(Color.white); // const?  use pallette.java
        g2.fillRect(0, 0, getWidth(), getHeight());

        // ----------------------------------------

        // "let's get ready to reeeenderrrrr!"
        Renderer r = Renderer.makeRenderer(view);

        drawGridlines(g2, r);

        // draw on the map -- what's with the exceptions?
        try {
            drawMap(g2, r);
        } catch (IOException ioe) {
            System.out.println("ERROR: " + ioe);
        }

        drawSites(g2, r);

        drawScale(g2, r);

        // switch back cursor
        setCursor(oldCursor);
    }

    // a hacked version of updatebuffer for gridline-dragging
    public void updateBufferGridlinesOnly() {// REFACTOR: updateBuffer(fast=true);
        if (buf.getWidth()!=view.size.width || buf.getHeight()!=view.size.height)
            buf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) buf.getGraphics();

        // background
        g2.setColor(Color.white); // const?  use pallette.java
        g2.fillRect(0, 0, getWidth(), getHeight());

        // just render the gridlines
        Renderer r = Renderer.makeRenderer(view);
        drawGridlines(g2, r);
    }

    private void drawOneGridline(Graphics2D g2, Renderer r, Map.Header g) {
        if (g.isVisible(r) != Renderer.VISIBLE_NO) { // REDUNDANT: isVisible projects the corners -- see below, too
            Vector3 v1 = r.project(g.getInsideCorner());
            Vector3 v2 = r.project(g.getOutsideCorner());
            g2.drawLine((int)v1.x, (int)v1.y, (int)v2.x, (int)v2.y);
            // nb, this takes advantage of the fact that gridlines are
            // just 2 points, which i suppose is ok (though a bit weird).
        }
    }
    private void drawGridlines(Graphics2D g2, Renderer r) {
        // drawing the gridlines: a simple loop for now (MUCH BETTER: start at r.loc, go out each way until !vis)
        Color gc = Pallette.getColor(Map.makeGridline(false, 0, 0)); // eh?
        if (gc != null)
            g2.setColor(gc);

        // TODO: draw labels, and stop looking for gridlines after they're gone.
        
        // horizontal gridlines
        for (int lat=-90; lat<=90; lat+=10) // gridlines every 10 degrees
            for (int lon=-180; lon<180; lon+=Map.STEP)
                drawOneGridline(g2, r, Map.makeGridline(true, lat, lon));

        // vertical gridlines
        for (int lon=-180; lon<=180; lon+=10) // gridlines every 10 degrees
            for (int lat=-90; lat<90; lat+=Map.STEP)
                drawOneGridline(g2, r, Map.makeGridline(false, lat, lon));
    }

    // (europe on 64 ints a day?)
    private int x[] = new int[64];
    private int y[] = new int[64];

    private void drawMap(Graphics2D g2, Renderer r) throws IOException {
        for (int i=0; i<Map.headers.length; i++) {
            Map.Header h = Map.headers[i];
            int vis = h.isVisible(r);
            if (vis == Renderer.VISIBLE_NO) // use a switch? with fall-through?
                continue;

            // ok, we know we're going to draw something.
            Color c = Pallette.getColor(h);
            if (c == null)
                continue; // ...maybe...
            g2.setColor(c);

            if (vis == Renderer.VISIBLE_YES) {
                // we'll have to render the whole thing.  get the data -- hopefully cached -- and render it

                // get data, and count them
                Map.Data d = h.getData();
                int numberOfLines = d.n; // was: d.x.length;

                // realloc x/y, if needed
                int myArraySize = x.length; // round up to power of 2, to minimize allocations
                while (myArraySize < numberOfLines) {
                    myArraySize *= 2;
                }
                if (myArraySize > x.length) {
                    x = new int[myArraySize];
                    y = new int[myArraySize];
                }

                // foreach point, render it into x/y
                for (int j=0; j<numberOfLines; j++) {
                    Vector3 v = r.project(new Location(d.y[j], d.x[j])); // maybe keep this location around, even.
                    x[j] = (int) v.x;
                    y[j] = (int) v.y;
                }

                // cut out trivial moves
                boolean faster = true;
                if (faster) {
                    int from = 0, to = 0;
                    int lastX = x[0], lastY = y[0];
                    for (;;) {
                        int threshold = 2; // "detail" -- detail = 5->2? (no need to go finer)
                        // threshold=2 removes a lot of lines, and looks much cleaner
                        // threshold=1 and even =0 remove quite a few lines, but i can't see any real improvement
                        double leapSize = Math.sqrt((x[from]-lastX)*(x[from]-lastX) + (y[from]-lastY)*(y[from]-lastY));
                        boolean bigLeap = (leapSize > threshold);
                        if (from==0 || from==numberOfLines-1 || bigLeap) {
                            // add this line
                            x[to] = x[from];
                            y[to] = y[from];
                            lastX = x[to];
                            lastY = y[to];
                            to++;
                        }
                        from++;
                        if (from==numberOfLines)
                            break;
                    }
                    // running stats on saved/total segments, it looks like we're saving around
                    // 96% of lines with thresh=2 and 98% with thresh=5.  yow.
                    numberOfLines = to;
                }

                // draw it
                g2.drawPolyline(x, y, numberOfLines);
            } else if (vis == Renderer.VISIBLE_POINT) {
                // score!  this whole segment is one pixel, so don't even bother to load it.
                Vector3 v = r.project(h.getInsideCorner()); // REDUNDANT: isVisible() projects both corners -- why can't i get at that result?
                g2.drawLine((int)v.x, (int)v.y, (int)v.x, (int)v.y);
            }
        }
    }
    
    // draw a little label, like those flags on hors d'oeuvres to tell you
    // which ones have dead animals in them and which ones are food
    private void label(Graphics2D g2, String text, int x, int y) {
        // measure the text
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int textHeight = g2.getFontMetrics().getHeight();

        // temps
        int width = textWidth + EPS*2;
        int height = textHeight/2+EPS;
        int RISE = height/2;
        int left = x - textWidth/2 - EPS;
        int right = left + width;
        int top = y-EPS-textHeight/2 - RISE;

        // ?
        int xleft = left+width/2 - RISE;
        int xright = left+width/2 + RISE;

        // abort!
        if (right < 0 || left > view.size.width)
            return;
        if (y < 0 || top > view.size.height)
            return;

        // speak-bubble
        int px[] = new int[] { left, right, right, xright, x, xleft, left };
        int py[] = new int[] { top, top, top+height, top+height, y, top+height, top+height };

        // actually draw it
        g2.fillPolygon(px, py, px.length);
        g2.setColor(Color.black);
        g2.drawPolygon(px, py, px.length);
        g2.drawString(text, x - textWidth/2, y - EPS/2 - RISE);
    }

    private void drawSites(Graphics2D g2, Renderer r) {
        // site 1
        g2.setFont(new Font("serif", Font.BOLD, (int)view.zoom*5));
        Vector3 p1=null;
        if (s1 != null) {
            g2.setColor(ColorUtils.addAlpha(s1.getSiteColor(), 128));

            p1 = r.project(s1.location);
            // g2.fillOval(p1.x-EPS/2, p1.y-EPS/2, EPS, EPS); -- don't fill oval now (see label)
            if (p1.z > 0) // REFACTOR: use an isVisible(), of some sort
                label(g2, s1.code, (int) p1.x, (int) p1.y);
        }

        // /n/ other sites -- s2
        if (s2 != null) {
            for (int i=0; i<s2.length; i++) {
                if (s2[i].location == null)
                    continue;

                g2.setColor(ColorUtils.addAlpha(s2[i].getSiteColor(), 128));
                Vector3 p2 = r.project(s2[i].location);
                if (p2.z < 0)
                    continue;
                //g2.fillOval((int)p2.x-EPS/2, (int)p2.y-EPS/2, EPS, EPS); // dot it
                label(g2, s2[i].code, (int) p2.x, (int) p2.y); // label it

                // p1-p2 line -- need both s1 and s2 here
                if (s1!=null && s1.location.distanceTo(s2[i].location)<=100)
                    g2.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
            }
        }
    }

    // this method is lousy.  instead of direct computation, it uses
    // nested binary searches, neither of which is guaranteed to
    // terminate with the correct result.  ouch.  rewrite me, please.
    // ---
    // it needs to ask the renderer, "how many pixels is 100km of longitude at x¡ latitude"?
    // add that to renderer, perhaps.
    private void drawScale(Graphics2D g2, Renderer r) {
        try {

            /*
            // new strategy: (O(1), only 1 unrender() call)
            // - figure out the y-coord we will draw on
            int y0 = getHeight() - 40;
            // - unrender that (!)
            Location loc = renderer.unrender(new Point(getWidth()/2, y0));
            System.out.println("center point = " + loc);
            // - from that latitude, ASSUME there's no tilt, etc., and figure out how many degs long. make 100km
            double r = 360 * 100 / (2500 * Math.cos(loc.latitude)); // hack!
            r = Math.abs(r);
            System.out.println("at " + loc.latitude + "N, 100km=" + r + "deg");
             r  = Math.toRadians(r); // BELOW SEEMS OK
            // - if that result is r, render map.r.loc.long-r/2, map.r.loc.long+r/2
            Location center = renderer.location;
            Vector3 left = renderer.project(new Location(center.latitude, center.longitude - r/2));
            Vector3 right = renderer.project(new Location(center.latitude, center.longitude + r/2));
            // - figure out dx between those points, and draw a line that long.
            int dist = (int) (right.x - left.x);

            // back-ass-wards compatibility
            int x0=25, x1=x0+dist;
             */

            // find 100km to mark off
	    int x0=25, x1=275, y0=getHeight()-40;
	    int jump = view.size.width/4;
	    Location loc1 = r.unrender(new Point(x0, y0));
	    int dist;
	    for (;;) {
		// look for x1 s.t. dist[(x0,y0)..(x1,y0)] = 100km
		Location loc2 = r.unrender(new Point(x1, y0));
		if (loc2 == null) return; // ack!
		dist = loc1.distanceTo(loc2);
		if (dist == 100 || jump==0)
		    break;
		if (dist < 100)
		    x1 += jump;
		else
		    x1 -= jump;
		jump *= 0.5;
	    }

	    // problem here if dist!=100?

	    // draw a nice yellow box
	    g2.setColor(new Color(255, 255, 127, 204));
	    g2.fillRect(x0-10, y0-10, x1-x0+20, 30);
	    g2.setColor(Color.black);
	    g2.drawRect(x0-10, y0-10, x1-x0+20, 30);

	    // draw the bar
	    g2.setColor(Color.black);
	    g2.drawLine(x0, y0, x1, y0);
	    g2.drawLine(x0, y0, x0, y0+10);
	    if (x1-x0 > 100) { // draw ticks if >100 pixels wide
		double dx = (x1-x0) / 10.;
		double xi = x0 + dx;
		for (int i=1; i<10; i++) {
		    g2.drawLine((int) xi, y0, (int) xi, y0+5);
		    xi += dx;
		}
	    }
	    g2.drawLine(x1, y0, x1, y0+10);
	    g2.setFont(new Font("serif", Font.PLAIN, 12));
	    g2.drawString("100 km", x1 - 50, y0 + 15);

	} catch (Exception e) {
	    // this method has all sorts of problems.  if all hell
	    // breaks loose, just exit quietly.
	}
    }

    // blit out the buffer, and draw Extra Crap.
    public void paintComponent(Graphics g) {
        g.drawImage(buf, 0, 0, Color.white, null);

        // current tool gets a chance to decorate
        decorate(g);
    }

    // extra crap, er, decorators -- via callbacks.  probably not threadsafe, but
    // consolodate with sample listeners if you really want that.
    private List decorators = new ArrayList();
    public void addDecorator(Tool t) {
        decorators.add(t);
    }
    public void removeDecorator(Tool t) {
        decorators.remove(t);
    }
    public void decorate(Graphics g) {
        for (int i=0; i<decorators.size(); i++)
            ((Tool) decorators.get(i)).decorate(g);
    }

    // hrm...
    public void setHeight(int h) {
        view.size.height = h;
    }
    public void setWidth(int w) {
        view.size.width = w;
    }

    // they really want my view
    public View getView() {
        return view;
    }

    // there should be a permanent mouse/motion listener here:
    // right-click on a site should always show:
    // [see "map planned features"]
    // and the title bar should always show the current location.

    // NATIVE PRINTABLE
    private PageFormat pf = new PageFormat();
    public void setPageFormat(PageFormat pf) {
        this.pf = pf;
    }
    private final static float DPI=300; // change me, if you like
    private final static float detail=DPI/60; // normal printing is 60dpi -- DON't CHANGE ME
    public int print(Graphics g, PageFormat format, int pageNr) throws PrinterException {
        // a map is always exactly one page
        if (pageNr > 0)
            return Printable.NO_SUCH_PAGE;

        // create my own view: zoom
        View detailedView = (View) view.clone();
        detailedView.zoom *= detail;

        // then set the size -- scale up by |detail| to get |DPI|
        int ww = (int) pf.getImageableWidth();
        int hh = (int) pf.getImageableHeight();
        // h = w = Math.min(h, w); // if you wanted a square map
        detailedView.size = new Dimension((int) (ww*detail), (int) (hh*detail));

        // create my own renderer
        Renderer r2 = new RectangularRenderer(detailedView);

        // small brush
        Graphics2D g2 = (Graphics2D) g;
        // g2.setStroke(new BasicStroke(0.5f));

        // offset so it's entirely on the page, and scale down to
        // make it look detailed but normal-sized
        final float dx = (float) pf.getImageableX();
        final float dy = (float) pf.getImageableY();
        g2.translate(dx, dy);
        g2.scale(1/detail, 1/detail);

        // REFACTOR: from here on -- gridlines, map, sites -- is (or should be)
        // the same for printing as for updating the buffer, so it should use the
        // same code.

        // draw gridlines
        drawGridlines(g2, r2);

        // draw map
        try {
            drawMap(g2, r2);
        } catch (IOException ioe) {
            System.out.println("ioe=" + ioe);
        }

        // WORKING HERE -- draw sites, scale/legend, etc.
        drawSites(g2, r2);
        
        return Printable.PAGE_EXISTS;
    }
}
