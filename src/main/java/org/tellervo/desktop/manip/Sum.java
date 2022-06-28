/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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
 *     Peter Brewer
 ******************************************************************************/

package org.tellervo.desktop.manip;

import java.io.IOException;
import java.util.ArrayList;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleType;
import org.tridas.interfaces.ITridasDerivedSeries;
import org.tridas.schema.TridasDerivedSeries;


/**
 A "sum" of two or more datasets is the average of those datasets.

 There are two sum() methods, corresponding to two ways in which
 sums are made:

 <ul>

 <li> sum(List) makes a master from a list of elements.  This
 corresponds to the New &gt; Sum menuitem.  The user chooses any
 number of samples to sum, and a new master is created.</li>

 <li> sum(Sample) reconstructs a master in-place from its
 elements.  This corresponds to the Sum &gt; Re-Sum menuitem.  The
 user already has a master, and either added/removed some
 elements, or changed some data in one of the elements.
 Re-summing returns the same master that was given.</li>

 </ul>

 @see ObsFileElement

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id$
 */
public class Sum {

	// TODO:
	// - make Undoable?  this probably isn't *that* useful.
	// - if exactly one file is indexed, say which it is (and vice versa) -- or 2? 3?...

	// load all elements, and stuff 'em into a buffer.
	// (OBSOLETE once element-sample is no longer an important distinction!)
	private static Sample[] loadIntoBuffer(ElementList elements) throws IOException {
		// count number of active elements
		int numActive = 0, numTotal = elements.size();
		for (int i = 0; i < numTotal; i++)
			if (elements.isActive(elements.get(i)))
				numActive++;

		// allocate buffer
		Sample buf[] = new Sample[numActive];

		// load active elements into buffer
		int numLoaded = 0;
		for (int i = 0; i < numTotal; i++)
			if (elements.isActive(elements.get(i)))
				buf[numLoaded++] = elements.get(i).load();

		// return array
		return buf;
	}

	/**
	   Compute the union of all the ranges of elements.  (Assumes
	   |elements| holds at least one element.)  For sums using the
	   intersection of the ranges instead, simply replace "union" with
	   "intersection" in the loop.

	 From the user's point of view, it would be better to
	 provide the option (for example):

	<pre>
	 (*) union (1001-1050)
	 ( ) intersection (1010-1036)
	</pre>

	 (with "intersection" dimmed if the range is empty).
	 */
	private static Range computeRange(Sample elements[]) {
		// yup, it's just (reduce #'range-union elements #'sample-range).
		Range range = elements[0].getRange();
		for (int i = 1; i < elements.length; i++)
			range = range.union(elements[i].getRange());
		return range;
	}

	/**
	   Are the elements all raw, or all indexed?

	   @param elements an array to test
	   @return true, iff all of the elements in the array use the
	   same units
	 */
	private static boolean consistentUnits(Sample elements[]) {
		// yup, it's just (apply #'= (map 'list 'sample-format elements)).
		if (elements.length == 0)
			return true;
		boolean isIndexed = elements[0].isIndexed();
		for (int i = 1; i < elements.length; i++)
			if (isIndexed != elements[i].isIndexed())
				return false;
		return true;
	}

	// files which should watch for these exceptions:
	// - Browser, Editor, FileMenu, XMenubar, UnitTests
	// TODO: when they can all handle it, change it to extend merely Exception.
	// (shouldn't i change this first, so the JSL is in full effect?)

	// an exception that means "there's a gap in the sum"
	@SuppressWarnings("serial")
	public static class GapInSumException extends IllegalArgumentException {
		@Override
		public String toString() {
			return "A year gap exists in the data you wish to sum; sum is not contiguous.";
		}
		// sum_error_gap
	}

	// an exception that means "you're trying to mix raw and indexed files, dork"
	@SuppressWarnings("serial")
	public static class InconsistentUnitsException extends IllegalArgumentException {
		@Override
		public String toString() {
			return "Inconsistent units; you may not mix raw and indexed data.";
		}
	}

	// load elements, sum them, and store into result (returns result, too)
	private static Sample sum(Sample result, ElementList elements) throws IOException {

		result.setSampleType(SampleType.SUM);

		// if it's not a derived series, it is now
		if(!(result.getSeries() instanceof ITridasDerivedSeries))
			result.setSeries(new TridasDerivedSeries());		
		
		// step 0: load all elements, and stuff 'em into a buffer
		Sample buf[] = loadIntoBuffer(elements);

		// special case: zero elements!
		if (buf.length == 0) {
			// "skip to step 6" would be nice...
			result.setRange(new Range()); // default empty range (1001-1000)
			result.setRingWidthData(new ArrayList<Number>());
			result.setCount(new ArrayList<Integer>());
			result.setWJIncr(new ArrayList<Integer>());
			result.setWJDecr(new ArrayList<Integer>());

			result.setElements(elements);
			result.setMeta("format", "R"); // let's say no data = raw
			result.setModified();
			result.fireSampleDataChanged();
			result.fireSampleMetadataChanged();
			return result;
		}

		// step 0.5: verify units (raw/indexed)
		if (!consistentUnits(buf))
			throw new InconsistentUnitsException();
		boolean isIndexed = buf[0].isIndexed(); // save units for later

		// HERE'S where i'd identify single (or a few) wrong-unit
		// samples.  how to record/report?

		// step 1: compute max range
		Range range = computeRange(buf);

		// step 2: make arrays (all 0's) for computation
		int n = range.getSpan();
		int[] data = new int[n];
		int[] count = new int[n];
		int[] incr = new int[n];
		int[] decr = new int[n];

		// step 3: for each sample, add size, inc count; do weiserjahre numbers, too
		for (int i = 0; i < buf.length; i++) {
			// "load" from buffer
			Sample s = buf[i];

			// index into the sum (data[]) that this element (s.data[]) starts
			int startIndex = s.getRange().getStart().diff(range.getStart());

			// each iteration:
			// -- prevData is s.data[elemIndex-1]
			// -- thisData is s.data[elemIndex]
			int prevData, thisData = -1;

			// elemIndex is the counter into s.data (the element)
			// sumIndex is nthe counter into data (the sum)
			int elemIndex = Math.max(0, range.getStart().diff(s.getRange().getStart()));
			int sumIndex = elemIndex + startIndex;

			while (elemIndex < s.getRange().getSpan() && sumIndex < range.getSpan()) {

				// this year's data; also copy thisData->prevData
				prevData = thisData;
				thisData = ((Number) s.getRingWidthData().get(elemIndex)).intValue();

				// add element's data; increment count
				data[sumIndex] += thisData;
				count[sumIndex]++;

				// wj (first year is always just 0/0)
				if (elemIndex > 0) {
					if (thisData > prevData)
						incr[sumIndex]++;
					else if (thisData < prevData)
						decr[sumIndex]++;
				}

				elemIndex++;
				sumIndex++;
			}
		}
		// (buf can get gc'd now)

		// step 4: if any count is 0, there's a gap => throw exception
		for (int i = 0; i < n; i++)
			if (count[i] == 0)
				throw new GapInSumException();

		// HERE i should say where the gap is, and what the whole
		// range is, and maybe even what samples are near the gap (?).

		// step 5: divide each data by each count: data[i] /= count[i]
		for (int i = 0; i < n; i++)
			data[i] = (int) Math.round((double) data[i] / count[i]);

		// step 6: set range, and copy array back into (list) result.data
		result.setRange(range);
		result.setRingWidthData(new ArrayList<Number>(n));
		result.setCount(new ArrayList<Integer>(n));
		result.setWJIncr(new ArrayList<Integer>(n));
		result.setWJDecr(new ArrayList<Integer>(n));
		for (int i = 0; i < n; i++) {
			result.getRingWidthData().add(new Integer(data[i]));
			result.getCount().add(new Integer(count[i]));
			result.getWJIncr().add(new Integer(incr[i]));
			result.getWJDecr().add(new Integer(decr[i]));
		}
		// (data,count,incr,decr can get gc'd now)

		// step 7: set elements, and misc meta
		result.setElements(elements);
		result.setMeta("format", isIndexed ? "I" : "R");
		// FIXME: combine species
		// FIXME: compute ++, sapwood, etc. information
		result.setModified();
		result.fireSampleDataChanged();
		result.fireSampleMetadataChanged(); // format, title (modified)

		// return it
		return result;
	}

	/**
	   Create a new sum from some elements.

	   @param e a list of 
	   @return a new master
	   @exception IOException if one of the samples wasn't able to be
	   loaded
	   @exception IllegalArgumentException if there would be a gap in
	   the sum, or if the units are inconsistent
	 */
	public static Sample sum(ElementList e) throws IOException {
		return sum(new Sample(), e);
	}

	/**
	   Re-Sum an existing master.

	   @param m the master to re-sum
	   @return the same master, re-summed
	   @exception IOException if one of the samples wasn't able to be
	   loaded
	   @exception IllegalArgumentException if there would be a gap in
	   the sum, or if the units are inconsistent
	 */
	public static Sample sum(Sample m) throws IOException {
		return sum(m, m.getElements());
	}
}
