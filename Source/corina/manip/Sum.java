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

package corina.manip;

import corina.Year;
import corina.Range;
import corina.Sample;
import corina.Element;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

   @see Element

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Sum {

    // TODO:
    // - make Undoable?  this probably isn't *that* useful.
    // - if exactly one file is indexed, say which it is (and vice versa) -- or 2? 3?...

    // i18n
    private static ResourceBundle msg = ResourceBundle.getBundle("ManipBundle");

    // load all elements, and stuff 'em into a buffer.  (OBSOLETE once element-sample is no longer an important distinction to make!)
    private static Sample[] loadIntoBuffer(List elements) throws IOException {
        // count number of active elements
        int numActive=0, numTotal=elements.size();
        for (int i=0; i<numTotal; i++)
            if (((Element) elements.get(i)).isActive())
                numActive++;

        // allocate buffer
        Sample buf[] = new Sample[numActive];

        // load active elements into buffer
        int numLoaded=0;
        for (int i=0; i<numTotal; i++)
            if (((Element) elements.get(i)).active)
                buf[numLoaded++] = ((Element) elements.get(i)).load();

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

     (*) union (1001-1050)
     ( ) intersection (1010-1036)

     (with "intersection" dimmed if the range is empty).
     */
    private static Range computeRange(Sample elements[]) {
        // yup, it's just (reduce #'range-union elements #'sample-range).
        Range range = elements[0].range;
        for (int i=1; i<elements.length; i++)
            range = range.union(elements[i].range);
        return range;
    }

    /** Are the elements all raw, or all indexed?  (Assumes |elements|
        holds at least one element.) */
    private static boolean consistentUnits(Sample elements[]) {
        // yup, it's just (apply #'= (map 'list 'sample-format elements)) -- no, don't cons...
        boolean isIndexed = elements[0].isIndexed();
        for (int i=1; i<elements.length; i++)
            if (isIndexed != elements[i].isIndexed())
                return false;
        return true;
    }

    // load elements, sum them, and store into result (returns result, too)
    private static Sample sum(Sample result, List elements) throws IOException {
        // step 0: load all elements, and stuff 'em into a buffer
        Sample buf[] = loadIntoBuffer(elements);

        // step 0.5: verify units (raw/indexed)
        if (!consistentUnits(buf))
            throw new IllegalArgumentException(msg.getString("sum_error_mixed"));
        boolean isIndexed = buf[0].isIndexed(); // save units for later

        // HERE'S where i'd identify single (or a few) wrong-unit
        // samples.  how to record/report?

        // step 1: compute max range
        Range range = computeRange(buf);

        // step 2: make arrays (all 0's) for computation
        int n = range.span();
        int[] data = new int[n];
        int[] count = new int[n];
        int[] incr = new int[n];
        int[] decr = new int[n];

        // step 3: for each sample, add size, inc count; do weiserjahre numbers, too
        for (int i=0; i<buf.length; i++) {
            // "load" from buffer
            Sample s = buf[i];

            // index into the sum (data[]) that this element (s.data[]) starts
            int startIndex = s.range.getStart().diff(range.getStart());

            // each iteration:
            // -- prevData is s.data[elemIndex-1]
            // -- thisData is s.data[elemIndex]
            int prevData, thisData=-1;

            // elemIndex is the counter into s.data (the element)
            // sumIndex is nthe counter into data (the sum)
            int elemIndex = Math.max(0, range.getStart().diff(s.range.getStart()));
            int sumIndex = elemIndex + startIndex;

            while (elemIndex<s.range.span() && sumIndex<range.span()) {

                // this year's data; also copy thisData->prevData
                prevData = thisData;
                thisData = ((Number) s.data.get(elemIndex)).intValue();

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
        for (int i=0; i<n; i++)
            if (count[i] == 0)
                throw new IllegalArgumentException(msg.getString("sum_error_gap"));

        // HERE i should say where the gap is, and what the whole
        // range is, and maybe even what samples are near the gap (?).

        // step 5: divide each data by each count: data[i] /= count[i]
        for (int i=0; i<n; i++)
            data[i] = (int) Math.round((double) data[i] / count[i]);

        // step 6: set range, and copy array back into (list) result.data
        result.range = range;
        result.data = new ArrayList(n);
        result.count = new ArrayList(n);
        result.incr = new ArrayList(n);
        result.decr = new ArrayList(n);
        for (int i=0; i<n; i++) {
            result.data.add(new Integer(data[i]));
            result.count.add(new Integer(count[i]));
            result.incr.add(new Integer(incr[i]));
            result.decr.add(new Integer(decr[i]));
        }
        // (data,count,incr,decr can get gc'd now)

        // step 7: set elements, and misc meta
        result.elements = elements;
        result.meta.put("format", isIndexed ? "I" : "R");
        // FIXME: combine species
        // FIXME: compute ++, sapwood, etc. information
        result.setModified();
        result.fireSampleDataChanged();
        result.fireSampleMetadataChanged(); // format, title (modified)

        // return it
        return result;
    }

    /** Create a new sum from some elements. */
    public static Sample sum(List e) throws IOException {
        return sum(new Sample(), e);
    }

    /** Re-Sum an existing master. */
    public static Sample sum(Sample m) throws IOException {
        return sum(m, m.elements);
    }
}
