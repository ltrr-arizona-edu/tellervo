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
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.cross;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.logging.CorinaLog;
import edu.cornell.dendro.corina.sample.Sample;

/**
 * <h2>Left to do</h2>
 * <ul>
 * <li>need interface method (like getHighScores())
 * <li>instead of recomputing each time, add all to list, and return only overlap>min when asked to (filter)
 * <li>document me -- gpl header, javadoc; views should watch for when corina.cross.overlap changes (prefsListener), and ask me to recompute
 * <li>use this in CrossdatePrinter, SignificantScoresView; get rid of highScores in Cross; have Crossdate.run() create a TopScores object (including compute())
 * <li>CrossdateWindow sorts the highScores list, but shouldn't (why does it?)
 * <li>do sorting here -- need a sortBy(??) method here; also, getSort(??)?
 * </ul>
 */
public class TopScores {
	private static final Log log = new CorinaLog(TopScores.class);
	private Cross c;
	private List<HighScore> highScores;

	public TopScores(Cross c) {
		this.c = c;
		compute();
	}

	public List<HighScore> getScores() {
		return highScores;
	}

	private void compute() {
		highScores = new ArrayList<HighScore>();

		Sample fixed = c.getFixed();
		Sample moving = c.getMoving();
		Range fixedRange = fixed.getRange();
		Range movingRange = moving.getRange()
				.redateEndTo(fixedRange.getStart()); //.add(getMinimumOverlap()-
														// 1));
		// System.out.println("fixed: "+ fixed);
		// System.out.println("moving: "+ moving);
		// System.out.println("fixedRange: "+ fixedRange);
		// System.out.println("movingRange: "+ movingRange);

		// FIXME: default=15 shouldn't be here; it's used elsewhere, as well.
		final int minimumOverlap = c.getOverlap(); // App.prefs.getIntPref(
													// "corina.cross.overlap",
													// 15);
		int nr = 0;
		int length = c.getRange().span();
		for (int i = 0; i < length; i++) {
			// overlap not long enough? skip it.
			// (PERF: inefficient!)
			if (fixedRange.overlap(movingRange) >= minimumOverlap) {

				float score = c.getScore(movingRange.getEnd());
				log.debug(movingRange.getEnd() + ":" + score);

				if (c.isSignificant(score, fixedRange.overlap(movingRange))) {
					try {
						nr++;
						highScores.add(new HighScore(c, i, nr));
					} catch (Exception e) {
						log.error("trouble with bayes! -- " + e);
						// FIXME: bayes? huh?
						e.printStackTrace();
					}
				}
			}

			movingRange = movingRange.redateBy(+1); // slide it by 1
		}
		// convert to array now? all i do with it is .size(), .get(),
		// and sort(comparable)

		// how do i sort these? (that'll have an impact)

		// right now, i'm thinking:
		// - void cross.computeHighScores() -- computes all
		// - Iterator cross.getHighScores() -- returns only span>minimumOverlap

		// testing
		// oldComputeHighScores();
	}

	/**
	 * "Old" Cross computeHighScores implementation that accesses
	 * data array directly
	 * @deprecated for testing only
	 */
	/*private List oldComputeHighScores() {
    ArrayList highScores = new ArrayList();

    Sample fixed = c.getFixed();
    Sample moving = c.getMoving();
    Range fixedRange = fixed.range;
    Range movingRange = moving.range.redateEndTo(fixedRange.getStart()); // .add(getMinimumOverlap()-1));
    //System.out.println("old fixed: "+ fixed);
    //System.out.println("old moving: "+ moving);
    //System.out.println("old fixedRange: "+ fixedRange);
    //System.out.println("old movingRange: "+ movingRange);

    int nr = 0;
    int length = c.data.length;
    for (int i = 0; i < length; i++) {
      movingRange = movingRange.redateBy(+1); // slide it by 1

      float score = c.data[i];
      System.out.println(movingRange.getEnd() + ":" + score);
      if (c.isSignificant(score, fixedRange.overlap(movingRange)))
        try {
          nr++;
          highScores.add(new HighScore(c, i + 1, nr));
        } catch (Exception e) {
          System.out.println("trouble with bayes! -- " + e);
          // FIXME: bayes? huh?
          e.printStackTrace();
        }
     }
     // convert to array now? all i do with it is .size(), .get(),
     // and sort(comparable)

    // how do i sort these? (that'll have an impact)

    // right now, i'm thinking:
    // - void cross.computeHighScores() -- computes all
    // - Iterator cross.getHighScores() -- returns only span>minimumOverlap
    return highScores;
  }*/
}