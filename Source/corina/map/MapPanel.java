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

import corina.map.tools.Tool;
import corina.map.tools.ToolBox;
import corina.util.ColorUtils;
import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteDBAdapter;
import corina.site.SiteEvent;
import corina.site.SiteInfo; // never used?
import corina.site.SiteNotFoundException;
import corina.gui.Bug;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
// import java.util.Map; -- name collision!
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.AlphaComposite;
import java.awt.Composite;

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

    private MapFrame fr;
    public void setZoom() {
        fr.setZoom(); }

    public MapPanel(MapFrame fr) {
        view = new View(); // where?
        setBackground(Color.white);

        this.fr = fr;
        
        // disable double-buffering: not really useful, with the BufferedImage
        RepaintManager.currentManager(this).setDoubleBufferingEnabled(false);

        // initial buffer
	// REFACTOR ME -- wait, do i need the A?  can i get a BI without it?
        buf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB);
	sBuf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB_PRE);
	// updateBuffer();

	// add listener: update map when DB changes
	SiteDB.getSiteDB().addSiteDBListener(new SiteDBAdapter() {
		public void siteMoved(SiteEvent e) {
		    // System.out.println("site " + e.getSource() + " moved");
		    updateBuffer(); // PERF: this is slow, nothing better?  i only need to redraw the labels
		    repaint();
		}
		public void siteCodeChanged(SiteEvent e) {
		    // System.out.println("site " + e.getSource() + " moved");
		    updateBuffer(); // PERF: this is slow, nothing better?  i only need to redraw the labels
		    repaint();
		}
		// REFACTOR: both of these are "update-buffer, repaint"
	    });
    }

    // double-buffer for map+sites (everything but tool decorators)
    private BufferedImage buf;
    // BETTER?: one buffer for the map, another for the labels -- for only a couple MB, i get much faster updates

//    private List sites=null; // was: private
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

    public void setSites(List sites) {
        for (int i=0; i<sites.size(); i++) {
            Site s = (Site) sites.get(i);
            if (s.getLocation() == null)
                continue; // ignore these
            String loc = s.getLocation().toString();
            if (siteHash.containsKey(loc)) {
                List list = (List) siteHash.get(loc);
                list.add(s);
            } else {
                List list = new ArrayList();
                list.add(s);
                siteHash.put(loc, list);
            }
        }
    }

    public boolean isVisible(Site s) {
        if (s.getLocation() == null)
            return false;
        String loc = s.getLocation().toString();
        if (!siteHash.containsKey(loc))
            return false;
        List list = (List) siteHash.get(loc);
        return list.contains(s);
    }
    
    public void show(Site s) {
        String loc = s.getLocation().toString();
        if (siteHash.containsKey(loc)) {
            List list = (List) siteHash.get(loc);
            if (!list.contains(s))
                list.add(s);
        } else {
            List list = new ArrayList();
            list.add(s);
            siteHash.put(loc, list);
        }
        updateBuffer();
        repaint();
        /* OLD:
        if (!sites.contains(s)) {
            sites.add(s);
            updateBuffer();
            repaint();
        }
        */
    }
    public void hide(Site s) {
        String loc = s.getLocation().toString();
        if (siteHash.containsKey(loc)) {
            List list = (List) siteHash.get(loc);
            if (list.contains(s)) {
                list.remove(s);
                if (list.isEmpty())
                    siteHash.remove(loc);
            }
        }
        updateBuffer();
        repaint();
        /* OLD:
        if (sites.contains(s)) {
            sites.remove(s);
            updateBuffer();
            repaint();
        }
         */
    }
    public void toFront(Site s) {
        String loc = s.getLocation().toString();
        List list = (List) siteHash.get(loc);
        list.remove(s);
        list.add(0, s);
        updateBuffer();
        repaint();
    }

    // map overlay
    SatelliteOverlay sat = new SatelliteOverlay();

    // does this need to be synch'd?  might solve a misdraw problem.
    // (no, it doesn't.)
    private static final Cursor wait = new Cursor(Cursor.WAIT_CURSOR);
    public void updateBuffer() {
        // switch to WAIT cursor
        Cursor oldCursor = getCursor();
        setCursor(wait);

        // recreate buf
        if (buf.getWidth()!=view.size.width || buf.getHeight()!=view.size.height) {
            buf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB);
            sBuf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB_PRE);
	}
        Graphics2D g2 = (Graphics2D) buf.getGraphics();

        // aa -- much slower, and doesn't help quality much, if at all.
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                            RenderingHints.VALUE_ANTIALIAS_ON);
// but on mac it's ok, and without it looks horrible

        // background: fill with white
        g2.setColor(Color.white); // const?  use pallette.java
        g2.fillRect(0, 0, getWidth(), getHeight());

        // "let's get ready to reeeenderrrrr!"
        final Renderer r = Renderer.makeRenderer(view);

        // draw section of earthmap10k.jpg onto g2 here
        // (NEW IDEA: tile bitmap onto locations, instead of extracting bitmap offsets from locations)
        // ADD FEATURE: drag an image file or URL to the map, and it's used as a background
        long tt1 = System.currentTimeMillis();
        sat.draw(g2, view, r);
        long tt2 = System.currentTimeMillis();
        System.out.println("spent " + (tt2-tt1) + " ms drawing overlay");

        // ----------------------------------------

        drawGridlines(g2, r);

        // draw on the map
        try {
	    tt1 = System.currentTimeMillis();
            drawMap(g2, r);
	    tt2 = System.currentTimeMillis();
	    System.out.println("spent " + (tt2-tt1) + " ms drawing line map");
        } catch (IOException ioe) {
            System.out.println("ERROR: " + ioe); // yeah!
        }

        long t1 = System.currentTimeMillis();
        drawSites(g2, r);
        long t2 = System.currentTimeMillis();
        System.out.println("spent " + (t2-t1) + " ms drawing labels");

        drawScale(g2, r);

        System.out.println("----");
        
        // switch back cursor
        // FIXME: don't use oldCursor, use *(whatever it should be) -- ask again, so it doesn't get stuck
        setCursor(oldCursor);
    }

    // a hacked version of updatebuffer for gridline-dragging
    public void updateBufferGridlinesOnly() {// REFACTOR: updateBuffer(fast=true);
        if (buf.getWidth()!=view.size.width || buf.getHeight()!=view.size.height) {
            buf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB);
            sBuf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB_PRE);
	}
        Graphics2D g2 = (Graphics2D) buf.getGraphics();

        // background
        g2.setColor(Color.white); // const?  use pallette.java
        g2.fillRect(0, 0, getWidth(), getHeight());

        Renderer r = Renderer.makeRenderer(view);

        // well, and the colorful stuff
        sat.draw(g2, view, r);

        // just render the gridlines
        drawGridlines(g2, r);
    }

    private Location corner = new Location(); // for corners
    private void drawOneGridline(Graphics2D g2, Renderer r, Map.Header g, boolean horizontal) {
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

            // random label stuff
            Color oldColor = g2.getColor();

            // label it, at the left edge of the screen
            if (x1 <= 0 && x2 > 0) {
                Location l1 = new Location();
                g.getInsideCorner(l1);
                int x = 5;
                int y = interpolate(x1, y1, x2, y1, x);
                // (---same from here down---)
                int value = (int) (horizontal ? l1.latitude : l1.longitude);
                String compass; // eep!  want a way to do part of a toString()
                if (horizontal)
                    compass = (value > 0 ? "N" : (value < 0 ? "S" : "N/S"));
                else
                    compass = (value > 0 ? "E" : (value < 0 ? "W" : "E/W"));
                value = Math.abs(value);
                g2.setColor(Color.black);
                g2.setFont(new Font("sansserif", Font.BOLD, 12));
                g2.drawString(value + "¡" + compass, x+1, y+1);
                g2.setColor(oldColor);
                g2.setFont(new Font("sansserif", Font.BOLD, 12));
                g2.drawString(value + "¡" + compass, x, y);
            }

            // label it, at the top edge of the screen
            if (y1 <= 0 && y2 > 0) {
                Location l1 = new Location();
                g.getInsideCorner(l1);
                int x = (x1 + x2) / 2 + 5; // DUMMY
                int y = 15; // ???
                // (---same from here down---)
                int value = (int) (horizontal ? l1.latitude : l1.longitude);
                String compass; // eep!  want a way to do part of a toString()
                if (horizontal)
                    compass = (value > 0 ? "N" : (value < 0 ? "S" : "N/S"));
                else
                    compass = (value > 0 ? "E" : (value < 0 ? "W" : "E/W"));
                value = Math.abs(value);
                if (!horizontal) { // better: use slope of gridline at this point
                    x += 15;
                    g2.rotate(Math.toRadians(90), x, 0); }
                g2.setColor(Color.black);
                g2.setFont(new Font("sansserif", Font.BOLD, 12));
                g2.drawString(value + "¡" + compass, x+1, y+1);
                g2.setColor(oldColor);
                g2.setFont(new Font("sansserif", Font.BOLD, 12));
                g2.drawString(value + "¡" + compass, x, y);
                if (!horizontal)
                    g2.rotate(Math.toRadians(-90), x, 0);
            }

            // FIXME: align stuff properly
            // WRITEME: also label bottom and right edges?
            // FIXME: it's kind of hard to read
            // PERF: it's not very efficient at all
            // FIXME: shouldn't the numbers be on top of the map lines?
        }
    }
    private int interpolate(int x1, int y1, int x2, int y2, int x) {
        return (y1 + y2) / 2 - 5; // DUMMY!
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
                drawOneGridline(g2, r, Map.makeGridline(true, lat, lon), true);

        // vertical gridlines
        for (int lon=-180; lon<=180; lon+=10) // gridlines every 10 degrees
            for (int lat=-90; lat<90; lat+=Map.STEP)
                drawOneGridline(g2, r, Map.makeGridline(false, lat, lon), false);
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
                int numberOfLines = d.n;

                // realloc x/y, if needed
                int myArraySize = x.length; // round up to power of 2, to minimize reallocations
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
                        final int threshold = 3; // "detail" -- detail = 5->2? (no need to go finer)
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

    // draw a label using this mappanel's offset hash
    public void drawLabel(Graphics2D g2, Point p, Site site, int numSites, View view) {
        // get offsets
         Offset o = getOffset(site.getLocation());

         // compute bubble point
         t.x = p.x + (int) (o.dist * view.zoom * Math.sin(o.angle));
         t.y = p.y - (int) (o.dist * view.zoom * Math.cos(o.angle));

         // call renderer to draw it for me
         SiteRenderer.drawLabel(g2, p, site, numSites, view, t);
    }
    Point t = new Point();

    // make this GETfont, instead.
    public static void setFontForLabel(Graphics g, View view) {
        //        g.setFont(new Font("sans-serif", Font.BOLD, (int)view.zoom*5));
        // g.setFont(new Font("sans-serif", Font.PLAIN, 9));
        int size = 9;
        if (view.zoom < 1.2)
            size = 8;
        if (view.zoom > 3)
            size = 10;
        g.setFont(new Font("sans-serif", Font.PLAIN, size));
    }

    /* private */ public static class Offset {
        public float angle = 0f;
        public float dist = 0f; // new!
    }
    private HashMap offsets = new HashMap(); // (location.toString() => [angle, dist]) hash -- what bug makes me need toString()?
    public void setOffset(Location loc, float angle, float dist) {
        Offset o = new Offset();
        o.angle = angle;
        o.dist = dist;
        offsets.put(loc.toString(), o);
    }
    public Offset getOffset(Location location) {
        if (offsets.containsKey(location.toString()))
            return (Offset) offsets.get(location.toString()); // anaphoric macros, where are you?
        else
            return nullOffset;
    }
    private final Offset nullOffset = new Offset(); // singleton

    private Vector3 p1 = new Vector3(), p2 = new Vector3(); // (document what these are used for!)
    private void drawSites(Graphics2D g2, Renderer r) {
        // this should be in drawLabel(), but i don't want to call it /n/ times for no reason.
        // therefore: be sure to call this before calling drawLabel()!
        setFontForLabel(g2, view);

        // /n/ sites
        if (siteHash != null) {

            // draw sites
            Iterator iter = siteHash.values().iterator();
            while (iter.hasNext()) {
                List list = (List) iter.next();
                Site top = (Site) list.get(0);

                g2.setColor(top.getSiteColor());
                r.render(top.getLocation(), p2);
                if (p2.z < 0) // "invisible" -- this should be a result of render(), not a z<0
                    continue;

                // label it
                pt.x = (int) p2.x;
                pt.y = (int) p2.y;
                drawLabel(g2, pt, top, list.size(), view);

                // (obsolete, from when there was s1 and s2: draw s1-s2 line here)
            }
        }
    }
    private Point pt = new Point();

    BufferedImage lBuf=null;

    // for actual POINT -- list all sites here, to be put in a popup menu
    public List sitesForPoint(Site target) {
        String loc = target.getLocation().toString();
        return (List) siteHash.get(loc);
    }
    // how to use: on click (where? how?) on site, show popup consisting of these sites

    // important: for LABEL -- (return topmost site, of course)
    // (who uses this?)
    public Site siteForPoint(Renderer r, Point p, int dist) throws SiteNotFoundException {
        if (siteHash == null)
            throw new SiteNotFoundException();

        // i'll need a graphics, of some sort.  might as well be this one.
        // (only used for measuring text)
        Graphics2D g2 = (Graphics2D) buf.getGraphics();
        setFontForLabel(g2, view); // yeah, very weird...

        // (stuff that was originally in the loop, but are invariant and moved out for performance)
        int textHeight = g2.getFontMetrics().getHeight();
        Point t = new Point();

        Iterator iter = siteHash.values().iterator();
        // normally, i'd check in backwards order, so you get the top-most (last-drawn) one first,
        // which is what the user expects.  IteratorsSuck, and I can't iterate backwards, so I'll
        // look at all and take the last one (it's still O(n)).
        Site returnValue = null;
        while (iter.hasNext()) {
            List list = (List) iter.next();
            Site s = (Site) list.get(0);
            
            r.render(s.getLocation(), p2);
            if (p2.z < 0)
                continue;

            String text = s.getCode(); // REFACTOR: violates OAOO -- should ask SiteRenderer what's there

            // measure the text
            int textWidth = g2.getFontMetrics().stringWidth(text);

            // get offset, if any
            Offset o = getOffset(s.getLocation());

            // center of Text bubble -- like drawLabel(), but not quite (view.zoom)
            // NO, IT'S EXACTLY LIKE DRAWLABEL -- USE THAT!
            t.x = (int) p2.x + (int) (o.dist * view.zoom * Math.sin(o.angle));
            t.y = (int) p2.y - (int) (o.dist * view.zoom * Math.cos(o.angle));

            // ripped from drawLabel() -- SO MAKE IT A METHOD IN SITERENDERER AND USE THAT!
            int left = t.x - (textWidth/2 + EPS), width = textWidth + 2*EPS;
            int top = t.y - (textHeight/2 + EPS/4), height = textHeight + EPS/2;
            if (p.x>=left && p.x<=(left+width) && p.y>=top && p.y<=(top+height))
                returnValue = s;
        }

        // if i have something, return it, else snfe
        if (returnValue != null)
            return returnValue;
        throw new SiteNotFoundException();
    }
    private static int EPS = 4; // must be same as in siterenderer?

    // REFACTOR: by LoD, this really ought to be in Renderer
    private Location leftLoc = new Location(), rightLoc = new Location();
    private Vector3 rightVec = new Vector3();
    private int pixelsForDistanceAtPoint(Renderer r, Point p, float km) {
        r.unrender(p, leftLoc);
        double radius = 40000. * Math.cos(Math.toRadians(leftLoc.latitude)); // radius of circle at latitude
        double deg = (km / radius) * 360.; // deg for |km|
        deg = Math.abs(deg); // ack
        rightLoc = new Location(leftLoc.latitude, leftLoc.longitude + (float) deg); // PERF: new!
        r.render(rightLoc, rightVec);
        return (int) (rightVec.x - p.x); }

    // constants
    private static final Color LEGEND_YELLOW = new Color(255, 255, 127, 204);
    private static final Stroke LEGEND_STROKE = new BasicStroke(0.5f);
    private static final Font LEGEND_FONT = new Font("serif", Font.PLAIN, 12);

    private void drawScaleOnBuffer(BufferedImage buf, Point p1, Point p2, int dist) {
	// TODO: if p1/p2/dist are the same as they used to be, i
	// don't need to do anything here, and i can save a few dozen
	// milliseconds -- that's the case when the user just changes
	// longitude, for example

	// get graphics to draw with
	Graphics2D g2 = buf.createGraphics();

	// erase old
	Composite keep = g2.getComposite();
	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
	g2.fillRect(0, 0, buf.getWidth(), buf.getHeight());
	g2.setComposite(keep);

	// REFACTOR: for printing, there's no reason to draw to
	// intermediate buffers, especially because they'd be friggin'
	// huge.  so i want these to still be draw(Graphics, ...), and
	// update methods will do { create graphics, clear buffer,
	// draw new data on buffer }

        // draw a nice yellow box
        g2.setStroke(LEGEND_STROKE);
        g2.setColor(LEGEND_YELLOW);
        g2.fillRect(p1.x-10, p1.y-10, p2.x-p1.x+20, 30);
        g2.setColor(Color.black);
        g2.drawRect(p1.x-10, p1.y-10, p2.x-p1.x+20, 30);

        // draw the bar
        g2.setColor(Color.black);
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);

        // draw ticks going across
        double dx = (p2.x-p1.x) / 10.;
        double xi = p1.x;
        for (int i=0; i<=10; i++) {
            int length;
            if (i % 10 == 0)
                length = 10;
            else if (i % 5 == 0)
                length = 8;
            else
                length = 5;
            g2.drawLine((int) xi, p1.y, (int) xi, p1.y+length);
            xi += dx; }

        // draw "100 km" on it
        g2.setFont(LEGEND_FONT);
        // BUG: this isn't centered or anything...
        String text = dist + " km";
        int w = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (p1.x+p2.x)/2 - w/2, p1.y + 15); }

    private void drawScale(Graphics2D g2, Renderer r) {
        try {

	    // figure out a good distance to mark off: 100km isn't always ideal
            int km = 100;
            for (;;) {
                Point p1 = new Point(25, getHeight() - 40);
                int dist = pixelsForDistanceAtPoint(r, p1, km);
                Point p2 = new Point(p1.x + dist, p1.y);

                if (dist < 20 && km < 1000)
                    km *= 10;
                else if (dist > 200 && km > 1)
                    km /= 10;
                else {
                    drawScaleOnBuffer(sBuf, p1, p2, km);

                    break;
                }
            }

        } catch (Exception e) {
            // this method used to have all sorts of problems -- it shouldn't any more, but just in case,
	    Bug.bug(e);
        }
    }
    
    BufferedImage sBuf=null;

    // blit out the buffer, and draw Extra Crap.
    public void paintComponent(Graphics g) {
        g.drawImage(buf, 0, 0, null);
	g.drawImage(sBuf, 0, 0, null);

        // current tool gets a chance to decorate
        decorate(g);
    }

    // extra crap, er, decorators -- via callbacks.  probably not threadsafe, but
    // consolidate with sample listeners if you really want that.
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
        view.size.height = h; }
    public void setWidth(int w) {
        view.size.width = w; }

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
