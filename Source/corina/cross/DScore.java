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
   Class for computing the hybrid "D-Score" ("Dating Score").  The
   D-score is defined for each year <i>i</i> as:

   <blockquote>
   <table border="0">
   <tr>
       <td rowspan="2"><i>D</i> = {</td>
       <td>(tr - 50%) &sdot; <i>t</i></td>
       <td>tr > 50%</td>
   </tr>
   <tr>
       <td>0.0</td>
       <td>tr &le; 50%</td>
   </tr>
   </table>
   </blockquote>

   @see corina.cross.TScore
   @see corina.cross.Trend

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class DScore extends Cross {

    /** The T-Score to use (or compute). */
    private TScore tscore;

    /** The Trend scores to use (or compute). */
    private Trend trend;

    // don't use me
    protected DScore() { }

    /** Construct a D-Score from two uncrossed samples.  This requires
       calculating the T-Score and Trends first, so used this way,
       this is the slowest cross.
       @param s1 fixed sample to use
       @param s2 moving sample to use */
    public DScore(Sample s1, Sample s2) {
	// set fixed, moving
	super(s1, s2);

	// create (but don't run) new tscore and trend
	tscore = new TScore(s1, s2);
	trend = new Trend(s1, s2);
    }

    /** Construct a D-Score from two (possibly already-run) crosses,
	the T-Score and Trend for a given pair of samples.  If this
	constructor is used and the crosses have previously been run,
	they are not run again (making this the fastest cross).
	@param t TScore to use
	@param tr Trend to use */
    public DScore(TScore t, Trend tr) {
	// copy sample references
	super(t.fixed, t.moving);

	// copy existing tscore and trend
	this.tscore = t;
	this.trend = tr;
    }

    /** Return a nicer string of this cross, "D-Score".
	@return the name of this cross, "D-Score" */
    public String getName() {
	return msg.getString("dscore");
    }

    /** A format string for D-scores.
	@return a format string for D-scores */
    public String getFormat() {
	return System.getProperty("corina.cross.dscore.format", "0.00");
    }

    /** Return the minimum significant D-score.  I don't know where
	(if anywhere) the D-score is mentioned in the literature, so
	I'll assume it is the same as calculating the hypothetical
	D-score for a minimally significant trend and T-score.
	@return the minimum significant D-score */
    public double getMinimumSignificant() {
	return dscore(tscore.getMinimumSignificant(), trend.getMinimumSignificant());
    }

    /** Run any un-run crosses.
	@see corina.cross.TScore
	@see corina.cross.Trend */
    protected void preamble() {
	// run any un-run crosses -- uh-oh, what if they're running now?  need runningNow flag?
	if (!tscore.isFinished())
	    tscore.run();
	if (!trend.isFinished())
	    trend.run();
    }

    /** Compute a single D-score, i.e., the D-score between the two
       samples for a given possible position.  Because it's a
       composite of two other algorithms that have already been run,
       the first step is to compute the index into the T-score and
       trend vectors.
       @param offset_fixed index into the fixed sample to start
       @param offset_moving index into the moving sample to start
       @return D-score for these offsets */
    public double compute(int offset_fixed, int offset_moving) {
	// figure out what index (into the cross data) we're talking about
	int index;
	if (offset_fixed == 0) { // phase 1
	    index = (moving.data.size() - OVERLAP) - (offset_moving);
	} else { // phase 2
	    index = offset_fixed + (moving.data.size() - OVERLAP);
	}

        // get the t, tr, and compute d
        double t, tr, d;
        t = tscore.data[index];
        tr = trend.data[index];
        d = dscore(t, tr);

        // return it
        return d;
    }

    // the guts of the d-score
    private static double dscore(double tscore, double trend) {
        return (trend > 0.50 ? 100 * (trend - 0.50) * tscore : 0.0);
    }

    // histo was: 0.0, 2.5, 50.0
}
