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
   <p>A calendar year.  It normally acts similar to an integer, but
   skips the mythical "year 0".</p>

   <p>In <code>Year</code> math:</p>

   <ul>
     <li>-1 + 1 = 1</li>
     <li>2 - 4 = -3</li>
   </ul>

   <p>Years, like Numbers and Strings, are immutable, so they are not
   Cloneable (there's no reason for them to be).</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public final class Year implements Comparable {
    /** The default year: 1001. */
    public static final Year DEFAULT = new Year(1001);

    /** Holds the year value as an <code>int</code>. */
    private final int y;

    /**
       Default constructor.  Uses <code>DEFAULT</code> as the year.

       @see #DEFAULT
    */
    public Year() {
        y = DEFAULT.y;
    }

    /**
       Constructor for <code>int</code>s.  Uses <code>DEFAULT</code>
       as the year if an invalid value is passed.

       @param x the year value, as an int
       @see #DEFAULT
    */
    public Year(int x) {
        y = (x == 0 ? DEFAULT.y : x);
    }

    /**
       Constructor from (row,col) pair.  Assumes 10-year rows.  The
       column should always be between 0 and 9, inclusive.

       @param row the row; row 0 is the decade ending in year 9
       @param col the column; in row 0, year is the column
    */
    public Year(int row, int col) {
        int yy = 10 * row + col;
        if (yy == 0)
            yy = DEFAULT.y; // should this be 1?
        y = yy;
    }

    /**
       Constructor from String.  No AD/BC; reads it like C's
       <code>scanf(" %d ", &y)</code> would.

       @exception NumberFormatException if the String cannot be
       parsed, or is equal to zero
       @see java.lang.String
    */
    public Year(String s) throws NumberFormatException {
        y = Integer.parseInt(s.trim());
        if (y == 0)
            throw new NumberFormatException();
    }

    /**
       Constructor from String.  No AD/BC; reads it like C's
       <code>scanf(" %d ", &y)</code> would.  This constructor is for
       zero-year-systems, if <code>zys</code> is true, i.e., -5 means
       6 BC.

       @exception NumberFormatException if the String cannot be parsed
       @see java.lang.String
    */
    public Year(String s, boolean zys) throws NumberFormatException {
        int yy = Integer.parseInt(s.trim());

        // back up a year, if this system assumed a zero-year
        if (zys && yy<=0)
            yy--;
        y = yy;
    }

    /**
       Convert to a String.  No "AD"/"BC"; simply the integer value.

       @return this year as a String
       @see java.lang.String
    */
    public String toString() {
        return String.valueOf(y);
    }

    /**
       This method always throws UnsupportedOperationException.  It's
       not implemented, and don't even think about implementing it
       yourself!  It encourages being lazy and bypassing Year's
       methods to just deal with ints.  And that defeats the whole
       purpose of having Years.  So I'll just disallow it.  You don't
       need it anyway.  If you really need the int for some reason I
       can't imagine, you can always do
       <code>Integer.parseInt(y.toString())</code>.  That way you know
       you're doing it to get the int, and not for imagined
       performance or convenience reasons.

       @return never returns
       @exception UnsupportedOperationException always!
    */
    public int intValue() {
        // i pity th' fool who tries to use intvalue!
        throw new UnsupportedOperationException();
    }

    /**
       Return true, iff this is year 1.  (This actually comes up
       fairly often.)

       @return true iff this is year 1
    */
    public boolean isYearOne() {
        return (y == 1);
    }

    /**
       The maximum (later) of two years.

       @return the later of two years
    */
    public static Year max(Year y1, Year y2) {
        return (y1.y > y2.y ? y1 : y2);
    }

    /**
       The minimum (earlier) of two years.

       @return the earlier of two years
    */
    public static Year min(Year y1, Year y2) {
        return (y1.y < y2.y ? y1 : y2);
    }

    /**
       Adds (or subtracts, for negative values) some number of years,
       and generates a new Year object.

       @param dy the number of years to add (subtract)
       @see #diff
    */
    public Year add(int dy) {
        // copy, and convert to zys
        int r = y;
        if (r < 0)
            r++;

        // add dy
        r += dy;

        // convert back, and return
        if (r <= 0)
            r--;
        return new Year(r);
    }

    /**
       Calculate the number of years difference between two years.
       That is, there are this many years difference between
       <code>this</code> and <code>y2</code>; if they are equal, this
       number is zero.

       @param y2 the year to subtract
       @return the number of years difference between
       <code>this</code> and <code>y2</code>
       @see #add
    */
    public int diff(Year y2) {
        // copy, and convert to zys
        int i1 = y;
        if (i1 < 0)
            i1++;

        int i2 = y2.y;
        if (i2 < 0)
            i2++;

        // subtract, and return
        return i1 - i2;
    }

    /**
       Computes <code>this</code> modulo <code>m</code>.  Always
       gives a positive result, even for negative numbers, so it is
       suitable for computing a grid position for a span of years.

       @param m base for modulo
       @return the year modulo <code>m</code>
    */
    public int mod(int m) {
        int r = y % m;
        if (r < 0)
            r += m;
        return r;
    }

    /**
       Determines what row this year would be, if years were in a
       grid 10 wide, with the left column years ending in zero.  Row
       0 is years 1 through 9.

       @return this year's row
       @see #column
    */
    public int row() {
        int z = y / 10;
        if (y<0 && y%10!=0)
            z--;
        return z;
    }

    /**
       Determines what column this year would be, if years were in a
       grid 10 wide, with the left column years ending in zero.

       Works for BC years, also:
       <table border="1" cellspacing="0">
	   <tr><th>column()</th><td>0</td><td>1</td><td>2</td><td>3</td><td>4</td>
	        <td>5</td><td>6</td><td>7</td><td>8</td><td>9</td></tr>
	   <tr><th rowspan="3">Year</th><td>-10</td><td>-9</td><td>-8</td><td>-7</td><td>-6</td>
	        <td>-5</td><td>-4</td><td>-3</td><td>-2</td><td>-1</td></tr>
	   <tr><td> </td><td>1</td><td>2</td><td>3</td><td>4</td>
	        <td>5</td><td>6</td><td>7</td><td>8</td><td>9</td></tr>
	   <tr><td>10</td><td>11</td><td>12</td><td>13</td><td>14</td>
	        <td>15</td><td>16</td><td>17</td><td>18</td><td>19</td></tr>
       </table>

       @return this year's column
       @see #row
    */
    public int column() {
        return mod(10);
    }

    /**
       Compares this and <code>o</code>.

       @see java.lang.Comparable
       @param o Object to compare
       @return >0, =0, or <0 if this is greater-than, equal-to, or less-than o
       @throws ClassCastException if o is not a Year
    */
    public int compareTo(Object o) {
        return this.y - ((Year) o).y;
    }

    /**
       Returns <code>true</code> if and only if <code>this</code> is
       equal to <code>y2</code>.

       @param y2 the year to compare <code>this</code> to
       @return <code>true</code> if <code>this</code> is equal to
       <code>y2</code>, else <code>false</code>
    */
    public boolean equals(Object y2) {
        return (y == ((Year) y2).y);
    }

    // since i define equals(), i need to define hashCode()
    public int hashCode() {
	// returning something based on y is logical, but returning y
	// itself might make people mistakenly think this is like
	// intValue(), so let's do something weird to it first.
	return y*y*y;
    }

    // THESE TWO METHODS ARE BUGGY AND NEED WORK!
    public Year cropToCentury() {
	return add(-mod(100)); // is this correct?
    }
    public Year nextCentury() {
	Year tmp = add(100); // COMPLETELY INCORRECT!
	if (tmp.y == 101)
	    return new Year(100);
	return tmp;
    }
}
