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

// a 3-vector -- WHAT'S IT USED FOR?  i can't remember.
public class Vector3 {
    public double x, y, z; // non-private?  what was i thinking?

    public Vector3() {
        // nothing (then why do i exist?)
    }
    public Vector3(Vector3 old) { // REFACTOR: why isn't this clone()
        this.x = old.x;
        this.y = old.y;
        this.z = old.z;
    }
    public Vector3(double lat, double lon, double radius) {
        // ACK.  i can't just call setLocation() here, because i don't have a location!
        // and if Vector3(loc) doesn't call Vector3(double,double,double), what's the point of
        // having separate constructors?
        // -- DO I EVEN NEED THIS CONSTRUCTOR NOW?
        
        // convert angles
        lat = Math.toRadians(lat);
        lon = Math.toRadians(lon);

        // spherical->cartesian coordinates
        x = radius * Math.cos(lat) * Math.sin(-lon);
        y = radius * Math.sin(lat);
        z = radius * Math.cos(lat) * Math.cos(-lon);
    }
    public Vector3(Location l) {
        setLocation(l);
    }

    void setLocation(Location l) {
        // convert angles
        float lat = (float) Math.toRadians(l.latitude);
        float lon = (float) Math.toRadians(l.longitude);

        // spherical->cartesian coordinates
        x = Location.EARTH_RADIUS * Math.cos(lat) * Math.sin(-lon);
        y = Location.EARTH_RADIUS * Math.sin(lat);
        z = Location.EARTH_RADIUS * Math.cos(lat) * Math.cos(-lon);
    }

    // how many times is this implemented?  Scanner.java (ring width), MapPanel.java (threshold), Vector3 (???)
    float distanceTo(Vector3 p2) {
        // pythagorean distance -- AGAIN!
        float dx = (float) (this.x - p2.x);
        float dy = (float) (this.y - p2.y);
        float dz = (float) (this.z - p2.z);
        return (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
