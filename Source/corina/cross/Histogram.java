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
    private boolean hasInfty;

    // output
    private int buckets[];

    public Histogram(Cross c) {
        this(c.data, c.getFormat());
        // barf here if c wasn't run yet?
    }
    private DecimalFormat fmt; // format string for values/scores
    public Histogram(double data[], String fmt) {
        // copy format string
        this.fmt = new DecimalFormat(fmt);

        // number of inputs
        int n = data.length;

        // number of buckets
        int numberOfBuckets = 30;

        // specias case: max(data) = infty.  solution?
        // -- if there's an infty, ignore when computing low/high
        // -- add an extra bucket on the top, called "high - \infty"
        // no scores can be negative, but if they did, i'd probably need a special case for that, too

        // set it up -- use a fixed number of buckets for now
        {
            double high;
            low = high = data[0];
            for (int i=1; i<n; i++) {
                double x = data[i];
                if (Double.isInfinite(x) && x>0) {
                    hasInfty = true;
                } else if (Double.isNaN(x)) {
                    // heck, this shouldn't happen, but if it does, let's just ignore it.
                } else {
                    low = Math.min(low, x);
                    high = Math.max(high, x);
                }
            }
            if (hasInfty)
                step = (high - low) / (numberOfBuckets + 1);
            else
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
            if (target >= numberOfBuckets) // for when round-up puts it out of the last bucket, or infty
                target = numberOfBuckets-1;
            buckets[target]++;
        }
    }

    // the number of entries in the fullest bucket.
    private int fullest = -1;
    public int getFullestBucket() {
        // -1 means "not computed" (because no bucket can contain -1 things, of course)
        if (fullest == -1) {
            int n=buckets.length;
            for (int i=0; i<n; i++)
                fullest = Math.max(fullest, buckets[i]);
        }
        return max;
    }

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
            if (hasInfty && bucket==buckets.length-1)
                result = fmt.format(low+step*bucket) + " - " + fmt.format(Double.POSITIVE_INFINITY);
            else
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
