package corina.map.tools;

import corina.map.Location;
import corina.map.Vector3;
import corina.map.View;
import corina.map.Renderer;
import corina.map.MapPanel;
import corina.site.Site;
import corina.site.SiteDB;
import corina.site.SiteInfo;
import corina.site.SiteNotFoundException;
import corina.util.Angle;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import java.awt.event.MouseEvent;

// arrow
// -- click a site to select it
// -- control-click to select another
// -- click on nothing to deselect all
// -- drag a box(?) to select all sites in an area
// -- double-click a site to view its properties (edit it)
// -- drag a site to move it

public class ArrowTool extends Tool {
    private View v;

    public ArrowTool(MapPanel p, View v, ToolBox b) {
        super(p, b);
        this.v = v; }

    Icon getIcon() {
        ClassLoader cl = this.getClass().getClassLoader();
        return new ImageIcon(cl.getResource("Images/arrow.png")); }
    Cursor getCursor() {
        return new Cursor(Cursor.DEFAULT_CURSOR); }
    String getTooltip() {
        return "Selection Tool"; }
    String getName() {
        return "Info"; }
    Character getKey() {
        return new Character('v'); }
    KeyStroke getFastKey() {
        return null; } // should i have a NoShortcutException or singleton/flyweight or something?

    // mouse
    public void mouseClicked(MouseEvent e) {
        // what site am i looking at?
        // if control, add this to the selected-set
        // if no control, this becomes the selected-set

        // if double-click, show the properties for this site
        if (e.getClickCount() == 2) {
            try {
                Renderer r = Renderer.createRenderer(v);
                Point pt = e.getPoint();
                Site site = p.siteForPoint(r, pt, 20*((int) v.zoom)); // from popup trigger, below
                SiteInfo.showInfo(site); }
            catch (SiteNotFoundException snfe) { } } // double-click on nothing -> do nothing

	else {
	    // single-click
	    // WRITEME: select; if command(win32:control) held down, add this to selection.
	    // rerender this site only (not clip), and repaint.
	    // (WHAT I WANT FIRST: one updateBuffers(*) method, SiteRenderer knows clipping, drawing with selected=true

	    try {
		Renderer r = Renderer.createRenderer(v);
		Point pt = e.getPoint();
		site = p.siteForPoint(r, pt, 20*((int) v.zoom)); // from popup trigger, below
		p.setSelection(site);
		// the decorator hilites it now -- (though it shouldn't)
		p.repaint();
	    } catch (SiteNotFoundException snfe) {
		p.setSelection(null);
		site = null;
		// OOPS: need to know old selection, so i can un-hilite it (efficiently) now.
		// p.updateBufferLabelsOnly(); // ugly! -- and a hack! -- and ugly! -- and slow!
		p.repaint();
	    } } }

    // temps here?
    Renderer r;
    Site site=null;
    Point down=null;

    public void mousePressed(MouseEvent e) {
        // if there is a site here, record that
        try {
            r = Renderer.createRenderer(v);
            Point pt = e.getPoint();
            site = p.siteForPoint(r, pt, 20*((int) v.zoom)); }
        catch (SiteNotFoundException snfe) {
	    site = null; } // clicked on empty space => ignore

	down = e.getPoint();

        // REFACTOR: i need a way to attach the popup to any/all tools
        // from Tool.java, without regard for all the trivial crap in the actual
        // tool code -- (how) can i do that?

        // popup (mac version)
        maybeShowPopup(e, v); }

    public void mouseDragged(MouseEvent e) {
        if (site != null) {
	    // no drag -> do nothing
	    if (e.getPoint().equals(down))
		return;

            // record this place
            Location loc = new Location();
            r.unrender(e.getPoint(), loc);

            // TODO: esc cancels now

            // System.out.println("setting location of " + site.getCode() + " to " + site.getLocation());
            site.setLocation(loc);
	    SiteDB.getSiteDB().fireSiteMoved(site);

             // BUG: this violates all sorts of Site invariants, at least as far as MapPanel is concerned.
             // need an event structure, like for samples?  (it's on the way!)

             // REFACTORing idea: rewrite tools with the interface i
             // WISH i had, then write the classes to make those
             // interfaces possible.

	    // TODO: when it's been dragged from down2 to down2', the selection changes,
	    // but i only need to check a few points:
	    /*
	      -- break it down into orthogonal movements, down2 to down2', and down2' to down2''
	      -- if slice is getting smaller,
	      ---- all sites that are part of that slice, but not the result, get removed
	      -- if slice is getting bigger,
	      ---- all sites that are part of that slice, but not the initial, get added

	      so i'll basically want a set of (position, selected-p, site) tuples.

	      
	    */

            p.repaint();
	} else {
	    down2 = e.getPoint();

	    // TODO: select sites inside of (down..down2)

	    p.repaint(); // only if changed?  only relevant area?
	}
    }

    Point down2 = null;

    public void mouseReleased(MouseEvent e) {
        if (site != null) {
	    // no drag -> do nothing
	    if (e.getPoint().equals(down)) {
		site = null;
		return; }

	    // these were set on the last drag-event, and don't need to be done again
	    //            Location loc = new Location();
	    //            r.unrender(e.getPoint(), loc);
	    //            site.setLocation(loc);
            // System.out.println("setting location of " + site.getCode() + " to " + site.getLocation());
            // p.updateBuffer(); // only if moved!
	    p.updateBufferLabelsOnly(); // is this right?  (i haven't thought about it.)
	    // NO, BETTER: (site, extra), where |extra| is set on drag
	    // EVEN BETTER: don't do any redraws on mousereleased -- they should be handled in mousedragged
            p.repaint();
            site = null;
	}

        maybeShowPopup(e, v);

	if (site == null && down != null) {
	    down = down2 = null;
	    p.repaint();
	}
    }

    // TODO: decorate selected site(s)
    // TODO: don't draw this label!  it's already being drawn!
    public void decorate(Graphics g) {
        if (site != null) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(site.getSiteColor());
            p.setFontForLabel(g2, v);

            //                int numSites = p.sitesForPoint(site).size();

            Point pt = new Point();
            Vector3 v3 = new Vector3();
            r.render(site.getLocation(), v3);
            pt.x = (int) v3.x;
            pt.y = (int) v3.y;

            p.drawLabel(g2, pt, site, 1, v); }
	else if (down != null && down2 != null) {
	    // dragging to select sites

            Graphics2D g2 = (Graphics2D) g;

	    g2.setStroke(new BasicStroke(1f));

	    g2.setColor(new Color(0, 0, 0, 127));
	    g2.drawRect(down.x, down.y, down2.x-down.x, down2.y-down.y); // BUG: NPE HERE!

	    g2.setColor(new Color(0, 0, 0, 63));
	    g2.fillRect(down.x, down.y, down2.x-down.x, down2.y-down.y);
	}
    }
}
