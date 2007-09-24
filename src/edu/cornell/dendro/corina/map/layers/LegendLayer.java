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
import edu.cornell.dendro.corina.site.Location;

import edu.cornell.dendro.corina.gui.Bug;

import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

// TODO: rename?  it's just the scale, not really a legend.  (or: no, later it can be more full-featured)

/**
   A map layer which draws a scale.

   <p>The ends of the scale (0 and 10) have the longest ticks,
   the middle (5) scale is slightly shorter, and the others (1's)
   have the shortest ticks.  The scale measures some power-of-10
   kilometers (10 km, 100 km, etc.) based on what fits well on the
   map.</p>
 
   <p>Drawing this layer to the buffer takes 50ms or less (I think).</p>
 
   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class LegendLayer extends Layer {
    /**
       Make a new legend layer.
    */
    public LegendLayer() {
        // (this method is just a holder for javadoc tags)
    }

    /**
       Draw the scale.
    
       @param g2 the Graphics2D object to draw to
       @param r the projection to use
    */
    public void draw(Graphics2D g2, Projection r) {
	drawScale(g2, r);
    }

    // ************************************************************

    // constants
    private static final Color COLOR = new Color(255, 255, 127, 225); // mostly-opaque yellow
    private static final Stroke STROKE = new BasicStroke(0.5f);
    private static final Font FONT = new Font("serif", Font.PLAIN, 12);

    private void drawScale(Graphics2D g2, int dist) {
	// TODO: if p1/p2/dist are the same as they used to be, i
	// don't need to do anything here, and i can save a few dozen
	// milliseconds -- that's the case when the user just changes
	// longitude, for example

	// PERF: for "erase old" here i only need to erase a small
	// rectangle, not the whole thing.  be smart!  (i only have to
	// draw a small part, too -- why am i using a big honkin'
	// buffer?)

        // draw a nice yellow box
        g2.setStroke(STROKE);
        g2.setColor(COLOR);
        g2.fillRect(p1.x-10, p1.y-10, p2.x-p1.x+20, 30);
        g2.setColor(Color.black);
        g2.drawRect(p1.x-10, p1.y-10, p2.x-p1.x+20, 30);

        // draw the bar
        g2.setColor(Color.black);
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);

        // draw ticks going across
        float dx = (p2.x-p1.x) / 10f;
        float xi = p1.x;
        for (int i=0; i<=10; i++) {
	    // i'd drawLine() at xi, but drawline only takes ints;
	    // as a workaround, i'll use translate() which takes doubles.
	    g2.translate(xi, 0);
	    g2.drawLine(0, p1.y,
			0, p1.y+tickSize(i));
	    g2.translate(-xi, 0);

            xi += dx;
	}

        // draw "100 km" on it
        g2.setFont(FONT);
        // BUG: this isn't centered or anything...
        String text = dist + " km";
        int w = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (p1.x+p2.x)/2 - w/2, p1.y + 15);
    }

    private int tickSize(int tickNr) {
        if (tickNr % 10 == 0)
            return 10;
        if (tickNr % 5 == 0)
            return 8;
        return 5;
    }

    private void drawScale(Graphics2D g2, Projection r) {
        try {

	    // figure out a good distance to mark off: 100km isn't always ideal
	    // FIXME: extract method?
            int km = 100;
     // FIXME: is this loop bounded?
     // FIXME: make this do 5's, too (5, 10, 50, 100, 500, etc.)
            for (;;) {
                // (i avoid calling new here, for performance.)
                p1.x = 25;
                p1.y = r.view.size.height - 40;

                int dist = pixelsForDistanceAtPoint(r, p1, km);

                p2.x = p1.x + dist;
                p2.y = p1.y;

                if (dist < 20 && km < 1000)
                    km *= 10;
                else if (dist > 200 && km > 1)
                    km /= 10;
                else {
                    drawScale(g2, km);

                    break;
                }
            }
        } catch (Exception e) {
            // this method used to have all sorts of problems 
	    // -- it shouldn't any more, but just in case,
	    new Bug(e);
            // FIXME: remove this catch.  stupid stupid stupid...
        }
    }

    // temp vars used by drawScale() which i don't want to keep alloc'ing.
    private Point p1 = new Point(), p2 = new Point();

    // ----
    // (used to be in Projection.  what was i thinking?)
    private int pixelsForDistanceAtPoint(Projection r, Point p, float dist) {
        // put the latitude/longitude of |p| into leftLoc
        r.unproject(p, leftLoc);

        // figure out the radius of a circle around the earth at this latitude is.
        // (40,000 km is the size of the earth.)
        double radius = 40000. * Math.cos(Math.toRadians(leftLoc.getLatitudeAsDegrees()));

        // figure out how many degrees the distance |dist| km is at this latitude
        double deg = (dist / radius) * 360.; // deg for |dist| km
        deg = Math.abs(deg); // (just in case)

        // make a new location |deg| over from the first one
        rightLoc.setLatitudeAsDegrees(leftLoc.getLatitudeAsDegrees());
        rightLoc.setLongitudeAsDegrees(leftLoc.getLongitudeAsDegrees() + (float) deg);

        // project that location to a point
        r.project(rightLoc, rightVec);

        // return the distance between the two points
        return (int) (rightVec.getX() - p.x);
    }

    // temp vars for pixelsForDistanceAtPoint()
    private Location leftLoc = new Location(), rightLoc = new Location();
    private Point3D rightVec = new Point3D();
}
