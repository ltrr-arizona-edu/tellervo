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

package edu.cornell.dendro.corina.cross;

import java.text.DecimalFormat;

import edu.cornell.dendro.corina.Year;

/**
   A histogram for crossdates.

   <p>(Actually, except for one constructor, there's nothing
   crossdate-specific about this.)</p>

   <p>It splits up the incoming data into a fixed number of buckets,
   and lets you find out how many buckets there are, the fullest
   bucket, and the range of and number of items in each bucket.</p>

<pre>
   rarely-happens bugs:

   BUG: if zero-length array, all accessor calls will fail
        (what's the correct output, then?)
   BUG: what if the entire data array is infinities (or NaNs)?

   can't-happen (since i'm only using it for crossdates) bugs:

   BUG: what if the entire data array is NaNs?
   BUG: what if there's a -infinity value?
   (BUG?: what if there are any negative values?)
</pre>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Histogram {
    // format string for values/scores
	private DecimalFormat format;

	// intermediates
	private float low, step;
	private boolean hasInfty;

	// output
	private int buckets[];

	/** The number of buckets to split data into. */
	public static final int NUMBER_OF_BUCKETS = 30;

	/**
	 * Make a histogram out of the scores of a crossdate. Assumes the crossdate
	 * was already run.
	 * 
	 * @param crossdate
	 *            the crossdate of scores to count
	 */
	public Histogram(Cross crossdate) {
		this(allScores(crossdate), crossdate.getFormat());
	}

	// copy all of the scores from this crossdate into a float array.
	private static float[] allScores(Cross c) {
		float x[] = new float[c.getRange().span()];
		
		for (Year y = c.getRange().getStart(); c.getRange().contains(y); y = y.add(1))
			x[y.diff(c.getRange().getStart())] = c.getScore(y);
		
		return x;
	}

	/**
	 * Make a histogram out of any data. Also provide a format string, like
	 * DecimalFormat uses, which will be used for printing the ranges of the
	 * buckets.
	 * 
	 * @see java.text.DecimalFormat
	 * @param data
	 *            the data to analyze, as an array of floats
	 * @param format
	 *            the way to format data values, for DecimalFormat
	 */
	public Histogram(float data[], String format) {
		// copy format string
		this.format = new DecimalFormat(format);

		// number of inputs
		int n = data.length;

		// compute high/low/step
		{
			float high;
			if (data.length == 0) {
				buckets = new int[0];
				return;
			}
			low = Float.POSITIVE_INFINITY;
			high = Float.NEGATIVE_INFINITY;
			for (int i = 0; i < n; i++) {
				float x = data[i];

				// there are some special cases to watch out for:

				if (Float.isInfinite(x) && x > 0) {
					// infinity. what to do? first, ignore it when
					// computing low and high. make an extra bucket
					// on top, called "high - infinity".
					hasInfty = true;

				} else if (Float.isNaN(x)) {
					// NaN. they're just weird, and they don't really
					// fit in any buckets, so i'll just ignore them.
					// (no crossdate should return any NaNs, anyway.)

				} else {
					// a normal value
					low = Math.min(low, x);
					high = Math.max(high, x);
				}
			}
			if (hasInfty)
				step = (high - low) / (NUMBER_OF_BUCKETS + 1);
			else
				step = (high - low) / NUMBER_OF_BUCKETS;
		}

		// make the buckets
		buckets = new int[NUMBER_OF_BUCKETS]; // all zero

		// fill the buckets with data
		for (int i = 0; i < n; i++) {
			// get the next val
			float x = data[i];

			// place it in the proper bucket
			int target = (int) ((x - low) / step);

			// if rounding puts it out of the last bucket,
			// or it's an infinity, put it in the last bucket.
			if (target >= NUMBER_OF_BUCKETS)
				target = NUMBER_OF_BUCKETS - 1;

			buckets[target]++;
		}
	}

	// the number of entries in the fullest bucket, or -1(=not computed)
	private int fullest = -1;

	/**
	 * Get the number of items in the fullest bucket.
	 * 
	 * @return the number of items in the fullest bucket
	 */
	public int getFullestBucket() {
		// computed lazily; -1 means "not computed"
		// (because no bucket can contain -1 things, of course)
		if (fullest == -1) {
			for (int i = 0; i < buckets.length; i++)
				fullest = Math.max(fullest, buckets[i]);
		}
		return fullest;
	}

	/**
	 * Get the number of buckets. This is a compile-time constant.
	 * 
	 * @see Histogram#NUMBER_OF_BUCKETS
	 * @return the number of buckets
	 */
	public int getNumberOfBuckets() {
		return buckets.length;
	}

	private String memo[] = null;

	/**
	 * Get the range spanned by a bucket. This is returned as a string, in the
	 * format "a - b". The ends of the span are formatted in the provided
	 * format.
	 * 
	 * @param bucket
	 *            which bucket to look at
	 * @return the range spanned by that bucket, as a string "a - b"
	 */
	public String getBucketRange(int bucket) {
		// build memo, if necessary
		if (memo == null)
			memo = new String[buckets.length];

		// compute result for cache, if necessary
		if (memo[bucket] == null) {
			boolean isInfty = (hasInfty && bucket == buckets.length - 1);
			float a = low + step * bucket;
			float b = (isInfty ? Float.POSITIVE_INFINITY : low + step
					* (bucket + 1));
			memo[bucket] = format.format(a) + " - " + format.format(b);
			// "-" should really be "\u2014",
			// but my printer can't handle that yet,
			// so yours likely can't, either. :-(
		}

		// return it
		return memo[bucket];
	}

	/**
	 * Get the number of items in a bucket.
	 * 
	 * @param bucket
	 *            which bucket to look at
	 * @return the number of items in that bucket
	 */
	public int getBucketItems(int bucket) {
		return buckets[bucket];
	}
}
