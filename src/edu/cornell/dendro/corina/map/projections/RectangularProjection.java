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

package edu.cornell.dendro.corina.map.projections;

import edu.cornell.dendro.corina.map.View;
import edu.cornell.dendro.corina.map.Point3D;
import edu.cornell.dendro.corina.map.Projection;
import edu.cornell.dendro.corina.site.Location;

import java.awt.Point;

// TODO: put web link here to somewhere that has info on the rectangular projection!

/**
   A rectangular projection.
 
   <p>This projection suffers from many inherent problems, but it has the
   advantage of being really simple to write and fast to run.  It's great
   for testing other parts of Corina, and profiling other projections.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class RectangularProjection extends Projection {
    /**
       Make a new rectangular projection.

       <p>Normally, you'll use the factory method in Projection instead of
       calling this directly.</p>

       @see Projection#makeProjection
     
       @param view the View to use
    */
    public RectangularProjection(View view) {
        super(view);
    }

    // (normal zoom doesn't look quite the same on spherical and rectangular, so adjust it)
    private static final int EXTRA_ZOOM = 16;

    /**
       Project a point.

       <p>Since the superclass still says it must project into a Point3D,
       it simply returns z=1 for all points.  The Projection class should
       be fixed so this method projects into normal Points.</p>
    
       @param location the Location to project
       @param point the point to project it into
    */
    public void project(Location location, Point3D point) {
        float zoom = view.getZoom() * EXTRA_ZOOM;

        // PERF: this is called thousands of times.  would it help to extract
        // view.size.width/2, view.center.longAsDeg, etc., into local vars, created in the c'tor?
        // i'm at 6 flops per line now, that would make this 4.  savings of 33%?  hey, who knows...
        // 33% off [15% of my execution time] is 5% off my total execution time.  i'll take it.
        // maybe better: this is a+(f(x)-b)*c = a+f(x)*c-b*c = (a-b*c) + f(x)*c.
        // do (a-b*c) once, then you just have f(x), *c, and +(a-b*c) to do for each location.
        // that's 3 flops, or 50% off project(), or 7% off total program time.  even better.
        // (just be sure to document it, because that's getting a little cs-222-obscure.)

        // PERF: counterpoint: project() isn't too slow, it's called too many times.
        // for example, to figure out of a segment is visible, i project 2 corners, see
        // if they're visible, and then go project either one corner again, or the whole
        // segment full of data.  i could save 30,000 project() calls if i play my cards
        // right.  for example: unproject() the boundaries of the currient View (visible
        // screen), and store them as ints (seconds of lat/long).  then i can check if a
        // segment is visible without rendering anything: just a couple int comparisons.
        
        point.setX(view.size.width/2 + (location.getLongitudeAsDegrees() - view.center.getLongitudeAsDegrees())*zoom);
        point.setY(view.size.height/2 - (location.getLatitudeAsDegrees() - view.center.getLatitudeAsDegrees())*zoom);
        point.setZ(+1); // it's a flat map, everything's theoretically visible. -- UGLY!
    }

    /**
       Unproject a point.

       @param point the Point to unproject.
       @param location the Location to store it in
    */
    public void unproject(Point point, Location location) {
        location.setLongitudeAsDegrees(view.center.getLongitudeAsDegrees() +
                                  (point.x - view.size.width/2)/(view.getZoom()*EXTRA_ZOOM));
        location.setLatitudeAsDegrees(view.center.getLatitudeAsDegrees() +
                                 (point.y - view.size.height/2)/(-view.getZoom()*EXTRA_ZOOM));
    }
}
