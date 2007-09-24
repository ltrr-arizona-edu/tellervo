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

package corina.map;

import corina.map.layers.GridlinesLayer;
import corina.map.layers.MapLayer;
import corina.map.layers.LegendLayer;
import corina.map.layers.SitesLayer;

import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

// TODO: remember printer abort exception!  yes, but not here.
// TODO: allow printing list of (visible) sites, too?  hmm, no: if you want sites, go to that view and print those.

/**
   Print maps to a high-resolution printer.

   <p>This class implements Printable, so to use it, all you need to do is
   make a new one with

 <pre>
 Printable p = new MapPrinter(view, format);
 </pre>

   and pass that to Java's printing system.</p>

   <p>The Java graphics/printing system has a weird limitation: all of the
   drawing primitives only get you to point (1/72") resolution.  For most
   printing jobs (like a page of text), this is fine.  But our line maps
   have quite a bit more detail, and if each point is snapped to the nearest
   1/72" on the page, it looks really bad.</p>

   <p>The solution here is to make a new View, based on the View that we're
   passed, only with a larger area and zoomed in more, and then use
   Graphics2D's scale() call.  Then it all turns out ok.  (The only downside
   is that fixed-size things like the scale box become smaller, but that's
   much better than the alternative.  It could also be fixed, with some work.)

   <h2>Left to do:</h2>
   <ul>
     <li>Why does the c'tor need a PageFormat, if print() takes one, too?
     <li>Make it accept a Projection, too?  Aah, no, Projection is made from a View.  Need to pass a type-of-Projection.
     <li>The user's SitesLayer might have something different showing, so we'll need to pass that in, too.
         (Can I use the same layers they're already using?)
     <li>Zooming in more isn't perfect (legend gets smaller, etc.).  What I need is higher-resolution
         maps.  Is that hard to do?
     <li>Bug?: I set the stroke before drawing the layers, but don't the layers set their own strokes?
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class MapPrinter implements Printable {
    /**
       Make a new MapPrinter.
    
       @param view the View to print
       @param labels the LabelSet to use
       @param format the PageFormat to use
    */
    public MapPrinter(View view, LabelSet labels, PageFormat format) {
        this.format = format;

        this.labels = labels;
        
        this.view = (View) view.clone();  // wasteful later: need to decide if view is mutable!
    }

    private View view;

    private LabelSet labels;

    private PageFormat format;

    private final static float TARGET_DPI=300; // change me, if you like
    private final static float NORMAL_DPI=60; // normal print is 60dpi -- don't change me
    
    private final static float detail=TARGET_DPI/NORMAL_DPI;

    /**
       Print the map.  (A map is always exactly one page.)

       @param g the Graphics object to draw on
       @param format the PageFormat to use
       @param pageNr the page to print (only page 0 is actually printed)
       @return PAGE_EXISTS, for page 0, else NO_SUCH_PAGE
    */
    public int print(Graphics g, PageFormat format, int pageNr) throws PrinterException {
        // a map is always exactly one page
        if (pageNr > 0)
            return Printable.NO_SUCH_PAGE;

        // create my own view: zoom
        View detailedView = (View) view.clone();
        detailedView.setZoom(detailedView.getZoom() * detail);

        // then set the size -- scale up by |detail| to get |DPI|.
        // (if you want a square map, say: h = w = Math.min(h, w);)
        int w = (int) format.getImageableWidth();
        int h = (int) format.getImageableHeight();
        detailedView.size = new Dimension((int) (w*detail), (int) (h*detail));

        // create my own projection
        Projection r = Projection.makeProjection(detailedView);

        // use a small brush
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(0.5f));

        // offset so it's entirely on the page, and scale down to
        // make it look detailed but normal-sized
        final float dx = (float) format.getImageableX();
        final float dy = (float) format.getImageableY();
        g2.translate(dx, dy);
        g2.scale(1/detail, 1/detail);

        // print the layers to it
        printAllLayers(g2, r);

        return Printable.PAGE_EXISTS;
    }

    private void printAllLayers(Graphics2D g2, Projection r) {
        // make some layers
        Layer grid = new GridlinesLayer();
        Layer legend = new LegendLayer();
        Layer sitesLayer = new SitesLayer(labels);
        Layer mapLayer = new MapLayer();
        
        // draw them to |g2|
        grid.draw(g2, r);
        mapLayer.draw(g2, r);
        sitesLayer.draw(g2, r);
        legend.draw(g2, r);
    }
}
