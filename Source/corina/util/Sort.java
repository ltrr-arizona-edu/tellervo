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

package corina.util;

import java.lang.reflect.Field;

import java.util.Comparator;
import java.util.Collections;
import java.util.List;

/**
   A sorter for Lists of Objects.  (This is actually just a wrapper
   around <code>Collections.sort()</code>, but it's far more convenient.)

   <p>Suppose you have the class:</p>

<pre>
class Point {
   int x, y;
   String label;
}
</pre>

   If you have a List of Points, you can sort it different ways simply by saying:

<pre>
Sort.sort(list, "x"); // sort by x-coordinates
Sort.sort(list, "y", true); // sort by y-coordinates, high-to-low
Sort.sort(list, "label"); // sort by label
</pre>

   (With the Collections class, you'd have to write a Comparator
   object for each of these.)

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class Sort {

    // this class should never be instantiated
    private Sort() { }

    /** Sort a list from lowest to highest.
       @param data a list to sort
       @param fieldName the field of the Objects in data to sort by
    */
    public static void sort(List data, String fieldName) throws IllegalArgumentException {
        sort(data, fieldName, false);
    }

    /** Sort a list.
       @param data a list to sort
       @param fieldName the field of the Objects in data to sort by
       @param decreasing if true, sort highest-to-lowest; else, lowest-to-highest
    */
    public static void sort(List data, String fieldName, boolean decreasing) throws IllegalArgumentException {
        // no data -> no need to sort (and also no way to try,
        // since i don't even know what type it would be)
        if (data.size() == 0)
            return;
        
        try {
            // possible bug: if data contains different classes of objects, data[0].class
            // might be the declarer of field fieldname -- do i need to call
            // -- f = f.getDeclaringClass().getDeclaredField(fieldName)?
            Class c = data.get(0).getClass();
            final Field f = c.getDeclaredField(fieldName); // final because my anonymous class needs it

            final boolean reverse = decreasing;

            Collections.sort(data, new Comparator() {
                public int compare(Object o1, Object o2) {
                    try {
                        Comparable v1 = (Comparable) f.get(o1);
                        Comparable v2 = (Comparable) f.get(o2);
                        int x = v1.compareTo(v2); // bug: trouble here if v1 (or maybe v2) is null
                        return (reverse ? -x : x);
                    } catch (IllegalAccessException iae) {
                        // gah, nothing i can do here.
                        throw new IllegalArgumentException("no access to field! -- " + iae);
                    }
                }
            });
        } catch (NoSuchFieldException nsfe) {
            throw new IllegalArgumentException("no such field! --" + nsfe);
        }
    }
}
