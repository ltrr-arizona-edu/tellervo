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
import corina.util.ColorUtils;
import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteProperties;
import corina.site.SiteNotFoundException;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
// import java.util.Map; -- name collision!
import java.util.HashMap;
import java.util.ResourceBundle;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.BasicStroke;
import java.awt.RenderingHints;

import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.RepaintManager;

// features to consider:
// - tools:
// --- draw lines (between sites)
// --- draw circles? (range from a site)

public class MapPanel extends JPanel implements Printable {

    private View view;
    
    private ResourceBundle msg = ResourceBundle.getBundle("MapBundle");

    private JFrame _label=null; // label, if desired
    public void setLabel(JFrame label) {
        _label = label;
    }

    public MapPanel() {
        view = new View(); // where?
        setBackground(Color.white);

        // disable double-buffering: not really useful, with the BufferedImage
        RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);

        // initial buffer
        buf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB); // REFACTOR ME
        updateBuffer();
    }

    // double-buffer for map+sites (everything but tool decorators)
    private BufferedImage buf;
    // BETTER?: one buffer for the map, another for the labels -- for only a couple MB, i get much faster updates

    List sites=null; // was: private
    // REFACTOR: instead of a list of sites, make it a (location => list of sites) hash
    /*
     that's a really nasty refactoring.  how to proceed?
     -- first, make it private.  then i have 2 classes of problems to deal with: internal, and external.
     -- resolve all external issues by adding methods as needed.
     -- i guess internal issues can be resolved the same way.
     -- changing its name (|siteHash|?), temporarily, might really help with the internal stuff
     */
    HashMap siteHash = new HashMap(); // a Map of (Location=>(Site|List)); first element of each list is the frontmost site
    // i'll need iterators ... (will i need to make my own site iterator?  no.  maybe.)

    public void show(Site s) {
        if (!sites.contains(s)) {
            sites.add(s);
            updateBuffer();
            repaint();
        }
    }
    public void hide(Site s) {
        if (sites.contains(s)) {
            sites.remove(s);
            updateBuffer();
            repaint();
        }
    }

    // small value: diameter of the dot, and distance from dot to text
    private static int EPS = 4;

    // does this need to be synch'd?  might solve a misdraw problem.
    // (no, it doesn't.)
    private static final Cursor wait = new Cursor(Cursor.WAIT_CURSOR);
    public void updateBuffer() {
        // switch to WAIT cursor
        Cursor oldCursor = getCursor();
        setCursor(wait);

        // recreate buf
        if (buf.getWidth()!=view.size.width || buf.getHeight()!=view.size.height)
            buf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) buf.getGraphics();

        // aa -- much slower, and doesn't help quality much, if at all.
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                            RenderingHints.VALUE_ANTIALIAS_ON);
// but on mac it's ok, and without it looks horrible

        // set stroke
//        g2.setStroke(new BasicStroke(1f));
//        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10f,
//                                     new float[] { 20f, 4f }, 0));
// REFACTOR: make a DottedStroke(float[]), this is stupid
// FIXME: stroke should be an attribute of the palette, not a global (default, no less) setting

        // background
        g2.setColor(Color.white); // const?  use pallette.java
        g2.fillRect(0, 0, getWidth(), getHeight());

        // ----------------------------------------

        // "let's get ready to reeeenderrrrr!"
        final Renderer r = Renderer.makeRenderer(view);

        drawGridlines(g2, r);
        
        // draw on the map
        try {
            drawMap(g2, r);
        } catch (IOException ioe) {
            System.out.println("ERROR: " + ioe); // yeah!
        }
        
        drawSites(g2, r);
        drawScale(g2, r);

        // switch back cursor
        // FIXME: don't use oldCursor, use *(whatever it should be) -- ask again, so it doesn't get stuck
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

    private Location corner = new Location(); // for corners
    private void drawOneGridline(Graphics2D g2, Renderer r, Map.Header g) {
        // REDUNDANT: isVisible projects the corners -- see below, too
        if (g.isVisible(r) != Renderer.VISIBLE_NO) {

            g.getInsideCorner(corner);
            r.render(corner, v);
            int x1 = (int) v.x, y1 = (int) v.y;
            g.getOutsideCorner(corner);
            r.render(corner, v);
            int x2 = (int) v.x, y2 = (int) v.y;

            g2.drawLine(x1, y1, x2, y2);
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
    private Location l = new Location();
    private Vector3 v = new Vector3();

    private synchronized void drawMap(Graphics2D g2, Renderer r) throws IOException {
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

                // what i'd prefer: vis is just a bool, RIGHT HERE render min/max, and maybe use that.
                
                // get data, and count them
                Map.Data d;
                synchronized (Map.headers) {
                    d = h.getData();
                }
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

                // foreach point, render it into (x,y)
                for (int j=0; j<numberOfLines; j++) {
                    l.latitude = (d.y[j] / 3600f) % 90;
                    l.longitude = (d.x[j] / 3600f) % 180;
                    // (keep this location around?)
                    r.render(l, v);
                    x[j] = (int) v.x;
                    y[j] = (int) v.y;
                }

                // cut out trivial moves
                {
                    int to = 0;
                    int lastX = x[0], lastY = y[0];
                    for (int from=0; from<numberOfLines; from++) {
                        int threshold = 2; // "detail" -- detail = 5->2? (no need to go finer)
                        // threshold=2 removes a lot of lines, and looks much cleaner -- USE THIS NORMALLY
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
                    }
                    // running stats on saved/total segments, it looks like we're saving around
                    // 96% of lines with thresh=2 and 98% with thresh=5.  yow.
                    numberOfLines = to;
                }

                // draw it
                g2.drawPolyline(x, y, numberOfLines);
            } else if (vis == Renderer.VISIBLE_POINT) {
                // score!  this whole segment is one pixel, so don't even bother to load it.

                h.getInsideCorner(corner);
                r.render(corner, v); // REDUNDANT: isVisible() projects both corners -- why can't i get at that result?
                int x = (int) v.x, y = (int) v.y;
                g2.drawLine(x, y, x+1, y+1); // (is +1,+1 what i want?)
                // how many points does this actually draw?  (not many.)  worse, do i even care about them?
            }
        }
    }
    
    // draw a little label, like those flags on hors d'oeuvres to tell you
    // which ones have dead animals in them and which ones are food
    // REFACTOR: why isn't this in Site?
    // FEATURE: it should handle multi-line text
    /* no longer private! */ static void drawLabel(Graphics2D g2, float zoom, String text, Point p, float angle, int dist, boolean dupe) {
        // measure the text
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int textHeight = g2.getFontMetrics().getHeight();
        
        // center of Text bubble
        Point t = new Point(p.x + (int) (dist * Math.sin(angle)),
                            p.y - (int) (dist * Math.cos(angle)));

        // abort!  (if completely off-screen) -- er, what if the stretchy goes on screen?
        // FIXME: i'll need some sort of clipping
        //        if (right < 0 || left > view.size.width)
//            return;
//        if (p.y < 0 || top > view.size.height)
//            return;

        // extra drop shadows
        if (dupe) {
            Color body = g2.getColor();
            for (int nr=0; nr<2; nr++) {
                Color c = body;
                for (int i=2; i>=nr; i--)
                    c = c.darker();
                for (int in=0; in<2; in++) {
                    g2.setColor(in==0 ? Color.black : c);
                    g2.setStroke(new BasicStroke((in==0 ? 2f : 1f), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                    // other JOINs make the point look mis-drawn (twice, from different angles)

                    // draw the bubble
                    {
                        int left = t.x - (textWidth/2 + EPS), width = textWidth + 2*EPS;
                        int top = t.y - (textHeight/2 + EPS/4), height = textHeight + EPS/2;
                        top += 3*(2-nr);
                        left += 3*(2-nr);
                        if (in==0)
                            g2.drawRoundRect(left, top, width, height, (int) zoom, (int) zoom);
                        else
                            g2.fillRoundRect(left, top, width, height, (int) zoom, (int) zoom);
                        // (don't worry, roundrect doesn't appear to take up (much) more time than rect)
                    }
                }
            }
            g2.setColor(body);
        }
        
        // drop shadow
        Color body = g2.getColor();
        for (int ii=0; ii<2; ii++) {
            g2.setColor(ii==0 ? Color.black : body);
            g2.setStroke(new BasicStroke((ii==0 ? 2f : 1f), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            // other JOINs make the point look mis-drawn (twice, from different angles)

            // draw the bubble
            {
                int left = t.x - (textWidth/2 + EPS), width = textWidth + 2*EPS;
                int top = t.y - (textHeight/2 + EPS/4), height = textHeight + EPS/2;
                if (ii==0)
                    g2.drawRoundRect(left, top, width, height, (int) zoom, (int) zoom);
                else
                    g2.fillRoundRect(left, top, width, height, (int) zoom, (int) zoom);
                // (don't worry, roundrect doesn't appear to take up (much) more time than rect)
            }

            // draw the stretchy to the point
            {
                // compute corners of label-rect
                int top = t.y - textHeight/8, bottom = t.y + textHeight/8;
                int left = t.x - textWidth/8, right = t.x + textWidth/8;

                // corners of label-rect, in arrays
                sx[0] = left;  sy[0] = top;
                sx[1] = right;  sy[1] = top;
                sx[2] = right;  sy[2] = bottom;
                sx[3] = left;  sy[3] = bottom;

                // draw trangles to each side
                for (int i=0; i<4; i++) {

                    // corners of triangles
                    tx[0] = p.x;  ty[0] = p.y;
                    tx[1] = sx[i];  ty[1] = sy[i];
                    tx[2] = sx[(i+1) % 4];  ty[2] = sy[(i+1) % 4];

                    // draw polygon, then fill with color
                    if (ii==0)
                        g2.drawPolygon(tx, ty, 3);
                    else
                        g2.fillPolygon(tx, ty, 3);

                    // it really does suck that i can't switch on method invocation, and also
                    // that draw/fill isn't simply a boolean parameter (which is sort of related)
                }
            }
        }
        
        // write the text, in black
        g2.setColor(ColorUtils.reallyDark(body) ? Color.white : Color.black);
        g2.drawString(text, t.x - textWidth/2, t.y + textHeight/2 - EPS/2); // is this y-value right?
    }
    private static int sx[] = new int[4], sy[] = new int[4]; // square corners (x,y)
    private static int tx[] = new int[3], ty[] = new int[3]; // triangle corners (x,y)

    void setFontForLabel(Graphics g) {
        g.setFont(new Font("sans-serif", Font.BOLD, (int)view.zoom*5));
    }
    
    // idea for refactoring: don't have s1/s2 special cases, just one s = set of sites
    // initial case is the same as measure-tool just having been used, so line+dist
    // is still drawn.  (only problem:  how to deal with 1-n maps, where you want
    // multiple lines?
    private Vector3 p1 = new Vector3(), p2 = new Vector3();
    private void drawSites(Graphics2D g2, Renderer r) {
        // this should be in drawLabel(), but i don't want to call it /n/ times for no reason.
        // therefore: be sure to call this before calling drawLabel()!
        setFontForLabel(g2);

        // /n/ sites
        if (sites != null) {

            // look for duplicate sites
            Set locations = new HashSet();
            Set dupes = new HashSet();
            for (int i=0; i<sites.size(); i++) {
                Site s = (Site) sites.get(i);
                if (s.location == null)
                    continue;
                if (locations.contains(s.location.toString())) // tostring is bad here, but it makes it work (why?)
                    dupes.add(s.location.toString());
                else
                    locations.add(s.location.toString());
            }
            locations = null; // gc it

            // draw sites
            for (int i=0; i<sites.size(); i++) {
                Site s = (Site) sites.get(i);
                
                // no location known for this site
                if (s.location == null)
                    continue;

                g2.setColor(s.getSiteColor());
                r.render(s.location, p2);
                if (p2.z < 0) // "invisible" -- this should be a result of render(), not a z<0
                    continue;
//                if (dupes.contains(s.location.toString())) // HACK: make dupes black, for now
//                    g2.setColor(Color.black);
                drawLabel(g2, view.zoom, s.code, new Point((int) p2.x, (int) p2.y), s.angle, s.dist /* (int) (10*view.zoom) */, dupes.contains(s.location.toString())); // label it

                // BUG: if s.location has had a label drawn to it before, draw all sites as "AAA,BBB,...",
                // possibly using 2 lines if needed -- or even actually drawing the elipsis, which the user can
                // hover/click to see more.

                // (when there was s1 and s2: draw s1-s2 line here)
            }
        }
    }

    // for actual POINT -- list all sites here, to be put in a popup menu
    public List sitesForPoint(Renderer r, Site target) {
        List found = new ArrayList();
        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (s.location == null)
                continue;
            if (s.location.toString().equals(target.location.toString())) // memoize!
                found.add(s);
        }
        return found;
    }
    // how to use: on click (where? how?) on site, show popup consisting of these sites

    // important: for LABEL [ future: return topmost site, of course ]
    public Site siteForPoint(Renderer r, Point p, int dist) throws SiteNotFoundException {
        if (sites == null)
            throw new SiteNotFoundException();

        // i'll need a graphics, of some sort.  might as well be this one.
        Graphics2D g2 = (Graphics2D) buf.getGraphics();
        setFontForLabel(g2); // yeah, very weird...

        for (int i=sites.size()-1; i>=0; i--) {
            // check backwards, so you get the top-most (last-drawn) one first,
            // which is what the user expects

            Site s = (Site) sites.get(i);
            if (s.location == null)
                continue;
            r.render(s.location, p2);
            if (p2.z < 0)
                continue;

            String text = s.code;

            // measure the text
            int textWidth = g2.getFontMetrics().stringWidth(text);
            int textHeight = g2.getFontMetrics().getHeight();

            // center of Text bubble
            Point t = new Point((int) p2.x + (int) (s.dist * Math.sin(s.angle)),
                                (int) p2.y - (int) (s.dist * Math.cos(s.angle)));            

            // ripped from drawLabel()
            int left = t.x - (textWidth/2 + EPS), width = textWidth + 2*EPS;
            int top = t.y - (textHeight/2 + EPS/4), height = textHeight + EPS/2;
            if (p.x>=left && p.x<=(left+width) && p.y>=top && p.y<=(top+height))
                return s;
        }
        throw new SiteNotFoundException();
    }

    // this method is lousy.  instead of direct computation, it uses
    // nested binary searches, neither of which is guaranteed to
    // terminate with the correct result.  ouch.  rewrite me, please.
    // ---
    // it needs to ask the renderer, "how many pixels is 100km of longitude at x¡ latitude"?
    // add that to renderer, perhaps.
    private static Location loc1 = new Location(), loc2 = new Location();
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
            r.unrender(new Point(x0, y0), loc1);
            int dist;
            for (;;) {
                // look for x1 s.t. dist[(x0,y0)..(x1,y0)] = 100km
                r.unrender(new Point(x1, y0), loc2);
                // if (loc2 == null) return; // ack!
                dist = loc1.distanceTo(loc2);
                if (dist == 100 || jump==0)
                    break;
                x1 += (dist < 100 ? jump : -jump);
                jump *= 0.5;
            }
            
	    // do i have a problem here if dist!=100?

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
            System.out.println("ioe=" + ioe); // yeah!
        }

        // WORKING HERE -- draw sites, scale/legend, etc.
        drawSites(g2, r2);
        
        return Printable.PAGE_EXISTS;
    }
}
