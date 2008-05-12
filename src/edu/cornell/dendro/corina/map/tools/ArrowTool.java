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
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.map.tools;

import edu.cornell.dendro.corina.map.Point3D;
import edu.cornell.dendro.corina.map.View;
import edu.cornell.dendro.corina.map.Projection;
import edu.cornell.dendro.corina.map.MapPanel;
import edu.cornell.dendro.corina.site.Location;
import edu.cornell.dendro.corina.site.LegacySite;
import edu.cornell.dendro.corina.site.LegacySiteDB;
import edu.cornell.dendro.corina.site.SiteInfoDialog;
import edu.cornell.dendro.corina.site.SiteNotFoundException;
import edu.cornell.dendro.corina.ui.Builder;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import java.awt.event.MouseEvent;

// arrow
// -- click a site to select it
// -- control-click to select another [mac: command-click]
// -- click on nothing to deselect all
// -- drag a box(?) to select all sites in an area
// -- double-click a site to view its properties (edit it)
// -- drag a site to move it

/**
   An arrow tool for selecting and moving sites.

WRITEME.
 
   <h2>Left to do:</h2>
   <ul>
     <li>Drag-to-select
     <li>Command-click to select another
     <li>Double-click on multiple sites edits multiple
     <li>Right-click should show a menu, not list the sites there (right?)
     <li>Clicking on the dot should select all; clicking on a label should select just that one
     <li>Dragging the label should just move the label, not the site/location
     <li>Javadoc
     <li>Get drag-to-move working (real buffer updates)
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class ArrowTool extends Tool {
    private View v;

    public ArrowTool(MapPanel p, View v, ToolBox b) {
        super(p, b);
        this.v = v;
    }

    @Override
	Icon getIcon() {
	return Builder.getIcon("arrow.png");
    }
    @Override
	Cursor getCursor() {
        return new Cursor(Cursor.DEFAULT_CURSOR);
    }
    @Override
	String getTooltip() {
        return "Selection Tool";
    }
    @Override
	String getName() {
        return "Info";
    }
    @Override
	Character getKey() {
        return new Character('v');
    }
    @Override
	KeyStroke getFastKey() {
        return null; // should i have a NoShortcutException or singleton/flyweight or something?
    }

    // mouse
    @Override
	public void mouseClicked(MouseEvent e) {
        // what site am i looking at?
        // if control, add this to the selected-set
        // if no control, this becomes the selected-set

        // if double-click, show the properties for this site
        if (e.getClickCount() == 2) {
            try {
                Projection r = Projection.makeProjection(v);
                Point pt = e.getPoint();
                LegacySite site = p.siteForPoint(r, pt, 20*((int) v.getZoom())); // from popup trigger, below
                new SiteInfoDialog(site, p.getFrame());
                p.notifyLabelsChanged();
                p.updateBufferLabelsOnly();
                p.repaint();
		// FIXME: null = center on screen; would be better to
		// center on the Atlas window which i'm in
	    } catch (SiteNotFoundException snfe) {
		// double-click on nothing -> do nothing
	    }
	} else {
	    // single-click
	    // WRITEME: select; if command(win32:control) held down, add this to selection.
	    // rerender this site only (not clip), and repaint.
	    // (WHAT I WANT FIRST: one updateBuffers(*) method, SiteRenderer knows clipping, drawing with selected=true

	    try {
		Projection r = Projection.makeProjection(v);
		Point pt = e.getPoint();
		site = p.siteForPoint(r, pt, 20*((int) v.getZoom())); // from popup trigger, below
		p.setSelection(site);
		// the decorator hilites it now -- (though it shouldn't)
		p.repaint();
	    } catch (SiteNotFoundException snfe) {
		p.setSelection(null);
		site = null;
		// OOPS: need to know old selection, so i can un-hilite it (efficiently) now.
		// p.updateBufferLabelsOnly(); // ugly! -- and a hack! -- and ugly! -- and slow!
		p.repaint();
	    }
	}
    }

    // temps here?
    Projection r;
    LegacySite site=null;
    Point down=null;

    @Override
	public void mousePressed(MouseEvent e) {
        // if there is a site here, record that
        try {
            r = Projection.makeProjection(v);
            Point pt = e.getPoint();
            site = p.siteForPoint(r, pt, 20*((int) v.getZoom()));
	} catch (SiteNotFoundException snfe) {
	    site = null; // clicked on empty space => ignore
	}

	down = e.getPoint();

        // REFACTOR: i need a way to attach the popup to any/all tools
        // from Tool.java, without regard for all the trivial crap in the actual
        // tool code -- (how) can i do that?

        // popup (mac version)
        maybeShowPopup(e, v);
    }

    @Override
	public void mouseDragged(MouseEvent e) {
        if (site != null) {
	    // no drag -> do nothing
	    if (e.getPoint().equals(down))
		return;

            // record this place
            Location loc = new Location();
            r.unproject(e.getPoint(), loc);

            // TODO: esc cancels now

            // System.out.println("setting location of " + site.getCode() + " to " + site.getLocation());
            site.setLocation(loc);

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

            // p.updateBufferLabelsOnly(); // bad!  causes too many redraws.

            p.repaint();
	} else {
	    down2 = e.getPoint();

	    // TODO: select sites inside of (down..down2)
            
            Rectangle selectionBox = new Rectangle();
            selectionBox.x = Math.min(down.x, down2.x);
            selectionBox.y = Math.min(down.y, down2.y);
            selectionBox.width = Math.abs(down.x - down2.x);
            selectionBox.height = Math.abs(down.y - down2.y);

            // project all sites, and see who's in (down..down2)

            /*
             TODO: only consider visible sites (perf) -- this changes the loop
             TODO: lazily/only once make r/p3 objects
             TODO: deselect other sites
             TODO: memoize projections, so as you drag, this doesn't change
            */
            java.util.List sites = LegacySiteDB.getSiteDB().sites;
            for (int i=0; i<sites.size(); i++) {
                LegacySite s = (LegacySite) sites.get(i);
                Location l = s.getLocation();
                if (l == null)
                    continue;

                Projection r = Projection.makeProjection(v);
                Point3D p3 = new Point3D();
                r.project(l, p3);

                boolean insideBox = selectionBox.contains(p3.getX(), p3.getY());

                edu.cornell.dendro.corina.map.LabelSet ls = p.labels; // HACK!
                ls.setSelected(s, insideBox);
            }

            edu.cornell.dendro.corina.map.LabelSet ls = p.labels; // HACK!
            System.out.println(ls.countSelectedSites() + " sites selected");
            
	    p.repaint(); // only if changed?  only relevant area?
	}
    }

    private Point down2 = null;

    @Override
	public void mouseReleased(MouseEvent e) {
        if (site != null) {
	    // no drag -> do nothing
	    if (e.getPoint().equals(down)) {
		site = null;
		return;
	    }

	    // these were set on the last drag-event, and don't need to be done again
	    //            Location loc = new Location();
	    //            r.unproject(e.getPoint(), loc);
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
    @Override
	public void decorate(Graphics g) {
        if (site != null) {
            // draw the site here myself.
            // this is a performance hack: really, i should just revalidate the layer.
            
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(site.getSiteColor());
            MapPanel.setFontForLabel(g2, v);

            //                int numSites = p.sitesForPoint(site).size();

            Point pt = new Point();
            Point3D v3 = new Point3D();
            r.project(site.getLocation(), v3);
            pt.x = (int) v3.getX();
            pt.y = (int) v3.getY();

            p.drawLabel(g2, pt, site, 1, v);
	} else if (down != null && down2 != null) {
	    // dragging to select sites

            Graphics2D g2 = (Graphics2D) g;

            g2.setStroke(STROKE);

	    g2.setColor(MUCH_DARKER);
	    drawRectSafe(g2, down.x, down.y, down2.x, down2.y);

	    g2.setColor(DARKER);
	    fillRectSafe(g2, down.x, down.y, down2.x, down2.y);
	}
    }

    private static BasicStroke STROKE = new BasicStroke(1);
    private static Color DARKER = new Color(0, 0, 0, 63);
    private static Color MUCH_DARKER = new Color(0, 0, 0, 127);
    
    // normally drawRect() and fillRect() assume width, height > 0,
    // so you can't simply say drawRect(x, y, dx, dy) if dx or dy
    // might be negative.  here are versions that work with any
    // (x,y), (x2,y2) popints.
    private void drawRectSafe(Graphics g, int x, int y, int x2, int y2) {
        g.drawRect(Math.min(x, x2), Math.min(y, y2),
                   Math.abs(x - x2), Math.abs(y - y2));
    }
    private void fillRectSafe(Graphics g, int x, int y, int x2, int y2) {
        g.fillRect(Math.min(x, x2), Math.min(y, y2),
                   Math.abs(x - x2), Math.abs(y - y2));
    }
}
