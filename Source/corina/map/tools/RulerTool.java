package corina.map.tools;

import corina.map.Location;
import corina.map.Vector3;
import corina.map.View;
import corina.map.Renderer;
import corina.map.MapPanel;
import corina.site.Site;
import corina.site.SiteInfo;
import corina.site.SiteNotFoundException;
import corina.util.Angle;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.geom.GeneralPath; // is this less efficient?
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
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
        this.v = v; }

    Icon getIcon() {
        ClassLoader cl = this.getClass().getClassLoader();
        return new ImageIcon(cl.getResource("Images/ruler.png")); }
    Cursor getCursor() {
        ClassLoader cl = this.getClass().getClassLoader();
        ImageIcon icon = new ImageIcon(cl.getResource("Images/ruler-pointer.png"));
        return Toolkit.getDefaultToolkit().createCustomCursor(icon.getImage(), new Point(0, 0), "Ruler"); }
    String getTooltip() {
        return "Measure Tool"; }
    String getName() {
        return "Distance"; }
    Character getKey() {
        return new Character('m'); }
    KeyStroke getFastKey() {
        return null; }

    // mouse
    boolean mouseIsDown=false;
    int dist;
    Point pointA, pointB;
    Location locA = new Location(), locB = new Location();
    Vector3 v3 = new Vector3();
    Renderer r;
    Site siteA=null, siteB=null;

    public void mousePressed(MouseEvent e) {
        r = Renderer.createRenderer(v);
        pointA = e.getPoint();

        // if this point is a site label, use that location!  decorate it somehow, too,
        // so it's painfully obvious the site is one end of the drag
        siteA = tryToGetSite(pointA, locA);
    }

    // given a point, if there's a site there, return it, and put
    // its location into |result|; otherwise, put the location
    // of the point in |result|, and return null.
    // (fwiw, the guts of this method take around 10ms to execute.)
    private Site tryToGetSite(Point point, Location result) {
        Site site;
        try {
            site = p.siteForPoint(r, point, 20*((int) v.zoom));
            r.render(site.getLocation(), v3);
            point.x = (int) v3.x; // common operation? -- "copy vector3 to point" --
            // -- or or maybe i should render to a point -- or maybe i should just subclass point -- or ...
            point.y = (int) v3.y;
            Location.copy(result, site.getLocation()); }
        catch (SiteNotFoundException snfe) {
            site = null;
            r.unrender(point, result); }
        return site; }
    
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
        p.repaint(); }

    public void mouseReleased(MouseEvent e) {
        // stop drawing line
        mouseIsDown = false;

        // let some stuff get GC'd
        r = null;
        pointA = pointB = null;

        // again, force redraw
        p.repaint(); }

    // creates a new object -- probably not the greatest idea...
    private void arrow(Graphics2D g2, Point head, Point tail, float dist, float angle) {
        float theta = Angle.angle(tail, head);
        GeneralPath arrow = new GeneralPath();
        arrow.moveTo((float) (head.x + dist*Math.cos(theta+angle)),
                     (float) (head.y + dist*Math.sin(theta+angle)));
        arrow.lineTo((float) head.x, (float) head.y);
        arrow.lineTo((float) (head.x + dist*Math.cos(theta-angle)),
                     (float) (head.y + dist*Math.sin(theta-angle)));
        g2.draw(arrow); }

    public void decorate(Graphics g) {
        if (mouseIsDown) {

            Graphics2D g2 = (Graphics2D) g;

            //                g2.setColor(Color.red);

            // draw a line between the two points
            g2.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
            // draw a curve!
            /*{ -- terribly inefficient
                Location l1, l2, l3;
                Vector3 v1=new Vector3(), v2=new Vector3(), v3=new Vector3();
                l2 = Location.midpoint(locA, locB);
                l1 = Location.midpoint(locA, l2);
                l3 = Location.midpoint(l2, locB);
                r.render(l1, v1);
                r.render(l1, v2);
                r.render(l1, v3);
                g2.drawLine(pointA.x, pointA.y, (int) v1.x, (int) v1.y);
                g2.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
                g2.drawLine((int) v2.x, (int) v2.y, (int) v3.x, (int) v3.y);
                g2.drawLine((int) v3.x, (int) v3.y, pointB.x, pointB.y);
            }*/

	    // TODO: instead of checking for siteA==NULL, if one of (pointA pointB) is on a site that's selected,
	    // instead of drawing to that site, draw that end to all selected sites.

            // draw flat line, arrow on both ends -- but only if there's no site there
            if (siteA == null) {
                arrow(g2, pointA, pointB, 10f, (float) (Math.PI/2));
                arrow(g2, pointA, pointB, 10f, (float) (Math.PI*3/4)); }
            if (siteB == null) {
                arrow(g2, pointB, pointA, 10f, (float) (Math.PI/2));
                arrow(g2, pointB, pointA, 10f, (float) (Math.PI*3/4)); }

            // draw siteA, siteB labels, if needed (basically, light them up in yellow)
            if (siteA != null) {
                // REFACTOR: major refactoring needed here!
                int numSites = p.sitesForPoint(siteA).size(); // probably shouldn't rebuild list every time, just to count it -- memoize int
                g2.setColor(Color.yellow);
                p.setFontForLabel(g2, v);
                p.drawLabel(g2, pointA, siteA, numSites, v); }
            if (siteB != null) {
                int numSites = p.sitesForPoint(siteB).size();
                g2.setColor(Color.yellow); // (often called twice -- do i care?)
                p.setFontForLabel(g2, v); // (often called twice -- do i care?)
                p.drawLabel(g2, pointB, siteB, numSites, v); }

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
            g2.translate(-xt, -yt); } }

    private Rectangle r1 = new Rectangle(), r2 = new Rectangle(), rt = new Rectangle(); // siteA, siteB, tag
    private final BasicStroke defaultStroke = new BasicStroke(1f);
    private JToolTip tip = new JToolTip(); {
        tip.setDoubleBuffered(false); } // i'm buffering myself, no reason to do it again
}
