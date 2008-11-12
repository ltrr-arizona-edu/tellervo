package edu.cornell.dendro.corina.map.tools;

import edu.cornell.dendro.corina.map.Point3D;
import edu.cornell.dendro.corina.map.View;
import edu.cornell.dendro.corina.map.Projection;
import edu.cornell.dendro.corina.map.MapPanel;
import edu.cornell.dendro.corina.site.Location;
import edu.cornell.dendro.corina.site.LegacySite;
import edu.cornell.dendro.corina.site.SiteNotFoundException;
import edu.cornell.dendro.corina.util.Angle;
import edu.cornell.dendro.corina.ui.Builder;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.geom.GeneralPath; // is this less efficient?
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.JToolTip;

import java.awt.event.MouseEvent;

// measure the distance between 2 points on the map, of course.

// other features to consider:
// -- shift = allow latitude XOR longitude diffs only
// -- auto-scroll

public class RulerTool extends Tool {
    private View v;

    public RulerTool(MapPanel p, View v, ToolBox b) {
        super(p, b);
        this.v = v;
    }

    @Override
	Icon getIcon() {
	return Builder.getIcon("ruler.png");
    }
    @Override
	Cursor getCursor() {
	Image image = Builder.getImage("ruler-pointer.png");
	return Toolkit.getDefaultToolkit().createCustomCursor(image,
							      new Point(0, 0),
							      "Ruler");
    }
    @Override
	String getTooltip() {
        return "Measure Tool";
    }
    @Override
	String getName() {
        return "Distance";
    }
    @Override
	Character getKey() {
        return new Character('m');
    }
    @Override
	KeyStroke getFastKey() {
        return null;
    }

    // mouse
    boolean mouseIsDown=false;
    int dist;
    Point pointA, pointB;
    Location locA = new Location(), locB = new Location();
    Point3D v3 = new Point3D();
    Projection r;
    LegacySite siteA=null, siteB=null;

    @Override
	public void mousePressed(MouseEvent e) {
        r = Projection.makeProjection(v);
        pointA = e.getPoint();

        // if this point is a site label, use that location!  decorate it somehow, too,
        // so it's painfully obvious the site is one end of the drag
        siteA = tryToGetSite(pointA, locA);
    }

    // EXTRACT ME: the stuff in the next method i do in several of the tools,
    // i.e., "figure out what site is here".  it shouldn't be tool-specific, at all.

    // given a point, if there's a site there, return it, and put
    // its location into |result|; otherwise, put the location
    // of the point in |result|, and return null.
    // (fwiw, the guts of this method take around 10ms to execute.)
    private LegacySite tryToGetSite(Point point, Location result) {
        LegacySite site;
        try {
            site = p.siteForPoint(r, point, 20*((int) v.getZoom()));
            r.project(site.getLocation(), v3);
            point.x = (int) v3.getX(); // common operation? -- "copy Point3D to point" --
            // -- or or maybe i should render to a point -- or maybe i should just subclass point -- or ...
            point.y = (int) v3.getY();

            // copy site.getLocation() to result
            Location.copy(result, site.getLocation());
	} catch (SiteNotFoundException snfe) {
            site = null;
            r.unproject(point, result);
	}
        return site;
    }
    
    @Override
	public void mouseDragged(MouseEvent e) {
        pointB = e.getPoint();

        // if this point is a site label, use that location!  decorate it somehow, too,
        // so it's painfully obvious the site is one end of the drag
        siteB = tryToGetSite(pointB, locB);

        // compute distance.  too friggin' easy, just the way i likes it!
        dist = locA.distanceTo(locB);

        // tell myself how to draw this in decorate()
        mouseIsDown = true;

        // force a redraw, otherwise, how would the panel know?
        p.repaint();
    }

    @Override
	public void mouseReleased(MouseEvent e) {
        // stop drawing line
        mouseIsDown = false;

        // let some stuff get GC'd
        r = null;
        pointA = pointB = null;

        // again, force redraw
        p.repaint();
    }

    // creates a new object -- probably not the greatest idea...
    private void arrow(Graphics2D g2, Point head, Point tail, float dist, float angle) {
        float theta = Angle.angle(tail, head);
        GeneralPath arrow = new GeneralPath();
        arrow.moveTo((float) (head.x + dist*Math.cos(theta+angle)),
                     (float) (head.y + dist*Math.sin(theta+angle)));
        arrow.lineTo(head.x, head.y);
        arrow.lineTo((float) (head.x + dist*Math.cos(theta-angle)),
                     (float) (head.y + dist*Math.sin(theta-angle)));
        g2.draw(arrow);
    }

    @Override
	public void decorate(Graphics g) {
        if (mouseIsDown) {

            Graphics2D g2 = (Graphics2D) g;
	    g2.setColor(Color.black);

            // draw a line between the two points
            g2.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
            // TODO: draw a curve?

	    // TODO: instead of checking for siteA==NULL, if one of (pointA pointB) is on a site that's selected,
	    // instead of drawing to that site, draw that end to all selected sites.

            // draw flat line, arrow on both ends -- but only if there's no site there
            if (siteA == null) {
                arrow(g2, pointA, pointB, 10f, (float) (Math.PI/2));
                arrow(g2, pointA, pointB, 10f, (float) (Math.PI*3/4));
	    }
            if (siteB == null) {
                arrow(g2, pointB, pointA, 10f, (float) (Math.PI/2));
                arrow(g2, pointB, pointA, 10f, (float) (Math.PI*3/4));
	    }

            // draw siteA, siteB labels, if needed (basically, light them up in yellow)
            if (siteA != null) {
                // REFACTOR: major refactoring needed here!
                int numSites = p.sitesForPoint(siteA).size(); // probably shouldn't rebuild list every time, just to count it -- memoize int
                g2.setColor(Color.yellow);
                MapPanel.setFontForLabel(g2, v);
                p.drawLabel(g2, pointA, siteA, numSites, v);
	    }
            if (siteB != null) {
                int numSites = p.sitesForPoint(siteB).size();
                g2.setColor(Color.yellow); // (often called twice -- do i care?)
                MapPanel.setFontForLabel(g2, v); // (often called twice -- do i care?)
                p.drawLabel(g2, pointB, siteB, numSites, v);
	    }

            // construct string, and use it to set the tooltip
            String str = dist + " km"; // (... not too bad ...)
            tip.setTipText(str);
            tip.setSize(tip.getPreferredSize());

            int x = (pointA.x+pointB.x)/2 - tip.getWidth()/2;
            int y = (pointA.y+pointB.y)/2 - tip.getHeight()/2;

            // if there's not enough room, sckootch it out of the way (extract method?: skootchiyor())
            int xt = x, yt = y; // translated x,y
            {
                if (siteA == null) // extract method?
                    r1.setBounds(pointA.x, pointA.y, 1, 1);
                else
                    r1.setBounds(pointA.x - 15, pointA.y - 7, 30, 14); // a guess!  ask siterenderer!
                if (siteB == null)
                    r2.setBounds(pointB.x, pointB.y, 1, 1);
                else
                    r2.setBounds(pointB.x - 15, pointB.y - 7, 30, 14); // a guess!  ask siterenderer!
                int out = 0;
                float theta = Angle.angle(pointA, pointB);
                rt.setSize(tip.getWidth(), tip.getHeight()); // easier way to say this?
                for (;;) {
                    rt.setLocation(xt, yt);
                    if (rt.intersects(r1) || rt.intersects(r2))
                        out += 2; // jump by 2 (pick a number)
                    else
                        break;

                    xt = x + (int) (out * Math.cos(theta + Math.PI/2));
                    yt = y + (int) (out * Math.sin(theta + Math.PI/2));
                }
            }

            // tip.setLocation(new Point(x, y)); -- can't do this for some reason, so i'll just translate the graphics
            g2.translate(xt, yt);
            g2.setStroke(defaultStroke); // apparently jtooltip doesn't set the stroke -- gah
            tip.paint(g2);
            g2.translate(-xt, -yt);
	}
    }

    private Rectangle r1 = new Rectangle(), r2 = new Rectangle(), rt = new Rectangle(); // siteA, siteB, tag
    private final BasicStroke defaultStroke = new BasicStroke(1f);
    private JToolTip tip = new JToolTip(); {
        tip.setDoubleBuffered(false); // i'm buffering myself, no reason to do it again
    }
}
