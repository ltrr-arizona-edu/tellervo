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

import java.text.DecimalFormat;

public class Histogram {
    // input
    private double low, step;

    // output
    private int buckets[];

    // better inteface: Histogram(String formatString, double[] data)
    // Histogram(Cross) calls this(c.getFormat(), c.data)
    public Histogram(Cross c) {
        this(c.data, c.getFormat());
    }
    private DecimalFormat fmt; // format string for values/scores
    public Histogram(double data[], String fmt) {
        // copy format string
        this.fmt = new DecimalFormat(fmt);

        // number of inputs
        int n = data.length;

        // number of buckets
        int numberOfBuckets = 30;

        // set it up -- just make 20 buckets for now
        {
            double high;
            low = high = data[0];
            for (int i=1; i<n; i++) {
                low = Math.min(low, data[i]);
                high = Math.max(high, data[i]);
            }
            step = (high - low) / numberOfBuckets;
        }

        // make the buckets
        buckets = new int[numberOfBuckets]; // all zero

        // run it
        for (int i=0; i<n; i++) {
            // get the next val
            double x = data[i];

            // place it in the proper bucket
            int target = (int) ((x - low) / step);
            if (target == numberOfBuckets) // ???
                target = numberOfBuckets-1;
            buckets[target]++;
        }
    }

    // the number of entries in the fullest bucket.
    // (don't bother remembering this; it's only used once.)
    public int getFullestBucket() {
        int n=buckets.length;
        int max=0;
        for (int i=0; i<n; i++)
            max = Math.max(max, buckets[i]);
        return max;
    }

    // there's PROBABLY no reason for bucket to be its own class,
    // so here are some access-by-bucket methods.
    // i can always refactor later if need be.

    public int countBuckets() {
        return buckets.length;
    }

    private String memo[]=null;
    public String getRange(int bucket) {
        // make sure it's a legit bucket
        if (bucket<0 || bucket>=buckets.length)
            throw new ArrayIndexOutOfBoundsException();

        // build memo, if necessary
        if (memo == null)
            memo = new String[buckets.length];

        // compute result
        String result;
        if (memo[bucket] == null) {
            result = fmt.format(low+step*bucket) + " - " + fmt.format(low+step*(bucket+1));
            memo[bucket] = result;
        } else {
            result = memo[bucket];
        }
        
        // return it
        return result;
    }
    public int getNumber(int bucket) {
        return buckets[bucket];
    }
}
