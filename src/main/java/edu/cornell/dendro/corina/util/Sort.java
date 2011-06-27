/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
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

package edu.cornell.dendro.corina.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.Comparator;
import java.util.Collections;
import java.util.List;

/**
   A sorter for Lists of Objects.  (This is actually just a wrapper
   around <code>Collections.sort()</code>, but it's far more
   convenient.  It's inspired by Lisp's <code>(sort :key ...)</code>.)

   <p>Suppose you have the class:</p>

<pre>
class Point {
&nbsp;&nbsp;&nbsp;int x, y;
&nbsp;&nbsp;&nbsp;String label;
}
</pre>

   <p>If you have a List of Points, the standard way to sort is by
   making it implement Comparable, and calling Collections.sort():</p>

<pre>
class Point implements Comparable {
&nbsp;&nbsp;&nbsp;int x, y;
&nbsp;&nbsp;&nbsp;String label;

&nbsp;&nbsp;&nbsp;// compare by label
&nbsp;&nbsp;&nbsp;public int compareTo(Object o2) {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return label.compareTo(((Point) o2).label);
&nbsp;&nbsp;&nbsp;}
}

// sort Points by label
Collections.sort(listOfPoints);
</pre>

   <p>The downside of this is that you have to implement the
   Comparable interface, which in turn locks you in to sorting by only
   one field.  You can get around this by making your own Comparator
   object each time you want to sort but that's a lot of extra
   typing.</p>

   <p>With this class, you can sort it different ways (without
   implementing any interface) simply by saying:</p>

<pre>
Sort.sort(list, "x"); // sort by x-coordinates
Sort.sort(list, "y", true); // sort by y-coordinates, high-to-low
Sort.sort(list, "label"); // sort by label
</pre>

   <p>It also works for private fields, as long as they have
   accessors.  For example, if the class had been defined:</p>

<pre>
class Point {
&nbsp;&nbsp;&nbsp;public int x, y;
&nbsp;&nbsp;&nbsp;private String label;
&nbsp;&nbsp;&nbsp;public String getLabel() {
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return label;
&nbsp;&nbsp;&nbsp;}
}
</pre>

   <p>you could still sort by label with:</p>

<pre>
Sort.sort(list, "label");
</pre>

   <p>(Feature idea: allow also passing in a Comparator, like a
   Collator or a NaturalSort, so you can sort in other ways.)</p>

   <p>Note: these sort methods assume that the lists consist entirely
   of the same type of object, or at least that they all contain the
   requested field.  If the list contains different kinds of objects,
   some of them lacking the requested field or accessor method having
   different access, it may fail.  (The first element of the list is
   used to determine whether to sort-by-field or sort-by-method.)</p>

   @see java.util.List
   @see java.util.Collections
   @see java.lang.Comparable
   @see java.util.Comparator
   @see java.text.Collator
   @see edu.cornell.dendro.corina.util.NaturalSort

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Sort {
    // this class should never be instantiated
    private Sort() { }

    /** Sort a list from lowest to highest.
       @param data a list to sort
       @param field the field of the Objects in data to sort by
    */
    @SuppressWarnings("unchecked")
	public static void sort(List data, String field) throws IllegalArgumentException {
        sort(data, field, false);
    }

    /** Sort a list.
       @param data a list to sort
       @param field the field of the Objects in data to sort by
       @param decreasing if true, sort highest-to-lowest; else, lowest-to-highest
    */
    @SuppressWarnings("unchecked")
	public static void sort(List data, String field, boolean decreasing) throws IllegalArgumentException {
        // no data -> no need to sort (and also no way to try,
        // since i don't even know what type it would be)
        if (data.size() == 0)
            return;

        Class c = data.get(0).getClass();

        try {
            // possible bug: if data contains different classes of objects, data[0].class
            // might be the declarer of field |field| -- do i need to call
            // -- f = f.getDeclaringClass().getDeclaredField(field)?
            Field f = c.getDeclaredField(field);

	    // see if data[0].field is visible
	    try {
		f.get(data.get(0));

		// if it passes that, then f is visible to me.
		// so sort by the field.
		sortByField(data, f, decreasing);

	    } catch (IllegalAccessException iae) {

		// well, we can't see field |field|.  what else can we try?
		// we try a get<field>() method, of course!

                String methodName = makeAccessorName(field);

                try {
                    Method m = c.getMethod(methodName, new Class[] { });
		    // FIXME: what if this fails?

		    sortByMethod(data, m, decreasing);

		} catch (NoSuchMethodException nsme) {
		    // no get() method exists, abort
                    throw new IllegalArgumentException("No method '" + methodName + "()' exists " +
                                                       "in class " + c.getName() + "!");
		}

	    }
        } catch (NoSuchFieldException nsfe) {
            throw new IllegalArgumentException("No such field '" + field + " exists " +
                                               "in class " + c.getName() + "!");
        }
    }

    // given a field, return the name of its accessor.
    // e.g., "myField" => "getMyField"
    private static String makeAccessorName(String field) {
	return "get" +
	       Character.toUpperCase(field.charAt(0)) +
	       field.substring(1);
    }

    // sort |data| by |field|, high-to-low iff |decreasing|.
    @SuppressWarnings("unchecked")
	private static void sortByField(List data, Field field, boolean decreasing) throws IllegalArgumentException {
	final boolean reverse = decreasing;
	final Field f = field;

	Collections.sort(data, new Comparator() {
		public int compare(Object o1, Object o2) {
		    try {
			Comparable v1 = (Comparable) f.get(o1);
			Comparable v2 = (Comparable) f.get(o2);

			if (v1 == null && v2 == null) return 0;
			if (v1 == null) return (reverse ? +1 : -1);
			if (v2 == null) return (reverse ? -1 : +1);

			int x = v1.compareTo(v2);
			return (reverse ? -x : x);
		    } catch (IllegalAccessException iae) {
			// gah, nothing i can do here.
			// can't happen.  or, rather, almost can't happen.
                        throw new IllegalArgumentException("No access to field '" + f.getName() + "()'");
			// an IAE can be thrown here (doesn't need to be declared), and it'll clear to the top.
		    }
		}
	    });
    }

    // sort |data| by result of |method|, high-to-low iff |decreasing|
    @SuppressWarnings("unchecked")
	private static void sortByMethod(List data, Method method, boolean decreasing) throws IllegalArgumentException {
	final boolean reverse = decreasing;
	final Method m = method;

	Collections.sort(data, new Comparator() {
		public int compare(Object o1, Object o2) {
		    try {
			Comparable v1 = (Comparable) m.invoke(o1, new Object[] { });
			Comparable v2 = (Comparable) m.invoke(o2, new Object[] { });

			if (v1 == null && v2 == null) return 0;
			if (v1 == null) return (reverse ? +1 : -1);
			if (v2 == null) return (reverse ? -1 : +1);

			int x = v1.compareTo(v2);
			return (reverse ? -x : x);
		    } catch (IllegalAccessException iae) {
			// gah, nothing i can do here.
			// can't happen.  or, rather, almost can't happen.
                        throw new IllegalArgumentException("No access to method '" + m.getName() + "()'");
		    } catch (InvocationTargetException ie) {
			throw new IllegalArgumentException("Error invoking method '" + m.getName() + "()'");
		    }
		}
	    });
    }
}
