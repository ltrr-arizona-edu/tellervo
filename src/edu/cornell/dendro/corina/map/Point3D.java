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

package edu.cornell.dendro.corina.map;

import edu.cornell.dendro.corina.tridas.Location;

/**
   A point in 3-space: (x, y, z).  Each component (x, y, z) is a float.

   <h2>Left to do:</h2>
   <ul>
     <li>Verify that a float is sufficient precision in the range I'm using (and document this)
     <li>Why is it so important that x/y/z be private?  java.awt.Point doesn't do this.
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Point3D {
    //
    // FIELDS
    //

    private float x;
    private float y;
    private float z;

    //
    // ACCESSORS
    //

    /**
       Get the x coordinate.
       @return the x coordinate
    */
    public float getX() {
        return x;
    }
    /**
       Get the y coordinate.
       @return the y coordinate
    */
    public float getY() {
        return y;
    }
    /**
       Get the z coordinate.
       @return the z coordinate
    */
    public float getZ() {
        return z;
    }

    /**
       Set the x coordinate to a new value.
       @param x the new x value
    */
    public void setX(float x) {
        this.x = x;
    }
    /**
       Set the y coordinate to a new value.
       @param y the new y value
    */
    public void setY(float y) {
        this.y = y;
    }
    /**
       Set the z coordinate to a new value.
       @param z the new z value
    */
    public void setZ(float z) {
        this.z = z;
    }
    
    //
    // CONSTRUCTORS
    //

    /**
       Make a new point at (0,0,0).
    */
    public Point3D() {
        // do nothing -- simply to allow null-arg construction
    }
    
    //
    // METHODS
    //
    
    /**
       Make a point from a Location (latitude, longitude).  The center of the earth is (0,0,0),
       and the earth is assumed to be spherical, i.e., this converts from spherical coordinates
       (Location) to rectangular coordinates (Point3D).

       WRITEME: which way to the axes point?

       @param location the location to use
    */
    public void setFromLocation(Location location) {
        // convert angles
        float lat = (float) Math.toRadians(location.getLatitudeAsDegrees());
        float lon = (float) Math.toRadians(location.getLongitudeAsDegrees());

        // spherical->cartesian coordinates
        x = (float) (Location.EARTH_RADIUS * Math.cos(lat) * Math.sin(-lon));
        y = (float) (Location.EARTH_RADIUS * Math.sin(lat));
        z = (float) (Location.EARTH_RADIUS * Math.cos(lat) * Math.cos(-lon));
    }

    /**
       Multiply each component by a scale factor.

       @param factor the factor to scale by
    */
    public void scale(float factor) {
        x *= factor;
        y *= factor;
        z *= factor;
    }

    /**
       Compute the (Pythagorean) distance between two points.
        
       @param point the point to measure to
       @return the distance between the two points
    */
    public float distanceTo(Point3D point) {
        // pythagorean distance -- yet again!
        float dx = this.x - point.x;
        float dy = this.y - point.y;
        float dz = this.z - point.z;
        return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
