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
package edu.cornell.dendro.corina.util;

import java.lang.ref.WeakReference;

import edu.cornell.dendro.corina.Year;

/**
 * WeakReference wrapper around the most common years
 * For performance improvements in Editor
 * 
 * @author Lucas Madar
 */

@SuppressWarnings("unchecked")
public final class Years {
	/**
	 * Not instantiable
	 */
	private Years() {
	}
	
	/**
	 * Given a String year, get a Year object
	 * 
	 * @param value
	 * @return
	 * @throws NumberFormatException
	 */
	public static Year valueOf(String value) throws NumberFormatException {
		int y = Integer.parseInt(value.trim());
		
		// year can't be zero (BC/AD!)
		if(y == 0)
			throw new NumberFormatException();
		
		return valueOf(y);
	}
	
	/**
	 * Given an integer year, get a Year object
	 * 
	 * @param value
	 * @return
	 */
	public static Year valueOf(int value) {
		// outside of our range
		if(value < MIN_CACHE_YEAR || value >= MAX_CACHE_YEAR)
			return new Year(value);
		
		// subtract the minimum cache year to get an index
		int idx = value - MIN_CACHE_YEAR;
		Year y;
		
		// lazily load/create Years
		if(years[idx] == null || (y = years[idx].get()) == null) {
			y = new Year(value);	
			years[idx] = new WeakReference<Year>(y);
		}
		
		return y;
	}

	/**
	 * Wraps constructor from (row,col) pair. Assumes 10-year rows. The column should
	 * always be between 0 and 9, inclusive.
	 * 
	 * @param row
	 *            the row; row 0 is the decade ending in year 9
	 * @param col
	 *            the column; in row 0, year is the column
	 */
	public static Year forRowAndColumn(int row, int col) {
		int yy = 10 * row + col;
		if (yy == 0)
			return Year.DEFAULT;
		
		return valueOf(yy);
	}
	
	// these should cover the range of everything we use
	private final static int MIN_CACHE_YEAR = -1000;
	private final static int MAX_CACHE_YEAR = MIN_CACHE_YEAR + 4096;
	
	private static final WeakReference<Year> years[] = new WeakReference[MAX_CACHE_YEAR - MIN_CACHE_YEAR];	
}
