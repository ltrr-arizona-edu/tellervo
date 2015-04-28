/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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

package org.tellervo.desktop.cross;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.sample.Sample;

import edu.emory.mathcs.backport.java.util.Collections;


public class TopScores {
	private final static Logger log = LoggerFactory.getLogger(TopScores.class);
	private Cross c;
	private List<HighScore> highScores;

	public TopScores(Cross c) {
		this.c = c;
		compute();
	}

	public List<HighScore> getScores() {
		return highScores;
	}
	
	public void sortScoresMostSigFirst()
	{
		Collections.sort(highScores);
		Collections.reverse(highScores);
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

		final int minimumOverlap = c.getOverlap();
													
						
		int nr = 0;
		int length = c.getRange().getSpan();
		for (int i = 0; i < length; i++) {

			if (fixedRange.overlap(movingRange) >= minimumOverlap) {

				float score = c.getScore(movingRange.getEnd());
				//log.debug(movingRange.getEnd() + ":" + score);

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

	}


}
