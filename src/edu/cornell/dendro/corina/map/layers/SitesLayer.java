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

package edu.cornell.dendro.corina.map.layers;

import edu.cornell.dendro.corina.map.Layer;
import edu.cornell.dendro.corina.map.Point3D;
import edu.cornell.dendro.corina.map.Projection;
import edu.cornell.dendro.corina.map.View;
import edu.cornell.dendro.corina.map.LabelSet;

import edu.cornell.dendro.corina.site.Location;
import edu.cornell.dendro.corina.site.Site;
import edu.cornell.dendro.corina.site.SiteDB;

import edu.cornell.dendro.corina.util.ColorUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap; // why?
import java.util.Iterator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

/**
   A layer containing all the Sites on the map.

   <h2>Left to do:</h2>
   <ul>
     <li>Draw "selected" sites in the system hilite color (how to find out?)
     <li>Need to keep a list of "selected" sites, then!  (Idea: keep it in synch between
         the Map view, and the Sites view!)
     <li>Draw offsets (dragged labels) correctly -- if y<0,x<0, y>0,x>0, etc.
     <li>Figure out how to store offsets (dragged labels)
     <li>Figure out how to compute hits on the label / dot -- needs offsets
     <li>Get rid of the "tag mover" tool, and have the arrow drag both
     <li>Draw multiple sites in one location
     <li>Get showing/hiding sites working
     <li>Javadoc
     <li>Future: join/split sites in one location
     <li>Future: intelligently pick some offsets
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class SitesLayer extends Layer {

/*
 IDEA: visible sites are held here, not by MapPanel/component/whatever
 (though they hold a ref to me -- or will they?).
 when user toggles in sitelistpanel, it just calls show/hide here ...
 no, because tools need to know what's visible.  unless they're going
 to come to me for that stuff, i shouldn't hold all that.
 ah, but they should!
*/

    private LabelSet labels;
    
    // (javadoc) - WRITEME
    public SitesLayer(LabelSet labels) {
        // OLD: setSites(SiteDB.getSiteDB().sites); // !!!

        this.labels = labels;
    }

    /**
       Draw the sites.

       @param g2 the Graphics2D object to draw to
       @param r the Projection to use
    */
    @Override
	public void draw(Graphics2D g2, Projection r) {
	drawSites(g2, r);
    }

    // ****************************************
    // below here is some old crap out of MapPanel
    // which was used to draw sites.
    // it's not pretty.
    // ****************************************
    // TODO: need siteHash, somehow
    // TODO: need drawLabel()

    private void drawSites(Graphics2D g2, Projection r) {
        // normally, this would be in drawLabel(), but it's the same for all labels
        // (i.e., it's a function of view.zoom only), and i don't want to call it
        // /n/ times -- 1 time is faster.  (and profiling shows that drawing the
        // labels is one of the slowest parts of the map, so i'll take all the cycles
        // i can get.)
        setFontForLabel(g2, r.view);
        
        // draw all (visible) sites
        List allSites = SiteDB.getSiteDB().sites;
        Iterator iter = allSites.iterator(); // this iterator gives me all the sites
        /* WAS: Iterator iter = siteHash.values().iterator(); // this iterator gives me lists of sites */

        while (iter.hasNext() && !abort) {
            // WAS: List list = (List) iter.next();
            // WAS: Site top = (Site) list.get(0);
            Site site = (Site) iter.next();

            // if its location is null (unknown), skip it.
            if (site.getLocation() == null)
                continue;
            
            // if not visible, skip it
            // FIXME: does this only check the top one?
            if (!labels.isVisible(site))
                continue;

            g2.setColor(site.getSiteColor());
            r.project(site.getLocation(), p2);
            if (p2.getZ() < 0) // "invisible" -- this should be a result of render(), not a z<0
                continue;

            // convert the Point3D into a Point (!!!)
            pt.x = (int) p2.getX();
            pt.y = (int) p2.getY();

            // label it
            drawLabel(g2, pt, site, 1 /*WAS: list.size()*/, r.view);
        }
    }

    // make this GETfont, instead. -- ?
    // PERF: makes new font each time!
    private void setFontForLabel(Graphics g, View view) {
        //        g.setFont(new Font("sans-serif", Font.BOLD, (int)view.getZoom()*5));
        // g.setFont(new Font("sans-serif", Font.PLAIN, 9));
        int size = 9;
        if (view.getZoom() < 1.2)
            size = 8;
        if (view.getZoom() > 3)
            size = 10;
        g.setFont(new Font("sans-serif", Font.PLAIN, size));
    }

    // draw a label using this mappanel's offset hash
    private void drawLabel(Graphics2D g2, Point p, Site site, int numSites, View view) {
        // get offsets
	Offset o = getOffset(site.getLocation());

	// compute bubble point
	t.x = p.x + (int) (o.dist * view.getZoom() * Math.sin(o.angle));
	t.y = p.y - (int) (o.dist * view.getZoom() * Math.cos(o.angle));

	// call renderer to draw it for me
	drawLabelSR(g2, p, site, numSites, view, t, labels.isSelected(site));
    }
    private Point t = new Point();

    private Point3D p2 = new Point3D(); // ("p2" by historical accident) -- FIXME!

    private Point pt = new Point();

    /* private */ public static class Offset {
        public float angle = 0f;
        public float dist = 0f; // new!
    }
    private Offset getOffset(Location location) { // DUMMY METHOD
	return nullOffset;
    }
    private final Offset nullOffset = new Offset(); // singleton

    // SITES
    /*private*/ HashMap siteHash = new HashMap();
    
    /** Set your sites on this! */
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

    // ================================================
    // note!  code below here came from SiteRenderer.java.
    // it needs refactoring.
    // ================================================

    private static final Color MOSTLY_WHITE = ColorUtils.addAlpha(Color.white, 0.9f);
    private static final Color MOSTLY_BLACK = ColorUtils.addAlpha(Color.black, 0.9f); // TESTING!
    private static Dimension textSize = new Dimension(); // BUG: concurrency problems here?

    // timing:
    // -- as little as 1/3 ms per label for dots+rects
    // -- close to 1 ms per label for dots+rects+text
    // -- (i.e., 2/3 ms per label for text)
    // IDEA: move label (text part, not dot+line) into its own layer?
    public static void drawLabelSR(Graphics2D g2,
                                   Point p /* site point */, Site site, int numSites,
                                   View view /* used only for clipping */,
                                   Point t /* bubble center */,
                                   boolean selected) {

        // LEGACY: store color
        Color c = g2.getColor();

        // TESTING: move this far
        java.util.Random r = new java.util.Random(site.getCode().hashCode());
        int extraDx = 0; // r.nextInt(11) - 5;
        int extraDy = 0; // r.nextInt(5) - 2;

        // get the text, and its size (needed later)
        String text = getLabel(site, view);
        textSize.width = g2.getFontMetrics().stringWidth(text);
        textSize.height = g2.getFontMetrics().getAscent(); // USUALLY THE SAME!  is it slow?

        // draw a white rect
        g2.setColor(selected ? MOSTLY_BLACK : MOSTLY_WHITE);
        g2.fillRect(p.x + extraDx, p.y + extraDy, textSize.width + EPS, textSize.height + EPS);

        // draw the text
        g2.setColor(selected ? Color.white : Color.black);
        g2.drawString(text, p.x+EPS/2 + extraDx, p.y+EPS/2+textSize.height + extraDy);

        // draw a dot (colored)
        g2.setColor(c); // .darker());
        g2.fillOval(p.x-EPS/2, p.y-EPS/2, EPS, EPS);
        // NOTE: if you want to use a dot with color |c|, you're going to have
        // to darken it quite a bit first.

        // draw a line (also colored)
        g2.drawLine(p.x, p.y,
                    p.x + extraDx, p.y + textSize.height + EPS + extraDy);
        g2.drawLine(p.x + extraDx, p.y + textSize.height + EPS + extraDy,
                    p.x + textSize.width + EPS + extraDx, p.y + textSize.height + EPS + extraDy);
    }

    // TODO: i'll want the option of "code" or "name"
    private static String getLabel(Site site, View view) {
        String text = (view.getZoom() > 10 ? site.getName() : site.getCode());
        if (text == null)
            text = ""; // BUG: but isn't there a java2d bug that disallows even this?
        return text;
    }

    // need FONT-SETTING method here, too

    private static final int EPS = 4; // small value: diameter of the dot, and distance from dot to text
}
