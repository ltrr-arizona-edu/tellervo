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

/**
   Class representing a "trend" algorithm for crossdating, whereby
   scores are the ratio of matching trends to total 1-year intervals.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class Trend extends Cross {

    // don't use me
    protected Trend() { }

    /** Create a new Trend from given samples.
	@param fixed fixed sample to use
	@param moving moving sample to use */
    public Trend(Sample fixed, Sample moving) {
    	super(fixed, moving);
    }

    /** The minimum significant trend percentage: 65%.
	@return minimum significant trend */
    public double getMinimumSignificant() {
	return 0.65;
    }

    /** This algorithm's name ("Trend", hopefully localized).
	@return the name of this cross */
    public String getName() {
	return msg.getString("trend");
    }

    /** A format string for trends.
	@return a format string for trends */
    public String getFormat() {
	return System.getProperty("corina.cross.trend.format", "00.0%");
    }

    /** Compute a single trend, i.e., the trend between the two
       samples for a given possible position.
       @param offset_fixed index into the fixed sample to start
       @param offset_moving index into the moving sample to start
       @return trend score for these offsets */
    public double compute(int offset_fixed, int offset_moving) {
	int i=offset_fixed, j=offset_moving;
	double agree=0;
	int total=0;

	int fi0, fi1, mi0, mi1;
	fi1 = ((Integer)fixed.data.get(i)).intValue();
	mi1 = ((Integer)moving.data.get(j)).intValue();

	while (i<fixed.data.size()-1 && j<moving.data.size()-1) {

	    fi0 = fi1;
	    fi1 = ((Integer)fixed.data.get(i+1)).intValue();
	    mi0 = mi1;
	    mi1 = ((Integer)moving.data.get(j+1)).intValue();

	    if ((fi0 < fi1 && mi0 < mi1) || (fi0 > fi1 && mi0 > mi1) ||
		                            (fi0 == fi1 && mi0 == mi1))
		agree += 1;
	    else if (fi0 == fi1 || mi0 == mi1) // note: "||", not "&&" (!)
		agree += 0.5;

	    total++;
	    i++;  j++;
	}

	return agree / (double) total;
    }

    // histo was: 0.00, 0.05, 1.00
}
