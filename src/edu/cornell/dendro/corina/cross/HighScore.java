// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt
// Created on Apr 1, 2005

package edu.cornell.dendro.corina.cross;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;


public class HighScore {
  // a significant score. there's a brilliant refactoring to be
  // done here -- i think it should be usable for tables and grids,
  // as well -- that i'm just not seeing yet. FIXME.

  // currently only used in sigstable -- should be used in table
  // and grid. (no, it shouldn't, that would be silly. no
  // silliness!)

  // unless you want to make it extend Single...

  // MAKE THESE NOT PUBLIC!
  public int number; // 0, 1, 2, ... -- ugly, fixme?
  public Range fixedRange;
  public Range movingRange;
  public float score;
  public int span;
  public float confidence;
  public int signifigant;

  public HighScore(Cross cross, int index, int nr) {
    Year firstCross = cross.getRange().getStart();
    Year thisCross = firstCross.add(index);

    int movedBy = thisCross.diff(cross.getMoving().range.getEnd());

    fixedRange = cross.getFixed().range.redateBy(-movedBy);
    movingRange = cross.getMoving().range.redateEndTo(thisCross);

    // WAS: score = cross.getScoreOLD(index);
    //System.out.println("highscore: old: " + cross.getScoreOLD(index) + " new: " + cross.getScore(thisCross));
    // XXX: evil one-off error lurking around here
    score = cross.getScore(thisCross);
    signifigant = cross.getScoreSignifigance(thisCross);

    span = cross.getFixed().range.overlap(movingRange);

    confidence = Bayesian.getSignificance(cross, score);
    // FIXME: this throws exceptions(?)

    number = nr;
       
  }

  /*
   * -- something like this later? public String toString() { return new DecimalFormat(getFormat()).format(score); }
   */
  public String toString() {
    return "[HighScore: number=" + number + ", fixedRange=" + fixedRange + ", movingRange="
           + movingRange + ", score=" + score + ", span=" + span + ", confidence=" + confidence;
  }
}