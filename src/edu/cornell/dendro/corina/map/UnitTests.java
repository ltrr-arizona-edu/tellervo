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

import corina.site.Location;
import junit.framework.TestCase;

public class UnitTests extends TestCase {
    public UnitTests(String name) {
        super(name);
    }

    //
    // testing Location.java
    //
    public void testStringConstructor() {
        try {
            // make a location
            Location l = new Location("38*30'N 33*45'E");

            // convert it to a string, and back
            Location l2 = new Location(l.toString());

            // equal?
            assertEquals(l, l2);
        } catch (NumberFormatException nfe) {
            fail();
        }
    }
    public void testLatitudeSandwiching() {
        Location l = new Location();

        // make sure 45 degrees doesn't get touched
        l.setLatitudeAsSeconds(45 * 3600);
        assertEquals(45 * 3600, l.getLatitudeAsSeconds());

        // make sure 95 degrees becomes 90 degrees
        l.setLatitudeAsSeconds(95 * 3600);
        assertEquals(90 * 3600, l.getLatitudeAsSeconds());

        // make sure -105 degrees becomes -90 degrees
        l.setLatitudeAsSeconds((-105) * 3600);
        assertEquals((-90) * 3600, l.getLatitudeAsSeconds());
    }
    public void testLongitudeWrapping() {
        Location l = new Location();

        // make sure 90+360 degrees wraps to 90 degrees.
        // (let's just say within 1 degree of correct.  FP math is weird.)
        l.setLongitudeAsDegrees(90 + 360);
        float longitude = l.getLongitudeAsDegrees();
        assertTrue(Math.abs(longitude - 90) < 1);

        // try a negative one: -5 - 360*5 should wrap to -5.
        l.setLongitudeAsSeconds((-5 - 360*5)*3600);
        assertEquals((-5)*3600, l.getLongitudeAsSeconds());
    }

    public void testISO6709Parsing() {
        try {
            // putting it into 6709
            Location l = new Location("11*30'45''N 20*40'50''E");
            String iso = l.toISO6709();
            assertEquals(iso, "+113045+0204050/");
            // BROKEN!  why?

            // getting it out of 6709
            Location l2 = new Location("+113045+0204050/");
            assertEquals(l, l2);

            // getting it out of 6709, other variants
            // WRITEME
        } catch (NumberFormatException nfe) {
            fail();
        }
    }
    
    public void testDistanceTo() {
        try {
            // distance to myself is zero
            Location l = new Location("38*30'N 33*45'E");
            assertEquals(l.distanceTo(l), 0);

            // distance to somebody else is >0
            Location l2 = new Location("48*30'N 33*45'E");
            assertTrue(l.distanceTo(l2) > 0);

            // functional requirements say it's to be rounded to the
            // nearest 10km, too.
            assertEquals(l.distanceTo(l2) % 10, 0);
        } catch (NumberFormatException nfe) {
            fail();
        }
    }
}
