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
import corina.site.SiteFrame;
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

import java.awt.image.BufferedImage;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener; // REMOVE THESE (all 3)
//import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.JFrame;

// features to consider:
// - tools:
// --- move/edit sites
// --- draw lines (between sites)
// --- draw circles? (range from a site)

public class MapPanel extends JPanel { // implements MouseListener, MouseMotionListener { // UNIMPLEMENT THESE

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
        buf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) buf.getGraphics();

        // aa -- much slower, and doesn't help quality much, if at all.
        // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);

        // set stroke -- why?
        // g2.setStroke(new BasicStroke(1.0f)); // medium: 0.5 to 1.2 is best

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
    public void updateBufferGridlinesOnly() {
        // recreate buf
        buf = new BufferedImage(view.size.width, view.size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) buf.getGraphics();

        // background
        g2.setColor(Color.white); // const?  use pallette.java
        g2.fillRect(0, 0, getWidth(), getHeight());

        // "let's get ready to reeeenderrrrr!"
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

    // europe on 64 ints a day?
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
                Map.Data d = h.getData();
                int n = d.x.length;
                int m = x.length; // round up to power of 2, to minimize allocations
                while (m < n) {
                    m *= 2;
                }
                if (m > x.length) {
                    x = new int[m];
                    y = new int[m];
                }
                for (int j=0; j<n; j++) {
                    Vector3 v = r.project(new Location(d.y[j], d.x[j])); // maybe keep this location around, even.
                    x[j] = (int) v.x;
                    y[j] = (int) v.y;
                }
                g2.drawPolyline(x, y, n);
            } else if (vis == Renderer.VISIBLE_POINT) {
                // score!  this whole segment is one pixel, so don't even bother to load it.
                Vector3 v = r.project(h.getInsideCorner()); // REDUNDANT: isVisible() projects both corners -- why can't i get at that result?
                g2.drawLine((int)v.x, (int)v.y, (int)v.x, (int)v.y);
            }
        }
    }

        // for fun: draw the bounding boxes in green
        /*
         Point p1 = renderer.render(new Location(s.minlat, s.minlong));
         Point p2 = renderer.render(new Location(s.minlat, s.maxlong));
         Point p3 = renderer.render(new Location(s.maxlat, s.maxlong));
         Point p4 = renderer.render(new Location(s.maxlat, s.minlong));
         if (p1!=null && p2!=null && p3!=null && p4!=null) {
             g2.setColor(Color.green);
             g2.drawPolygon(new int[] { p1.x, p2.x, p3.x, p4.x },
                            new int[] { p1.y, p2.y, p3.y, p4.y }, 4);
         }
         */

    // draw a little label, like those flags on hors d'oeuvres to tell you
    // which one has dead animals in it and which ones are food
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

        // draw a line? -- OBSOLETE, REMOVE ME
/*
        if (drawLine) {
            g.setColor(Color.black);
            g.drawLine((int)p1.getX(), (int)p1.getY(),
                       (int)p2.getX(), (int)p2.getY());
        }
 */

        // current tool gets a chance to decorate
        decorate(g);
    }

    // extra crap, er, decorators -- via callbacks.  probably not threadsafe, but CONSOLIDATE WITH SAMPLE LISTENERS if you really want that.
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

    // ----------------------------------------------------------------------
    // below here is mouselistener / mousemotionlistener -- enjoy -- REMOVE
    // ----------------------------------------------------------------------

    /*
    private boolean _dragged = false;

    // drag!
    private Location _original=null;
    private Site _target=null;
    public void mousePressed(MouseEvent e) {
        // store starting (x,y)
        p1 = e.getPoint();
        _original = renderer.unrender(p1);

        _dragged = false;

        // figure out what was pressed
        if (_original == null)
            return;

        // look for the _target
        SiteDB db = SiteDB.getSiteDB();
        Site near[] = db.getSitesAt(_original);
        if (near.length > 0)
            _target = near[0];

        // hit?  print it
        // if (_target != null)
        // System.out.println(_target);

        // repaint -- this is not strictly needed, but it signifies to
        // the user that the mouse button was received.
        // !!! updateBuffer();
        repaint();
    }
    public void mouseReleased(MouseEvent e) {
	// user clicked on something, but didn't drag it: show info
	if (!_dragged && _target!=null)
	    SiteFrame.showSiteInfo(_target);

	// done dragging
	_dragged = false;

	// no target now
	_target = null;
	_original = null;

	// no line
	drawLine = false;
    }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) {
	// does this ever get called now?
    }
    private Point p1, p2;
    public void mouseDragged(MouseEvent e) {
        p2 = e.getPoint();
        Location loc = renderer.unrender(p2);

        _dragged = true;

        if (loc != null && _label != null) {
            String text = msg.getString("map") + ": " + _target + " to " + loc;
            if (_target != null)
                text += " (" + _target.location.distanceTo(loc) + " km)";
            _label.setTitle(text);
        }

        // no site?  forget it.
        if (_target == null)
            return;

        // uh, why's stuff null?  oh well, shit happens.
        if (loc == null || _original == null)
            return;

        // ok, we don't actually DO anything here any more, do we?
        // ---
        repaint();
        drawLine = true;
    }
    private boolean drawLine = false;
    public void mouseMoved(MouseEvent e) {
        Location loc = renderer.unrender(e.getPoint());
        if (loc != null && _label != null) // WHY would _label be null?
            _label.setTitle(msg.getString("map") + ": " + loc);
    }
     */
}
