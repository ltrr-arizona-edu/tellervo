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

import corina.site.Site;

import java.io.IOException;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

// a Printable for Maps
public class MapPrinter implements Printable {

    // this interface doesn't feel ideal.  how about one static method?

    private Site s1=null, s2[]=null; // sites -- BAD INTERFACE
    public void setS1(Site s) {
	s1 = s;
    }
    public void setS2(Site s[]) {
	s2 = s;
    }

    private PageFormat pf = new PageFormat();

    // set pageformat
    public void setPageFormat(PageFormat pf) {
        this.pf = pf;
    }

    // create a new MapPrinter from lat,long,zoom
    private View view;
    public MapPrinter(View v) throws IOException {
        // same view, but blown up by some factor
        view = (View) v.clone();
        view.zoom *= detail;
    }

    // extra detail factor -- should compute me from DPI on the page.
    // no, i can't find out the DPI of the printer, so assume 300DPI:
    // it's pretty good, and most people won't be using 600+DPI
    // printers, and if you care that much, well, you probably care
    // enough to hack the source yourself to fix it.
    private final static float DPI=300;
    private final static float detail=DPI / 60; // std is 60dpi

    public int print(Graphics g, PageFormat format, int pageNr) throws PrinterException {
        // currently, maps are only one page (and probably forever: if
        // you want a bigger map, get a bigger printer)
        if (pageNr > 0)
            return Printable.NO_SUCH_PAGE;

        // set the size! -- scale up by |detail| to get |DPI|
        int w = (int) pf.getImageableWidth();
        int hh = (int) pf.getImageableHeight();
        // h = w = Math.min(h, w); // if you wanted a square map
        view.size = new Dimension((int) (w*detail), (int) (hh*detail));

        // create a renderer for me
        Renderer r = new RectangularRenderer(view);

        // 2d graphics -- small brush
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(0.5f));

        // turn off double-buffering here?  i think only Components
        // and JComponents double-buffer, not all Graphics and
        // Graphics2D uses.

        // offset everything by this much
        final float dx = (float) pf.getImageableX();
        final float dy = (float) pf.getImageableX();

        // --- LIFTED FROM MAPPANEL
        // drawing the gridlines: a simple loop for now (much better: start at r.loc, go out each way until !vis)
        Color gc = Pallette.getColor(Map.makeGridline(false, 0, 0)); // eh?
        if (gc != null)
            g2.setColor(gc);
        for (int angle=0; angle<360; angle+=Map.STEP) {
            for (int rise=-90; rise<90; rise+=10) { // gridlines every 10 degrees
                for (int orient=0; orient<=1; orient++) {
                    Map.Header gg = Map.makeGridline(orient==0, angle, rise);
                    if (gg.isVisible(r) != Renderer.VISIBLE_NO) { // REDUNDANT: isVisible projects the corners -- see below, too
                        Vector3 v1 = r.project(gg.getInsideCorner());
                        Vector3 v2 = r.project(gg.getOutsideCorner());
                        drawDetailed(g2, dx, dy, new int[] { (int)v1.x, (int)v2.x }, new int[] { (int)v1.y, (int)v2.y });
                    }
                }
            }
        }

        // drawing the map
        for (int i=0; i<Map.headers.length; i++) {
            Map.Header h = Map.headers[i];
            int vis = h.isVisible(r);
            if (vis == Renderer.VISIBLE_NO) // use a switch, with fall-through?
                continue;

            // ok, we know we're going to draw something.
            Color c = Pallette.getColor(h);
            if (c == null)
                continue; // ...maybe...
            g2.setColor(c);

            if (vis == Renderer.VISIBLE_YES) {
                Map.Data d = h.getData();
                int x[] = new int[d.x.length], y[] = new int[d.y.length];
                for (int j=0; j<d.x.length; j++) {
                    Vector3 v = r.project(new Location(d.y[j], d.x[j]));
                    x[j] = (int) v.x;
                    y[j] = (int) v.y;
                }
                drawDetailed(g2, dx, dy, x, y);
            } else if (vis == Renderer.VISIBLE_POINT) {
                // score!  this whole segment is one pixel, so don't even bother to load it.
                Vector3 v = r.project(h.getInsideCorner()); // REDUNDANT: isVisible() projects both corners -- why can't i get at that result?
                drawDetailed(g2, dx, dy, new int[] { (int)v.x, (int)v.x }, new int[] { (int)v.y, (int)v.y });
            }
        }
        // --- END LIFTED FROM MAPPANEL

        // draw dots for sites, all in red (added for carol, 1-feb-2002)
        int EPS = 5; // dot diameter
        if (s2 != null) {
            for (int i=0; i<s2.length; i++) {
                g2.setColor(Color.red);

                if (s2[i].location == null) continue;

                Vector3 p2 = r.project(s2[i].location);
                if (p2.z < 0) continue;
                g2.fillOval((int) (dx + ((int)p2.x-EPS/2)/detail),
                            (int) (dy + ((int)p2.y-EPS/2)/detail),
                            EPS, EPS);
            }
        }

        /*
         // sites
         g2.setFont(new Font("serif", Font.BOLD, 16));
         g2.setColor(new Color(0.0f, 0.6f, 0.0f));
         if (s1 != null) {
             Point p1 = map.getPointForLocation(s1.location);
             g2.fillOval((int) dx + (int) ((p1.x-7/2)/detail), (int) dy + (int) ((p1.y-7/2)/detail), 7, 7); // radius=7
             g2.drawString(s1.code, dx + p1.x/detail, dy + p1.y/detail);

             // /n/ other sites -- s2
             if (s2 != null)
                 for (int i=0; i<s2.length; i++) {
                     Point p2 = map.getPointForLocation(s2[i].location);
                     g2.fillOval((int) dx + (int) ((p2.x-7/2)/detail), (int) dy + (int) ((p2.y-7/2)/detail), 7, 7); // radius=7
                     g2.drawString(s2[i].code, dx + p2.x/detail, dy + p2.y/detail);

                     // p1-p2 line
                     g2.drawLine((int) dx + (int) (p1.x/detail), (int) dy + (int) (p1.y/detail),
                                 (int) dx + (int) (p2.x/detail), (int) dy + (int) (p2.y/detail));
                 }
         }
         */

        // return ok
        return Printable.PAGE_EXISTS;
    }

    private void drawDetailed(Graphics2D g2, float dx, float dy, int x[], int y[]) {
        // draw segment.  must use GeneralPath, because
        // drawPolyline() only takes ints, and 60/72dpi would suck.
        // (so much for abstraction)
        GeneralPath p = new GeneralPath();
        p.moveTo(dx + ((float) x[0]) / detail, dy + ((float) y[0]) / detail);
        for (int i=1; i<x.length; i++)
            p.lineTo(dx + ((float) x[i]) / detail, dy + ((float) y[i]) / detail);
        g2.draw(p);
    }
}
