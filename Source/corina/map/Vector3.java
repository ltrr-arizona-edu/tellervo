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

// a 3-vector
class Vector3 {
    double x, y, z;
    Vector3() { }
    Vector3(double lat, double lon, double radius) {
        // convert angles
        lat = Math.toRadians(lat);
        lon = Math.toRadians(lon);

        // spherical->cartesian coordinates
        x = radius * Math.cos(lat) * Math.sin(-lon);
        y = radius * Math.sin(lat);
        z = radius * Math.cos(lat) * Math.cos(-lon);
    }
    Vector3(Location l) {
        this(l.latitude, l.longitude, Location.EARTH_RADIUS);
    }
    double distanceTo(Vector3 p2) {
        // pythagorean distance
        double dx = this.x - p2.x;
        double dy = this.y - p2.y;
        double dz = this.z - p2.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
