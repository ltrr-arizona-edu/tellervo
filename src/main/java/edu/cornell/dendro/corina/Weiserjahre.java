/*******************************************************************************
 * Copyright (C) 2001-2011 Ken Harris and Peter Brewer.
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
 *     Ken Harris - initial implementation
 *     Lucas Madar 
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina;

import edu.cornell.dendro.corina.sample.Sample;

/**
   Some useful utility functions for dealing with Sample's incr/decr
   fields.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Weiserjahre {
    /**
       Test if an interval is significant, by Weiserjahre standards.
       That is, does it have at least 4 samples, with at least 75% of
       the trends agreeing?

       @param s the sample to check
       @param i which interval to check
       @return true, iff this interval is significant
    */
    public static boolean isSignificant(Sample s, int i) {
	int total = ((Number) s.getCount().get(i)).intValue();
	if (total < 4)
	    return false;

	int incr = ((Number) s.getWJIncr().get(i)).intValue();
	int decr = ((Number) s.getWJDecr().get(i)).intValue();
	return (incr >= decr*3 || decr >= incr*3);
    }
    public static boolean isSignificant(Sample s, Year y) {
	return isSignificant(s, y.diff(s.getRange().getStart()));
    }

    public static String toString(Sample s, int i) {
	int incr = ((Number) s.getWJIncr().get(i)).intValue();
	int decr = ((Number) s.getWJDecr().get(i)).intValue();
	return incr + (isSignificant(s, i) ? SIGNIFICANT : INSIGNIFICANT) + decr;
    }

    public static String toString(Sample s, Year y) {
	return toString(s, y.diff(s.getRange().getStart()));
    }

    // 10/36, 9 => "  10/36  "; if width is even, undefined result.
    // TODO: why don't i just write 'format for java?
    public static String toStringFixed(Sample s, int i, int width, String c) {
	String incr = s.getWJIncr().get(i).toString();
	String decr = s.getWJDecr().get(i).toString();

	while (incr.length() < width/2)
	    incr = ' ' + incr;
	while (decr.length() < width/2)
	    decr = decr + ' ';

	return incr + c + decr;
    }

    // default is '*','/'
    public static String toStringFixed(Sample s, int i, int width) {
	return toStringFixed(s, i, width, isSignificant(s, i) ? SIGNIFICANT : INSIGNIFICANT);
    }

    /** The string to separate the parts of a significant interval: <code>*</code>. */
    public final static String SIGNIFICANT = "*";
    /** The string to separate the parts of an insignificant interval: <code>/</code>. */
    public final static String INSIGNIFICANT = "/";
    // (should those be a trivial function, instead?)
}
