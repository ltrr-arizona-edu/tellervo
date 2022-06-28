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

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.I18n;

/**
 <p>A (Student's) T-Score crossdate.</p>

 <p>This algorithm is based on Mecki Pohl's algorithm, and gives
 very similar results (reason for deviations unknown, possibly
 rounding error).  I'm not completely certain this implementation is
 correct, but it gives good results.</p>

 <p>There are apparently many (dozens of?) T-score algorithms in
 existance.  This one is taken from Baillie and Pilcher's "A Simple
 Crossdating Program", pp. 7-14, Tree-Ring Bulletin, Vol. 33, 1973.
 (That version was "written in FORTRAN IV and uses a card reader and
 line printer".)</p>

 <p>The procedure used is as follows:</p>

 <ol>

 <li>Normalize the data (make it "bivariate-normal"):</li>

 <ul>

 <li>convert each value to the percentage of the mean of the 5
 values it is the center of; then</li>

 <li>take the natural logarithm of each value</li>

 </ul>

 <li>For each possible overlap, compute the r (correlation coefficient):</li>

 <blockquote>
 <i>s</i><sub>1</sub> = <big>&Sigma;</big>
 ( <i>x</i><sub>i</sub><i>y</i><sub>i</sub> -
 <i>N</i> ( <i>x</i><sub>i</sub> - <i>x</i><sub>avg</sub> )
 ( <i>y</i><sub>i</sub> - <i>y</i><sub>avg</sub> ) )
 </blockquote>

 <blockquote>
 <i>s</i><sub>2</sub> = <big>&Sigma;</big>
 ( <i>x</i><sub>i</sub><sup>2</sup> -
 <i>N</i> ( <i>x</i><sub>i</sub> - <i>x</i><sub>avg</sub> )<sup>2</sup> )
 </blockquote>

 <blockquote>
 <i>s</i><sub>3</sub> = <big>&Sigma;</big>
 ( <i>y</i><sub>i</sub><sup>2</sup> -
 <i>N</i> ( <i>y</i><sub>i</sub> - <i>y</i><sub>avg</sub> )<sup>2</sup> )
 </blockquote>

 <blockquote>
 <i>r</i> = <i>s</i><sub>1</sub> / &radic;( <i>s</i><sub>2</sub> <i>s</i><sub>3</sub> )
 </blockquote>

 <li>Compute the t-score:</li>

 <blockquote>
 <i>t</i> = <i>r</i> &radic;( (<i>N</i> - 2) / (1 - <i>r</i><sup>2</sup>) )
 </blockquote>
 
 </ol>

 <p>The work of computing the r-value is independent, and it turns
 out users want that statistic, too, so that has been extracted into
 the class RValue, which TScore now extends.</p>

 <p>This class is dedicated to poor Mr. Potter out in Van Nuys.</p>

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id$ */
public class TScore extends RValue {

	// don't use me
	protected TScore() {
	}

	/** Construct a new T-score from two samples.
	 @param s1 the fixed sample
	 @param s2 the moving sample */
	public TScore(Sample s1, Sample s2) {
		super(s1, s2);
	}

	/** Return a prettier name for this cross: "T-Score".
	 @return the name of this cross, "T-Score" */
	public String getName() {
		return I18n.getText("statistics.tscore");
	}

	/** A format string for T-scores.
	 @return a format string for T-scores */
	public String getFormat() {
		return App.prefs.getPref(PrefKey.STATS_FORMAT_TSCORE, "0.00");
	}
	
	public static String getNameStatic() {
		return new TScore().getName();
	}
	
	public static String getFormatStatic() {
		return new TScore().getFormat();
	}


	// for 99.5% confidence, from PIK's table (source?)
	// "statistics for archaeologists" (DRENNAN) is off-by-a-column; is he wrong?
	// though i can't find his source, "contemporary statistical methods" (KOOPMANS) to check.
	// update: carol says it's probably one-sided versus two-sided (it's a symmetric
	// distribution).  that seems fair.
	private static float table[] = { 63.657f, 9.925f, 5.841f, 4.604f, 4.032f,
			3.707f, 3.499f, 3.355f, 3.250f, 3.169f, 3.106f, 3.055f, 3.012f,
			2.977f, 2.947f, 2.921f, 2.898f, 2.878f, 2.861f, 2.845f, 2.831f,
			2.819f, 2.807f, 2.797f, 2.787f, 2.779f, 2.771f, 2.763f, 2.756f,
			2.750f, };

	@Override
	public boolean isSignificant(float score, int overlap) {
		if (overlap == 0) // it happens...
			return false;

		// 2.704 is for overlap=40; 60 is 2.660, inf=2.576
		// WRITEME: so take care of those cases!
		float threshold = ((overlap <= 30) ? table[overlap - 1] : 2.576f);

		return (score >= threshold);
	}

	// OBSOLETE! -- but still used by CrossPrinter, CrossFrame
	@Override
	public float getMinimumSignificant() {
		return 2.55f;
	}

	/** A scorebundle - saves us from tons of new() */
	private final TScoreBundle scores = new TScoreBundle();
	
	/**
	 Given offsets into the fixed and moving data, compute a single
	 T-score for that position.

	 @return the T-score for this possible cross
	 */
	@Override
	public float compute(int offsetFixed, int offsetMoving) {
		return compute(offsetFixed, offsetMoving, scores).tscore;
	}

	/**
	 * Allows an easy way to get tscore and rscore
	 * 
	 * @param offsetFixed
	 * @param offsetMoving
	 * @param scores a TScoreBundle to populate
	 * @return the populated TScoreBundle
	 */
	public TScoreBundle compute(int offsetFixed, int offsetMoving, TScoreBundle scores) {
		int overlap = Math.min(getFixed().getRingWidthData().size() - offsetFixed,
				getMoving().getRingWidthData().size() - offsetMoving);

		// already know how to compute r
		scores.rval = super.compute(offsetFixed, offsetMoving);

		// if r is negative, t is zero.
		// (baillie & pilcher caught this earlier, at r's z3, but this
		// way is more convenient for me, and gives the same result.)
		if (scores.rval < 0) {
			scores.tscore = 0;
			return scores;
		}

		// t = r * sqrt(n-2)/sqrt(1-r^2)
		float num = (float) Math.sqrt(overlap - 2);
		float den = (float) Math.sqrt(1 - scores.rval * scores.rval);
		scores.tscore = scores.rval * num / den;

		// if overlap=1 or some other silliness, make it zero.
		if (Float.isNaN(scores.tscore))
			scores.tscore = 0;

		return scores;
	}
	
	/**
	 * A small structure holding both values calculated by this class
	 * @author Lucas Madar
	 *
	 */
	public static class TScoreBundle {
		public float rval;
		public float tscore;
	}
}
