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

package corina.cross;

/*
  the histogram sucks.  everybody just uses makeBuckets().  there's no
  "...above X" for open-ended crosses, like t-score.  and the
  abstraction is all wrong.
*/

public class Histogram {

    public static class Bucket {
	public double low, high;
	public int number=0;
	public double pct=0.0;
	public Bucket(double low, double high) {
	    this.low = low;
	    this.high = high;
	}
    }

    // buckets
    private Bucket[] buckets;

    // create a histogram for an already-run cross
    public Histogram(Cross c) {
	// get buckets
	buckets = c.getBuckets();

	// number of scores
	int n = c.data.length;

	// fill the buckets
	for (int i=0; i<n; i++) {

	    // fits in which bucket?
	    for (int j=0; j<buckets.length; j++) {
		if (c.data[i] > buckets[j].low && c.data[i] <= buckets[j].high) {
		    buckets[j].number++;
		}
	    }
	}

	// compute percentages
	for (int i=0; i<buckets.length; i++)
	    buckets[i].pct = (double) buckets[i].number / (double) n;
    }

    public Bucket[] getBuckets() {
        return buckets;
    }

    public static Bucket[] makeBuckets(double low, double increment, double high) {
        int n = (int) ((high-low) / increment);
        Bucket b[] = new Bucket[n];

        double l = low;
        for (int i=0; i<n; i++) {
            b[i] = new Bucket(l, l+increment);
            l += increment;
        }
        return b;
    }
}
