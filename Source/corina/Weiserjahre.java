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

package corina;

/**
   Some useful utility functions for dealing with Sample's incr and
   decr fields.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Weiserjahre {

    public static boolean isSignificant(Sample s, int i) {
	int total = ((Number) s.count.get(i)).intValue();

	if (total < 4)
	    return false;

	double incr = ((Number) s.incr.get(i)).doubleValue() / (double) total;
	double decr = ((Number) s.decr.get(i)).doubleValue() / (double) total;

	// at least, or more than?  (< vs <=)
	return (incr > 0.75 || decr > 0.75);
    }
    public static boolean isSignificant(Sample s, Year y) {
	return isSignificant(s, y.diff(s.range.getStart()));
    }

    public static String toString(Sample s, int i) {
	int incr = ((Number) s.incr.get(i)).intValue();
	int decr = ((Number) s.decr.get(i)).intValue();
	return incr + (isSignificant(s, i) ? "*" : "/") + decr;
    }

    public static String toString(Sample s, Year y) {
	return toString(s, y.diff(s.range.getStart()));
    }

    // 10/36, 9 => "  10/36  "; if width is even, all hell breaks loose.
    public static String toStringFixed(Sample s, int i, int width, char c) {
	String incr = s.incr.get(i).toString();
	String decr = s.decr.get(i).toString();

	while (incr.length() < width/2)
	    incr = ' ' + incr;
	while (decr.length() < width/2)
	    decr = decr + ' ';

	return incr + c + decr;
    }

    // default char is '*','/'
    public static String toStringFixed(Sample s, int i, int width) {
	return toStringFixed(s, i, width, isSignificant(s, i) ? '*' : '/');
    }
}
