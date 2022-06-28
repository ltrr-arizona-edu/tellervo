/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
// Copyright (c) 2005 Aaron Hamid.  All rights reserved.
// See license in COPYING.txt distributed with this file and available online at http://www.gnu.org/licenses/gpl.txt
// Created on Apr 1, 2005

package org.tellervo.desktop.cross;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;

public class HighScore implements Comparable<HighScore> {
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

		int movedBy = thisCross.diff(cross.getMoving().getRange().getEnd());

		fixedRange = cross.getFixed().getRange().redateBy(-movedBy);
		movingRange = cross.getMoving().getRange().redateEndTo(thisCross);

		// WAS: score = cross.getScoreOLD(index);
		// System.out.println("highscore: old: " + cross.getScoreOLD(index) +
		// " new: " + cross.getScore(thisCross));
		// XXX: evil one-off error lurking around here
		score = cross.getScore(thisCross);
		signifigant = cross.getScoreSignifigance(thisCross);

		span = cross.getFixed().getRange().overlap(movingRange);

		confidence = Bayesian.getSignificance(cross, score);
		// FIXME: this throws exceptions(?)

		number = nr;

	}

	/*
	 * -- something like this later? public String toString() { return new
	 * DecimalFormat(getFormat()).format(score); }
	 */
	@Override
	public String toString() {
		return "[HighScore: number=" + number + ", fixedRange=" + fixedRange + ", movingRange=" + movingRange
				+ ", score=" + score + ", span=" + span + ", confidence=" + confidence;
	}

	@Override
	public int compareTo(HighScore arg0) {
		Double o1 = (double) this.score;
		Double o2 = (double) arg0.score;
		
		return o1.compareTo(o2);
	}
}
