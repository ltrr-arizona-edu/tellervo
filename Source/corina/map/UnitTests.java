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
    public void testDistanceTo() {
        try {
            // distance to myself is zero
            Location l = new Location("38*30'N 33*45'E");
            assertEquals(l.distanceTo(l), 0);

            // distance to somebody else is >0
            Location l2 = new Location("48*30'N 33*45'E");
            assert(l.distanceTo(l2) > 0);

            // functional requirements say it's to be rounded to the
            // nearest 10km, too.
            assert(l.distanceTo(l2) % 10 == 0);
        } catch (NumberFormatException nfe) {
            fail();
        }
    }
}
