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

import corina.Year;
import corina.Range;
import corina.Sample;

import java.util.List;
import java.util.ArrayList;

/*
  <h2>Left to do</h2>
  <ul>
    <li>need interface method (like getHighScores())
    <li>instead of recomputing each time, add all to list, and return
        only overlap>min when asked to (filter)
    <li>document me -- gpl header, javadoc; views should watch for when
        corina.cross.overlap changes (prefsListener), and ask me to recompute
    <li>use this in CrossdatePrinter, SignificantScoresView; get rid of
        highScores in Cross; have Crossdate.run() create a TopScores
	object (including compute())
    <li>CrossdateWindow sorts the highScores list, but shouldn't (why does it?)
    <li>do sorting here -- need a sortBy(??) method here; also, getSort(??)?
  </ul>
*/
public class TopScores {

    private Cross c;

    private List highScores;

    public TopScores(Cross c) {
	this.c = c;

	compute();
    }

    private void compute() {
	Sample fixed = c.getFixed();
	Sample moving = c.getMoving();

	highScores = new ArrayList();

        int nr=0;
        Range fixedRange = fixed.range;
        Range movingRange = moving.range.redateEndTo(fixedRange.getStart());

	// FIXME: use Perf.getPerf()!
	// FIXME: default=15 shouldn't be here; it's used elsewhere, as well.
	final int minimumOverlap = Integer.getInteger("corina.cross.overlap",
						      15).intValue();

	int n = c.getRange().span();
        for (int i=0; i<n; i++) {
            movingRange = movingRange.redateBy(+1); // slide it by 1

	    // overlap not long enough?  skip it.
	    // (PERF: inefficient!)
	    if (fixedRange.overlap(movingRange) < minimumOverlap)
		continue;

	    float score = c.getScore(movingRange.getEnd());

            if (c.isSignificant(score, fixedRange.overlap(movingRange)))
		try {
		    highScores.add(new HighScore(c /*this*/, i, ++nr));
		} catch (Exception e) {
		    System.out.println("trouble with bayes! -- " + e);
		    // FIXME: bayes?  huh?
		    e.printStackTrace();
		}
        }
        // convert to array now?  all i do with it is .size(), .get(),
        // and sort(comparable)

	// how do i sort these?  (that'll have an impact)

	// right now, i'm thinking:
	// - void cross.computeHighScores() -- computes all
	// - Iterator cross.getHighScores() -- returns only span>minimumOverlap
    }
 
    public static class HighScore {

	// a significant score.  there's a brilliant refactoring to be
	// done here -- i think it should be usable for tables and grids,
	// as well -- that i'm just not seeing yet.  FIXME.

	// currently only used in sigstable -- should be used in table
	// and grid.  (no, it shouldn't, that would be silly.  no
	// silliness!)

	// unless you want to make it extend Single...

	// MAKE THESE NOT PUBLIC!

	public int number; // 0, 1, 2, ... -- ugly, fixme?
	public Range fixedRange;
	public Range movingRange;
	public float score;
	public int span;
	public float confidence;

	public HighScore(Cross cross, int index, int nr) {
	    Year firstCross = cross.getRange().getStart();
	    Year thisCross = firstCross.add(index);

	    int movedBy = thisCross.diff(cross.getMoving().range.getEnd());

	    fixedRange = cross.getFixed().range.redateBy(-movedBy);
	    movingRange = cross.getMoving().range.redateEndTo(thisCross);

	    // WAS: score = cross.getScoreOLD(index);
	    score = cross.getScore(thisCross);

	    span = cross.getFixed().range.overlap(movingRange);

	    confidence = Bayesian.getSignificance(cross, score);
	    // FIXME: this throws exceptions(?)

	    number = nr;
	}

	/* -- something like this later?
	public String toString() {
	   return new DecimalFormat(getFormat()).format(score);
	}
	*/
    }
}
