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

package corina.map.projections;

import corina.map.View;
import corina.map.Matrix;
import corina.map.Point3D;
import corina.map.Projection;
import corina.site.Location;

import java.awt.Point;

/**
   A spherical projection.

   <h2>Left to do</h2>
   <ul>
     <li>Clean up the backwards/negative messiness in here.
     <li>Write a straightforward unproject() that works
     <li>Clean up FAKE_EARTH junk.
     <li>Write javadoc.
     <li>Include links to web pages describing the spherical projection
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class SphericalProjection extends Projection {
    /**
       Make a new spherical projection.

       <p>Normally, you'll use the factory method in Projection instead of
       calling this directly.</p>

       @see Projection#makeProjection

       @param view the View to use
    */
    public SphericalProjection(View view) {
        super(view); // copy the view

        // make rotation matrix

        // BACKWARDS!
        // ALSO, view.center.* should be floats already -- don't cast!
        rotation = Matrix.makeRotateX((float) -view.center.getLatitudeAsDegrees());

        // BACKWARDS, AND NEGATIVE!
        rotation = Matrix.multiply(Matrix.makeRotateY((float) view.center.getLongitudeAsDegrees()), rotation);

        // is there a better way?  (it's upside-down!)
        rotation = Matrix.multiply(Matrix.makeRotateZ(180f), rotation);

        Matrix.scale(rotation, view.getZoom());
    }

    // (actual size is too big for most displays.)
    // FIXME: fake is 2500, real is 6380.  that's not too far off.  just use an adjustment, like rect proj does.
    private static final float FAKE_EARTH_RADIUS = 2500;
    
    // rotation matrix, used to project() locations
    private double rotation[][];

    private Point3D vv = new Point3D();
    
    public void project(Location location, Point3D point) {
        // put location into rectangular coordinates
        vv.setFromLocation(location);
        vv.scale(FAKE_EARTH_RADIUS / Location.EARTH_RADIUS);

        // copy to matrix for rotation
	xyz_tmp[0][0] = vv.getX();
	xyz_tmp[0][1] = vv.getY();
        xyz_tmp[0][2] = vv.getZ();

        // invariant: v.x^2+v.y^2+v.z^2 = FAKE_EARTH_RADIUS^2

        // rotate into place (maintains invariant -- actually, no, rot contains scale, too)
        xyz_tmp = Matrix.multiply(xyz_tmp, rotation);
	// PERF: -- but multiply() still allocs.  is this really a win?
	// DESIGN: need a multiply(a,b,c) method, then

        // maybe later: (once i'm smart enough not to return a Point3D)
        //        return new Point(v[0][0]+view.size.width/2, v[1][0]+view.size.height/2);

        // translate to move center from (0,0) to (width/2,height/2)
        point.setX((float) (xyz_tmp[0][0] + view.size.width/2));
        point.setY((float) (xyz_tmp[0][1] + view.size.height/2));
        point.setZ((float) xyz_tmp[0][2]); // people only like v.z for his sign
                          // WHEN PT IS REALLY A POINT, i can just say pt.translate(view.size.width/2, view.size.height/2);
    }
    
    private double xyz_tmp[][] = new double[1][3];

    // REWRITE: it should be fairly easy to do a direct (non-iterative) computation here
    private static final int EPSILON = 2; // (find location to within |EPSILON| pixels of |pt|)
    public void unproject(Point point, Location location) {
        // quick-n-dirty: binary search!
        // (assume zrot=0)
        // might fail if zoom<<10 (lat/long aren't very vert/horiz then), but it seems to work
        // in testing, it takes max 1ms, which is ok for user interaction.

        // start with the center: we'll be jumping around, so make a copy
        // was: Location loc = (Location) view.center.clone();

        // copy view.center to loc
        Location.copy(location, view.center);

        // jump by this many degrees; less each time
        float jump = 15f;

        // if we can't find it in 24 steps, something's wrong (i've
        // seen as high as 21 in a quick test).
        Point3D test = new Point3D();
        for (int i=0; i<24; i++) {
            // project
            project(location, test);

            // good?  done.
            if (Math.abs(point.x-test.getX())<EPSILON && Math.abs(point.y-test.getY())<EPSILON)
                return;

            // no?  update test
            if (point.x < test.getX())
                location.setLongitudeAsDegrees(location.getLongitudeAsDegrees() - jump);
            else if (point.x > test.getX())
                location.setLongitudeAsDegrees(location.getLongitudeAsDegrees() + jump);

            if (point.y < test.getY())
                location.setLatitudeAsDegrees(location.getLatitudeAsDegrees() + jump);
            else if (point.y > test.getY())
                location.setLatitudeAsDegrees(location.getLatitudeAsDegrees() - jump);

            // jump half as far next time
            jump *= .7; ///= 2;
        }

        // none found -- NO WAY TO SAY NULL HERE (but that's not bad...)
        // BETTER: return boolean?
        return;
    }
}
