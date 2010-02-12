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

import java.util.List;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;

/**
   A "trend", or <i>Gleichl&auml;ufigkeitskoeffizient</i>, crossdate.

   <p>Scores are the fraction of matching trends.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Trend extends Cross {

    // don't use me -- for getName() only, i think
    public Trend() { }

    /** Create a new Trend from given samples.
	@param fixed fixed sample to use
	@param moving moving sample to use */
    public Trend(Sample fixed, Sample moving) {
    	super(fixed, moving);
    }

    // number of significant intervals(?) to use, when computing
    // whether a trend is significant; 1 gave too many hits, 2 seems
    // good (should this be user-settable in the prefs?  1sig, 2sig,
    // 3sig)
    private static final int SIGMA = 2;

    // REFACTOR: this is ok here, because it's how these formulas are written,
    // but it's lousy higher-up.  i should have an isSig(int index) or even isSig(Year).
    @Override
	public boolean isSignificant(float score, int overlap) {
        return score >= (50. + SIGMA * 50./Math.sqrt(overlap))/100.;
    }

    // OBSOLETE soon -- i hope?
    // still used by: AllScoresView, CrossdatePrinter
    @Override
	public float getMinimumSignificant() {
        return 0.65f;
    }

	public String getName() {
	return I18n.getText("statistics.trend");
    }

	public String getFormat() {
	     return App.prefs.getPref("corina.cross.trend.format", "0.0%");
    }
	
	public static String getNameStatic() {
		return new Trend().getName();
	}
	
	public static String getFormatStatic() {
		return new Trend().getFormat();
	}


    // same data, but in arrays
    private float fixedData[], movingData[];

    @Override
	protected void preamble() {
	fixedData = makeArray(getFixed().getData());
	movingData = makeArray(getMoving().getData());

	// Float is (probably at least) 26 bytes, compared with 4
	// bytes for a float, so for 1000 floats, that's 26K vs 4K in
	// memory.  double this for Doubles: 52K for a list of
	// Doubles.  that's a savings of over 10x when moving from
	// Doubles to floats.  access time probably doesn't save this
	// much, but if i can keep more crossdates/samples in memory,
	// that's less i/o.
    }

    // make an array of floats from a List of Numbers
    private float[] makeArray(List<Number> list) {
	int n = list.size();
	float array[] = new float[n];
	for (int i=0; i<n; i++)
	    array[i] = ((Number) list.get(i)).floatValue();
	return array;
    }

    // why was trend so slow?  because it was running .get(i) O(n^2) times
    // t-score ran it only O(n) times, and ran 3x as fast.
    // solution:
    // -- never pass a List of Objects to a crossdate, it never pays off.
    // -- shove this list->array code up into cross.java, refactor.

    // why does trend need floating point numbers?  i can't think of a
    // case when ints wouldn't be good enough.  (floating point
    // comparisons are slower than int comparisons, i presume.)
    
    /**
       Compute a single trend, i.e., the trend between the two
       samples for a given possible position.

       @param offset_fixed index into the fixed sample to start
       @param offset_moving index into the moving sample to start
       @return trend score for these offsets
    */
    @Override
	public float compute(int offset_fixed, int offset_moving) {
        int i=offset_fixed, j=offset_moving;
        float agree=0;
        int total=0;

        float fi0, fi1, mi0, mi1;
        fi1 = fixedData[i];
        mi1 = movingData[j];

        while (i<fixedData.length-1 && j<movingData.length-1) {

            fi0 = fi1;
            fi1 = fixedData[i+1];
            mi0 = mi1;
            mi1 = movingData[j+1];

            // this isn't exactly the same as schweingruber.
	    // (would that be easier to vectorize?)
            
            if ((fi0<fi1 && mi0<mi1) || (fi0>fi1 && mi0>mi1) || (fi0==fi1 && mi0==mi1))
                agree += 1;
            else if (fi0==fi1 || mi0==mi1) // note: "||", not "&&"
                agree += 0.5;

            total++;
            i++;  j++;
        }

        // oh, for cryin' out loud, don't do that...
	// (some loser is crossdating a zero-length sample)
        if (total == 0)
            return 0;

        return agree / total;
    }
}
