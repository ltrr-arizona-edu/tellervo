package corina.map.tools;

import corina.map.Vector3;
import corina.map.View;
import corina.map.Renderer;
import corina.map.MapPanel;
import corina.site.Site;
import corina.site.SiteNotFoundException;
import corina.util.Angle;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import java.awt.event.MouseEvent;

// the Tag Tool!  use it to readjust tags.
// (in the future, this should probably be part of the arrow tool with a modkey or something)
public class TagMoverTool extends Tool {
    private View v;

    public TagMoverTool(MapPanel p, View v, ToolBox b) {
        super(p, b);
        this.v = v; }

    Icon getIcon() {
        ClassLoader cl = this.getClass().getClassLoader();
        return new ImageIcon(cl.getResource("Images/Tag.png")); }
    Cursor getCursor() {
        return new Cursor(Cursor.DEFAULT_CURSOR); }
    String getTooltip() {
        return "Tag Adjuster"; }
    String getName() {
        return "Move Tag"; }
    Character getKey() {
        return new Character('t'); }
    KeyStroke getFastKey() {
        return null; }

    // mouse
    Renderer r;
    public void mouseClicked(MouseEvent e) {
        // let's say double-click = reset
        if (e.getClickCount() == 2) {
            Point pt = e.getPoint();
            try {
                site = p.siteForPoint(r, pt, 20*((int) v.zoom));
                p.setOffset(site.getLocation(), 0f, 0f);

                // update display
                // p.updateBufferLabelsOnly(); // ugly! slow!
		p.updateBufferLabelsOnly(site, extra); // just ugly!  but fast!
                p.repaint(); }
            catch (SiteNotFoundException snfe) { } // double-clicked on space => do nothing
            finally { // (reset) -- doesn't really need to be in the finally-block, but i'm not very smart
                site = null; } } }

    boolean draw=false;
    Point p1, p2;
    public void mousePressed(MouseEvent e) {
        r = Renderer.createRenderer(v);

        // store mouse-down point
        p1 = e.getPoint();

        try {
            site = p.siteForPoint(r, p1, 20*((int) v.zoom));
            Vector3 v = new Vector3(); // PERF: new()!
            r.render(site.getLocation(), v); // ok, we know which it is, now use the real location
            p1.x = (int) v.x;
            p1.y = (int) v.y;
	    extra = p.getLabelBounds(site, r); } // p.hide(site); // -- but this is really slow!
        catch (SiteNotFoundException snfe) {
            site = null; // reset
            return; } }

    Rectangle extra=null;

    float angle=0f;
    float dist=0f;
    Site site=null;

    public void mouseDragged(MouseEvent e) {
        if (site == null)
            return;
        draw = true;
        p2 = e.getPoint();
        angle = Angle.angle(p1, p2);
        dist = (float) Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));

	// TRY THIS: don't let dist get too big!
	//	dist = Math.min(dist, 100f); (but it doesn't quite work, and i don't know that i want it, anyway)
        // ALSO: if you drag "off" (>100px away?), tag snaps back to original position (?)
	// if (dist > 100f)
	// dist = 0f; // sort of...

        // ALSO: if you press escape, tag snaps back

	// ANOTHER IDEA:
	// if you're dragging something, don't let it overlap anything else -- if it would,
	// move them out of the way, if possible.  (how hard is that?)

        p.setOffset(site.getLocation(), angle + (float) Math.toRadians(90), dist / v.zoom);

	p.updateBufferLabelsOnly(site, extra); // ugly! -- and a hack! -- and ugly!
	extra = p.getLabelBounds(site, r); // (update |extra|)

        p.repaint(); }

    public void mouseReleased(MouseEvent e) {
        if (site == null)
            return;

        // i wasn't just dragging?  bah.
        if (!draw)
            return;

        // show it again -- disabled, because hide() is disabled -- see why above
        // p.show(site);

        // i just finished dragging, so set the new angle for this site
        // OLD:             p2 = e.getPoint();
        // OLD:            angle = angle(p1, p2);
        // OLD:            dist = (float) Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y)); // REFACTOR!
        // it's a per-location hash, so just tell mappanel what it is, and it'll take care of the rest
        // OLD:            p.setOffset(site.getLocation(), angle + (float) Math.toRadians(90), dist / view.zoom /* float */);

        // update buffer once for everything, and redraw
        // p.updateBufferLabelsOnly(); // ugly!
	p.updateBufferLabelsOnly(site, extra); // ugly! -- and a hack! -- and ugly!
        draw = false;
        p.repaint(); }

    public void decorate(Graphics g) {
	/*        if (draw) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(site.getSiteColor());
            p.setFontForLabel(g2, v);

            int numSites = p.sitesForPoint(site).size();

	    long t1 = System.currentTimeMillis();
            p.drawLabel(g2, p1, site, numSites, v);
	    long t2 = System.currentTimeMillis();
	    System.out.println("(spent " + (t2-t1) + " ms drawing one label)"); } */ } }
