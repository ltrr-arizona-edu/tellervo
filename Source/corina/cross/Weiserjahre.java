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

import corina.Sample;

public class Weiserjahre extends Cross {

    // ASSUMES fixed has weiserjahre.  there's no check: a npe gets
    // fired if it doesn't have any.

    public Weiserjahre(Sample fixed, Sample moving) {
    	super(fixed, moving);
    }

    public String getFormat() {
        // FIXME: this should be a pref, as well
        return System.getProperty("corina.cross.weiserjahre.format", "0.0%");
    }

    // same as trend?  (this is old-style trend, even -- very obsolete!)
    public boolean isSignificant(double score, int overlap) {
        return score > 0.65;
    }
    public double getMinimumSignificant() {
        return 0.65;
    }

    public String getName() {
	return msg.getString("weiserjahre");
    }

    public double compute(int offset_fixed, int offset_moving) {
	// value = (# trends synchronous with signature years) /
	//         (# signature years in overlap)

	int i = offset_fixed;
	int j = offset_moving;

	int synchroTrends = 0;
	int totalSigs = 0;

	// do magic in here
	while (i<fixed.data.size()-1 && j<moving.data.size()-1) {

	    // number of samples: need n>3
	    int n = ((Integer) fixed.count.get(i)).intValue();

	    // fraction with increasing trend: need pct<25% OR pct>75%
	    double pct = ((Number) fixed.incr.get(i)).doubleValue() / (double) n;

	    // signature year?  (j==0 is bad, too)
	    if (n>3 && (pct<=0.25 || pct>=0.75) && j>0) {
		// count it
		totalSigs++;

		// i'm ASSUMING that the trend of the fixed sample is
		// the trend of the majority of the elements, NOT the
		// trend of the sum.  if i'm backwards, it's easy
		// enough to fix: copy "compute moving trend", and
		// replace s/moving/fixed/ and s/j/i/

		// compute fixed trend
		int fixedTrend = 0;
		if (pct <= 0.25)
		    fixedTrend = -1;
		else if (pct >= 0.75)
		    fixedTrend = +1;

		// compute moving trend
		int movingTrend = 0;
		if (((Number) moving.data.get(j-1)).intValue() < ((Number) moving.data.get(j)).intValue())
		    movingTrend = +1;
		else if (((Number) moving.data.get(j-1)).intValue() > ((Number) moving.data.get(j)).intValue())
		    movingTrend = -1;

		// do they match?
		if (fixedTrend == movingTrend)
		    synchroTrends++;
	    }

	    // next one
	    i++;
	    j++;
	}

        // if there were no significant intervals, call it 0.0.
        if (totalSigs == 0)
            return 0.0;

	return (double) synchroTrends / (double) totalSigs;
    }
}
