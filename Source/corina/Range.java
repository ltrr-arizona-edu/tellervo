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
   This class represents a range of years which can be redated
   safely.

   <p>Unfortunately, this data structure violates two [sic] major
   principles:</p>

   <ol>

     <li>Single-instance storage: we hope that the user of Range and
     Sample will always keep <code>Sample.data.size() ==
     Range.span()</code>, but there aren't any built-in ways to do
     this, so it's up to the user.  The problem is that
     <code>end</code> is a duplicate of <code>Sample.data.size() +
     start</code>.  (Resolution: nothing, for now; r1.overlap(r2) is
     just too incredibly useful and concise.  Perhaps it would be
     better to store only the ending year?)

   </ol>

   @see Year

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Range implements Comparable {

    /** Starting year of the Range. */
    private Year start;

    /** Ending year of the range. */
    private Year end;

    /** Construct a new empty range, starting at <code>Year.DEFAULT</code>.
	@see Year */
    public Range() {
	// this is only used by GraphFrame (which shouldn't use it)
	// and Sample
	start = Year.DEFAULT;
	end = start.add(-1);
    }

    /** Construct a new range, from y<sub>1</sub> to y<sub>2</sub>.
	(Neither year may be <code>null</code>.)
	@param y1 starting year
	@param y2 ending year */
    public Range(Year y1, Year y2) {
	// null argument?
	if (y1==null || y2==null)
	    throw new NullPointerException();

	this.start = y1;
	this.end = y2;

	// empty interval?
	if (start.compareTo(end) > 0) {
	    start = Year.DEFAULT;
	    end = start.add(-1);
	}
    }

    // start at |y|, length of |span| years, inclusive
    public Range(Year y, int span) {
	this.start = y;
	this.end = y.add(span-1);
    }

    /** Construct a range from a String.
	@param s the String */
    public Range(String s) {
	// (Grid.GridHandler.startElement is the only place this is used)

	// (ignore outside whitespace)
	String t = s.trim();

	// find the first dash that isn't t[0]
	int dash = t.indexOf('-', 1);

	// -- there must be a dash! --
	if (dash == -1)
	    throw new IllegalArgumentException();

	// y1 is everything before, y2 is everything after
	String y1 = t.substring(0, dash);
	String y2 = t.substring(dash+1);

	// construct years
	start = new Year(y1.trim());
	end = new Year(y2.trim());
    }

    /** Get the starting year of this range.
	@return the starting year */
    public Year getStart() {
	return start;
    }

    /** Get the ending year of this range.
	@return the ending year */
    public Year getEnd() {
	return end;
    }
    //
    // these 2 methods are all that's preventing Range from being immutable!
    //

    /** Set the starting year of the range, and adjust the ending year
	to maintain the same length.
	@param y new starting year for the range
	@see #redateEndTo */
    public Range redateStartTo(Year y) {
	return redateBy(y.diff(start));
    }

    public Range redateBy(int dy) {
	return new Range(start.add(dy), end.add(dy));
    }

    /** Set the ending year of the range, and adjust the start year to
	maintain the same length.
	@param y new ending year for the range
	@see #redateStartTo */
    public Range redateEndTo(Year y) {
	return redateBy(y.diff(end));
    }

    /** Return the number of years spanned by this range.  For
        example, the range 1001 - 1005 spans 5 years.
	@return the span of this range (difference between start and
	end, inclusive) */
    public int span() {
	return end.diff(start) + 1;
    }

    /** Return a simple string representation of the range,
        like "1001 - 1036".
        @return a string representation of the range */
    public String toString() {
        return start + " - " + end;
    }

    // "(1001 - 1036, n=36)"
    public String toStringWithSpan() {
        return "(" + start + " - " + end + ", n=" + span() + ")";
    }

    /** Return true if (and only if) the given year is inside the range, inclusive.
	@param y year to check 
	@return true if <code>y</code> is in the range, else false */
    public boolean contains(Year y) {
	return (start.compareTo(y) <= 0) && (y.compareTo(end) <= 0);
    }

    // is start of row?
    public boolean startOfRow(Year y) {
	return y.equals(start) || y.column()==0 || y.isYearOne();
    }

    // is end of row?
    public boolean endOfRow(Year y) {
	return y.equals(end) || y.column()==9;
    }

    /** Return the number of years overlap between this range and the
	given range.
	@param r range to compare
	@return number of years overlap */
    public int overlap(Range r) {
	return intersection(r).span();
    }

    // the intersection of this and |r|
    public Range intersection(Range r) {
	return new Range(Year.max(start, r.start),
			 Year.min(end, r.end));
    }

    // the union of this and |r|
    public Range union(Range r) {
	return new Range(Year.min(start, r.start),
			 Year.max(end, r.end));
    }

    /** Compare two ranges for equality.
	@param r range to compare with this
	@return true, if the ranges are equal, else false */
    public boolean equals(Object o) {
	Range r = (Range) o;
	return start.equals(r.start) && end.equals(r.end);
    }

    /** Compares this and o, for placing in fallback order.
	@param o Object to compare
	@return >0, ==0, or <0 if this is greater-than, equal-to, or less-than o
	@throws ClassCastException if o is not a Range */
    public int compareTo(Object o) {
	Range r2 = (Range) o;

	// since we know years are monotonically increasing -- the
	// only weirdness is the skip at 0 -- this can be done with a
	// ?:-statement and save a method call or two.

	int c1 = end.compareTo(r2.end);

	return (c1 != 0 ? c1 : start.compareTo(r2.start));
    }
}
