package corina.map;

import corina.map.Map.Header;

import java.awt.Point;
import java.awt.Dimension;

// idea: make renderer an abstract class, call this SphericalRenderer, (put matrix inside it?), make a make/useRenderer(r) method, add other renderers...

public class SphericalRenderer extends Renderer {
    // what we're looking at (input only)
//    /* MAKE ME private */ View view;
    
    // method: Point render(Location)
    // project my (latitude,longitude) onto a width-by-height bitmap, using rotation matrix rot
    private final static double FAKE_EARTH_RADIUS = 2500.; // (actual size is too big for most displays.)
    private double rot[][]; // rotation matrix, used to project() locations
    public Vector3 project(Location loc) { // BAD: this shouldn't return a VECTOR3 -- it's unnecessary, and bad for abstraction.  use POINT?
        // Vector3() does spherical->rectangular conversion
        Vector3 vv = new Vector3(loc.latitude, loc.longitude, FAKE_EARTH_RADIUS);
        double xyz[][] = new double[][] { { vv.x, vv.y, vv.z } };

        // invariant: v.x^2+v.y^2+v.z^2 = FAKE_EARTH_RADIUS^2

        // rotate the sucker into place (maintains invariant)
        xyz = Matrix.multiply(xyz, rot);

        // maybe later:
        //        return new Point(v[0][0]+view.size.width/2, v[1][0]+view.size.height/2);

        // translate to move center from (0,0) to (width/2,height/2)
        Vector3 v = new Vector3();
        v.x = xyz[0][0] + view.size.width/2;
        v.y = xyz[0][1] + view.size.height/2;
        v.z = xyz[0][2]; // people only like v.z for his sign
        return v;
    }

    // method: Location unrender(Point)
    // determine (lat,long) from (x,y)
    private static final int EPSILON = 2; // find location to within |EPSILON| pixels of |pt|
    public Location unrender(Point pt) {
        // quick-n-dirty: binary search!
        // (this fails if zrot!=0)
        // might fail if zoom<<10 (lat/long aren't very vert/horiz then), but it seems to work
        // in testing, it takes max 1ms, which is plenty fast for user interaction.  WFM.

        // start with the center: we'll be jumping around, so make a copy
        Location loc = (Location) view.center.clone();

        // jump by this many degrees; less each time
        double jump = 15;

        // if we can't find it in 24 steps, something's wrong (i've
        // seen as high as 21 in a quick test).
        for (int i=0; i<24; i++) {
            // project
            Vector3 tst = project(loc);

            // good?
            if (Math.abs(pt.x-tst.x)<EPSILON && Math.abs(pt.y-tst.y)<EPSILON)
                return loc;

            // no?  update test
            if (pt.x < tst.x)
                loc.longitude -= jump;
            else if (pt.x > tst.x)
                loc.longitude += jump;

            if (pt.y < tst.y)
                loc.latitude += jump;
            else if (pt.y > tst.y)
                loc.latitude -= jump;

            // jump half as far next time
            jump *= .7; ///= 2;
        }

        // none found
        return null;
    }

    // RENAME THIS METHOD!  something like reset() or rerender() or ...
    // NO!  this method is called so rarely, and renderer holds so little state, just make a new renderer each time!  (separate renderer and view!)
    public SphericalRenderer(View v) {
        // copy the view
        view = v;

        // make rotation matrix
        // REFACTOR: THIS BELONGS IN VIEW or MATRIX!
        rot = Matrix.makeRotateX(-view.center.latitude); // BACKWARDS
        rot = Matrix.multiply(Matrix.makeRotateY(view.center.longitude), rot); // BACKWARDS, AND NEGATIVE
        rot = Matrix.multiply(Matrix.makeRotateZ(180), rot); // is there a better way?  (it's upside-down!)
        Matrix.scale(rot, view.zoom);
    }
}
