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

import java.io.StringReader;
import java.io.StreamTokenizer;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import java.awt.Dimension;

/**
   A (latitude, longitude) location on the earth.  (See also ISO 6709,
   which I ignore, because it's too much trouble to support too many
   standards.)

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Location implements Cloneable {

    /** Radius of the earth in kilometers, from Miller & Schroeer,
        <i>College Physics</i>, 6th ed: 6.36x10<sup>3</sup> km. */
    public final static double EARTH_RADIUS = 6.38e3; // 6.38e6m

    // ------------------------------------------------------------
    // +lat is N, +long is E
    public double latitude=0.0, longitude=0.0;

    // merely to make clone public ... a shallow copy of 2 doubles is fine.
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException cnse) {
            return null; // can't happen
        }
    }

    // ------------------------------------------------------------

    /** Default constructor.  0 degrees North, 0 degrees East. */
    public Location() { }

    // lat/long, given as degrees
    public Location(double latitude, double longitude) {
        this.latitude = latitude % 90;
        this.longitude = longitude % 180;
    }

    // lat/long, given as minutes
    public Location(int latitude, int longitude) {
        this.latitude = (latitude / 3600.) % 90;
        this.longitude = (longitude / 3600.) % 180;
    }

    /** Constructor, given String.  The input format is perfectly
	compatible with the output of toString(), but is lenient in
	case users type in a location and can't type the degree-sign
	-- any gap between numbers works, so "34*56' N 11 22W" should
	parse just fine.
	@param string the String to parse */
    public Location(String string) throws NumberFormatException {
	// parse as "%d %d [NS] %d %d [EW]" (scanf) but using [^0-9] instead of whitespace
	// so really something like "(%d[^0-9])+ [nsewNSEW] (%d[^0-9])+ [nsewNSEW]" (regex)

	int angle[] = new int[3];
	int n = 0; // slot to fill
	angle[0] = angle[1] = angle[2] = 0;

	boolean justHadDigit = false;

	for (int i=0; i<string.length(); i++) {
	    char c = Character.toUpperCase(string.charAt(i));

	    if (Character.isDigit(c)) {
		// reading a number
		angle[n] *= 10;
		angle[n] += Integer.parseInt(String.valueOf(c));
	    } else if (c == 'N' || c == 'S' || c == 'E' || c == 'W') {
		// finished an angle
		double x = 0.;
		double divider = 1.;
		for (int j=0; j<3; j++) {
		    x += (double) angle[j] / divider;
		    angle[j] = 0; // reset for next round
		    divider *= 60.;
		}
		n = 0;
		switch (c) {
		case 'N': latitude = x; break;
		case 'S': latitude = -x; break;
		case 'E': longitude = x; break;
		case 'W': longitude = -x; break;
		}

	    } else if (justHadDigit) {
		// just had a digit, but not now: increment pointer
		n++;
	    }

	    justHadDigit = Character.isDigit(c);
	}
    }

    /** Return this location as a nicely-formatted String.
	@return this location as a string */
    public String toString() {
	double tmpLat = Math.abs(latitude);
	double tmpLong = Math.abs(longitude);

	int latDegs = (int) tmpLat;
	int latMins = (int) Math.round((tmpLat - latDegs) * 60);
	int longDegs = (int) tmpLong;
	int longMins = (int) Math.round((tmpLong - longDegs) * 60);

	// at this point, i'd get returned stuff like "29*60' E".
	// this is easily solved with some simple normalization.
	if (latMins == 60) {
	    latDegs++;
	    latMins = 0;
	}
	if (longMins == 60) {
	    longDegs++;
	    longMins = 0;
	}

	// hemispheres
	char latHemi = (latitude > 0.0 ? 'N' : 'S');
	char longHemi = (longitude > 0.0 ? 'E' : 'W');

	// return it
	return latDegs + "\u00B0" + latMins + "'" + latHemi + " " +
	    longDegs + "\u00B0" + longMins + "'" + longHemi;
    }

    // test equality by comparing strings.  not the fastest, but this
    // takes care of the round-off problem of comparing doubles.
    public boolean equals(Object l) {
        return (l instanceof Location) && toString().equals(l.toString());
    }

    /** Compute the surface distance between two Locations, rounded to
	the nearest 10km.
	@param loc measure distance to this Location
	@return the distance in kilometers */
    public int distanceTo(Location loc) {
        // the two points, in (x,y,z) coordinates
        Vector3 p1 = new Vector3(this);
        Vector3 p2 = new Vector3(loc);

        // sides of a triangle; third side is straight-line distance between the locations
        double a = EARTH_RADIUS;
        double b = EARTH_RADIUS;
        double c = p1.distanceTo(p2);

        // angle at center between two locations
        double C = Math.acos((a*a+b*b-c*c) / (2*a*b));

        // surface distance between locations
        double dist = EARTH_RADIUS * C;

        // round to ten km (just fixed: write unit test.)
        return roundTo(dist, 10);
    }

    // roundTo(x, 10) rounds to the nearest 10, for example
    private int roundTo(double value, int place) {
        return place * (int) Math.round(value / (double) place);
    }

    private final static int NEAR = 100; // "near" is 100km -- make me customizeable?
    public boolean isNear(Location loc) {
        return (loc != null) && (distanceTo(loc) <= NEAR);
    }

    public static Location midpoint(Location a, Location b) {
        // latitude doesn't wrap around, just take the mean
        Location l = new Location();
        l.latitude = (a.latitude + b.latitude) / 2;

        // longitude, otoh, does wrap around, so pick the short way
        l.longitude = (a.longitude + b.longitude) / 2;
        if (Math.abs(a.longitude - b.longitude) >= 180) {
            l.longitude += 180;
            l.longitude %= 180;
        }
        return l;
    }
}
