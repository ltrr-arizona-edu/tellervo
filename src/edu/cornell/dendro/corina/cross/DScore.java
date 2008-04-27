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

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.I18n;

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

   @see edu.cornell.dendro.corina.cross.TScore
   @see edu.cornell.dendro.corina.cross.Trend

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class DScore extends Cross {
    /** The T-Score to use (or compute). */
    private TScore tscore;

    /** The Trend scores to use (or compute). */
    private Trend trend;

    // don't use me
    protected DScore() { }

    /**
       Construct a D-Score from two uncrossed samples.  This requires
       calculating the T-Score and Trends first, so used this way,
       this is the slowest cross.

       @param s1 fixed sample to use
       @param s2 moving sample to use
    */
    public DScore(Sample s1, Sample s2) {
	// set fixed, moving
	super(s1, s2);

	// create (but don't run) new tscore and trend
	tscore = new TScore(s1, s2);
	trend = new Trend(s1, s2);
    }

    /**
       Construct a D-Score from two (possibly already-run) crosses,
       the T-Score and Trend for a given pair of samples.  If this
       constructor is used and the crosses have previously been run,
       they are not run again (making this the fastest cross).

       @param t TScore to use
       @param tr Trend to use
       @exception IllegalArgumentException if t and tr don't have the
       same fixed/moving samples
    */
    public DScore(TScore t, Trend tr) {
	// copy sample references
	super(t.getFixed(), t.getMoving());

	// make sure they're the same
	if (t.getFixed() != tr.getFixed() || t.getMoving() != tr.getMoving())
	    throw new IllegalArgumentException("samples aren't the same!");

	// copy existing tscore and trend
	this.tscore = t;
	this.trend = tr;
    }

    @Override
	public String getName() {
	return I18n.getText("dscore");
    }

    @Override
	public String getFormat() {
	    return App.prefs.getPref("corina.cross.dscore.format", "0.00");
    }

    @Override
	public boolean isSignificant(float score, int overlap) {
	// return 100.0; // said by PIK on 8-may-2002 at 10:18am
        return score > 40; // said by PIK on 9-may-2002 at 11:37am
    }

    @Override
	public float getMinimumSignificant() {
        return 40;
    }

    /**
       Run any un-run crosses.

       @see edu.cornell.dendro.corina.cross.TScore
       @see edu.cornell.dendro.corina.cross.Trend
    */
    @Override
	protected void preamble() {
	// run any un-run crosses --
	// BUG: uh-oh, what if they're running now?  need runningNow flag?
	if (!tscore.isFinished())
	    tscore.run();
	if (!trend.isFinished())
	    trend.run();
    }

    @Override
	public int getOverlap() {
    	return App.prefs.getIntPref("corina.cross.d-overlap", 100);
    }
    
    /*
      !!! BUG !!!

      d-score.preamble() calls t-score.run() and trend.run().  it needs this because even
      d-score.single() needs to know t-score.data and trend.data, and it isn't smart
      enough to just call t-score.single() and trend.single() for that.

      now, this wouldn't be a problem, except that run() checks to make sure the overlap
      is enough.  it won't bother to run crossdates if the overlap isn't at least 15
      (or whatever it is these days), so in those cases it just sets data to a zero-length
      array (i thought it did -- apparently it isn't -- ???)

      so then along comes d-score.single() to look at t-score.data, and it looks for an
      index into t-score.data that doesn't exist, because it never had to run that t-score.

      so what should i do?  well, besides the "it's all messed up" hideousness, a good short-term
      solution would be to:

      (1) have d-score.single() simply call t-score.single() and trend.single().  saves quite
      a bit of computation, i think.

      (2) (was there something else i was thinking of?)

      (3) profit!
     */

    @Override
	public float single() {
	// what happens if one of those single()s fails?  can it?

        // return the score
        return dscore(tscore.single(), trend.single());
    }

    // don't want to call this every compute()
    private int overlap = 1; // getMinimumOverlap();

    /**
       Compute a single D-score, i.e., the D-score between the two
       samples for a given possible position.  Because it's a
       composite of two other algorithms that have already been run,
       the first step is to compute the index into the T-score and
       trend vectors.

       @param offset_fixed index into the fixed sample to start
       @param offset_moving index into the moving sample to start
       @return D-score for these offsets
    */
    @Override
	public float compute(int offset_fixed, int offset_moving) {
        // figure out what index (into the cross data) we're talking about
        int index;
        if (offset_fixed == 0) { // phase 1
            index = (getMoving().getData().size() - overlap) - (offset_moving);
        } else { // phase 2
            index = offset_fixed + (getMoving().getData().size() - overlap);
        }

        // get the t, tr, and compute d
	float t, tr, d;
/***/   t = tscore.getScoreOLD(index);
        tr = trend.getScoreOLD(index);
        d = dscore(t, tr);

	/*
	  at ***, arrayindexoutofboundsexception, because overlap <
	  min_overlap (!)
	*/

        // return it
        return d;
    }

    // the guts of the d-score
    private static float dscore(float tscore, float trend) {
        return (trend > 0.50f ? 100 * (trend - 0.50f) * tscore : 0.0f);
    }
}
