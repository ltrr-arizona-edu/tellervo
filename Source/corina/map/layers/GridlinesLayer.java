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

package corina.map.layers;

import corina.map.Layer;
import corina.map.Projection;
import corina.map.Location;
import corina.map.Point3D;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Point;

/**
   A map layer which draws gridlines.
 
   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class GridlinesLayer extends Layer {
    /** Make a new gridline layer. */
    public GridlinesLayer() {
        // (this method exists only to hold the javadoc tag)
    }
    
    /*
      TODO:
      -- gridlines don't always reach the edges -- oops.
      -- when that's working, turn on clipping again
      -- doesn't draw lons
      -- doesn't draw even lats in western hemisphere!
      -- get rid of |horizontal| arg
      -- draw more gridlines, if zoomed more, or fewer, if further out
      ---- idea: every 1/5/10, so there's at least 2, and no more than 5?
    */

    private static final char DEGREES = '\u00B0';

    private static final Font FONT = new Font("sansserif", Font.PLAIN, 10);
    private static final Color COLOR = Color.gray;
    private static final Stroke HAIRLINE = new BasicStroke(1); // always use hairline?

    /**
        Draw the gridlines.
        
        @param g2 the Graphics2D object to draw to
        @param r the projection to use
    */
    public void draw(Graphics2D g2, Projection r) {
	// DISABLED: clip(g2, r);
	drawGridlines(g2, r);
	// DISABLED: unclip(g2);
    }
    // given zoom,location,width/height, draw gridlines on buf

    // **************************************************

    // draw a label, at (x,y), maybe horizontal, on g2, representing l1.  ack!
    private void label(int x, int y, boolean horizontal, Graphics2D g2, Location l1) {
	int value = (int) (horizontal ? l1.getLatitudeAsDegrees() : l1.getLongitudeAsDegrees());
	String compass; // eep!  want a way to do part of a toString()
	if (horizontal)
	    compass = (value > 0 ? "N" : (value < 0 ? "S" : "N/S"));
	else
	    compass = (value > 0 ? "E" : (value < 0 ? "W" : "E/W"));
	value = Math.abs(value);

	if (!horizontal) { // BETTER: use slope of gridline at this point
	    x += 15;
	    g2.rotate(Math.toRadians(90), x, 0);
	}
	g2.drawString(value + DEGREES + compass, x, y);
	if (!horizontal)
	    g2.rotate(Math.toRadians(-90), x, 0);
    }

    // i have no idea what this method does.
    // you would think it interpolates something, but it doesn't.
    private int interpolate(int x1, int y1, int x2, int y2, int x) {
        return (y1 + y2) / 2 - 5;
    }

    private void drawGridlines(Graphics2D g2, Projection r) {

	// WAS: get grid color from Pallette: 
	// Pallette.getColor(Map.makeGridline(false, 0, 0));

	g2.setColor(COLOR);
	g2.setFont(FONT);
	g2.setStroke(HAIRLINE);

        // TODO: draw labels better

	// north
	pt.x = r.view.size.width/2;
	pt.y = 0;
	r.unproject(pt, loc);
	int LAT_MAX = (int) loc.getLatitudeAsDegrees(); // BUG: casting ok? -- round up?

	// south
	pt.x = r.view.size.width/2;
	pt.y = r.view.size.height;
	r.unproject(pt, loc);
	int LAT_MIN = (int) loc.getLatitudeAsDegrees(); // BUG: casting ok? -- round down?

	// west
	pt.x = 0;
	pt.y = r.view.size.height/2;
	r.unproject(pt, loc);
	int LON_MAX = (int) loc.getLongitudeAsDegrees(); // BUG: casting ok? -- round up?

	// east
	pt.x = r.view.size.width;
	pt.y = r.view.size.height/2;
	r.unproject(pt, loc);
	int LON_MIN = (int) loc.getLongitudeAsDegrees(); // BUG: casting ok? -- round down?

	{
	    int tmp = LON_MAX;
	    LON_MAX = LON_MIN;
	    LON_MIN = tmp;
	}

	// draw gridlines every EVERY degrees
	final int EVERY = 5;

	// draw straight lines this many degrees long
	final int STEP = 3;

        // horizontal gridlines
        for (int lat=LAT_MIN-LAT_MIN%EVERY; lat<=LAT_MAX; lat+=EVERY) {
            drawHorizontalGridline(g2, r, lat, LON_MIN, LON_MAX, EVERY);
            if (abort)
                return;
        }
 
        // vertical gridlines
        for (int lon=LON_MIN-LON_MIN%EVERY; lon<=LON_MAX; lon+=EVERY) {
            drawVerticalGridline(g2, r, lon, LAT_MIN, LAT_MAX, EVERY);
            if (abort)
                return;
	}
    }

    // TODO: draw labels
    // TODO: compute bounds better
    // TODO: (put clipping back in)
    // TODO: rewrite/remove label()
    // TODO: remove interpolate()
    
    private void drawHorizontalGridline(Graphics2D g2, Projection r,
                                        int latitude,
                                        int longitudeStart, int longitudeEnd,
                                        int step) {
        boolean justStarted = true;
        
        // latitude is always the same
        a.setLatitudeAsDegrees(latitude);

        for (a.setLongitudeAsDegrees(longitudeStart);
             a.getLongitudeAsDegrees() <= longitudeEnd;
             a.setLongitudeAsDegrees(a.getLongitudeAsDegrees() + step)) {
            // project this point
            r.project(a, point);

            // move- or line-to this point.
            if (justStarted) {
                justStarted = false;
            } else {
                g2.drawLine((int) lastPoint.getX(), (int) lastPoint.getY(),
                            (int) point.getX(), (int) point.getY());
            }

            // REFACTOR: make a Point3D.copy(lastPoint,point).  er, make that Point3D.copy().
            // uh, no, it should just be a Point2D, anyway...
            lastPoint.setX(point.getX());
            lastPoint.setY(point.getY());

            if (abort)
                return;
        }
    }

    private void drawVerticalGridline(Graphics2D g2, Projection r,
                                      int longitude,
                                      int latitudeStart, int latitudeEnd,
                                      int step) {
        boolean justStarted = true;

        // longitude is always the same
        a.setLongitudeAsDegrees(longitude);

        for (a.setLatitudeAsDegrees(latitudeStart);
             a.getLatitudeAsDegrees() <= latitudeEnd;
             a.setLatitudeAsDegrees(a.getLatitudeAsDegrees() + step)) {

            // project this point
            r.project(a, point);

            // move- or line-to this point.
            if (justStarted) {
                justStarted = false;
            } else {
                g2.drawLine((int) lastPoint.getX(), (int) lastPoint.getY(),
                            (int) point.getX(), (int) point.getY());
            }

            lastPoint.setX(point.getX());
            lastPoint.setY(point.getY());

            if (abort)
                return;
        }
    }

    // temporary variables used by drawGridlines() which i don't want to re-allocate
    // each time.

    private Point pt = new Point();
    private Location loc = new Location();

    private Point3D point = new Point3D();
    private Point3D lastPoint = new Point3D();
    
    private Location a = new Location();
}
