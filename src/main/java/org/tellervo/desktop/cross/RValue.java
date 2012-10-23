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

import java.util.Arrays;
import java.util.List;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.indexing.HighPass;


/**
 An R-value.

 <p>Originally, the r-value was never explicitly reported: it was
 merely an intermediate value used in computing the T-score.
 Eventually, though, the statisticians insisted on seeing the
 r-values for themselves, so it was extracted.  (The T-score is now
 phrased in terms of this r-value.)</p>

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id$ */
public class RValue extends Cross {

	/** Size of window to use for the smoothing curve done by the
	 normalization.
	 @see #normalize */
	private final static int WINDOW = 5;

	/** A mutable copy of the fixed data, in array form. */
	private float fixedData[];

	/** A mutable copy of the moving data, in array form. */
	private float movingData[];

	/** The mean of the fixed data. */
	private float fixedMean = 0;

	/** The mean of the moving data. */
	private float movingMean = 0;

	// don't use me
	protected RValue() {
	}

	/** Construct a new R-value from two samples.
	 @param s1 the fixed sample
	 @param s2 the moving sample */
	public RValue(Sample s1, Sample s2) {
		super(s1, s2);
	}

	/** Return a prettier name for this cross: "R-Value".
	 @return the name of this cross, "R-Value" */
	public String getName() {
		return I18n.getText("statistics.rvalue");
	}

	/** A format string for R-values.
	 @return a format string for R-values */
	public String getFormat() {
		return App.prefs.getPref(PrefKey.STATS_FORMAT_RVALUE, "0.00");
	}
	
	public static String getNameStatic() {
		return new RValue().getName();
	}
	
	public static String getFormatStatic() {
		return new RValue().getFormat();
	}


	@Override
	public boolean isSignificant(float score, int overlap) {
		if (overlap == 0) // it happens...
			return false;

		// WRITEME: what's a sig r-value?
		return (score > 0.25f);
	}

	// FIXME: shouldn't need both of these!
	@Override
	public float getMinimumSignificant() {
		return 0.25f; // FIXME: what's a sig r-value?
	}

	/** Normalize the data.  This creates a new array of floats from
	 the List of Numbers.  The data is smoothed using a 5-point
	 window, and then the natural logarithm is taken.
	 @param data a List of Numbers holding the data to normalize
	 @return an array of floats containing the normalized data */
	private float[] normalize(List<Number> data) {
		// size of the data: used many times below
		int N = data.size();
		float smoothed[] = new float[N];

		// make sure everything is positive (for GAZ37ABC.TRU)
		// also, make sure things aren't zero - divide by zero errors!
		Integer one = new Integer(1); // (singleton)
		for (int i = 0; i < N; i++)
			if (data.get(i).intValue() < 1)
				data.set(i, one);

		// make array weights={1,1,1,...}
		int weights[] = new int[WINDOW];
		Arrays.fill(weights, 1);
		// FIXME: if WINDOW is fixed, this should be static

		// temporary test of alternate weighting system
		/*
		weights[0] = 1;
		weights[1] = 2;
		weights[2] = 4;
		weights[3] = 2;
		weights[4] = 1;
		*/
		
		// borrow a high-pass filter
		List<Double> tmp = HighPass.filter(data, weights);
		// high-pass should return an array!  (and maybe take an array, too!)

		// natural logarithm -- log(0) is bad news, so log(max(s[i],eps))
		for (int i = 0; i < N; i++) {
			float value = data.get(i).floatValue();
			float filter = tmp.get(i).floatValue();
			float ratio = value / filter;

			smoothed[i] = (float) Math.log(Math.max(100 * ratio, EPS));
		}

		return smoothed;
	}

	// a really small value
	private final static float EPS = Float.MIN_VALUE;

	/** Compute the mean of an array of floats.
	 @param array the array of floats to average
	 @return the average (arithmetic mean) of the given array */
	private float mean(float array[]) {
		// (/ (reduce '+ array) (length array))
		float s = 0.0f;
		for (int i = 0; i < array.length; i++)
			s += array[i];
		return s / array.length;

		// what if array.length==0? -- only happens if one sample has no data (possible?)
	}

	/** Given offsets into the fixed and moving data, compute a single
	 R-value for that position.
	 @return the R-value for this possible cross */
	@Override
	public float compute(int offsetFixed, int offsetMoving) {
		int i = offsetFixed, j = offsetMoving;
		int overlap = 0;

		float z1 = 0.0f;
		float z2 = 0.0f;
		float z3 = 0.0f;

		float xx, yy;

		while (i < fixedData.length && j < movingData.length) {
			xx = fixedData[i] - fixedMean;
			yy = movingData[j] - movingMean;

			// NOTE: looking at this today (22.8.2002), i'm not sure
			// this shouldn't be the mean of the overlapping sections
			// only (instead of the mean of the entire samples), but
			// this appears to be what baillie did, i'm pretty sure
			// it's what mecki did, and it gives reasonable results
			// (changing which mean it uses probably wouldn't have
			// much effect), so i'm going to leave it for now.

			z1 += xx * xx;
			z2 += yy * yy;
			z3 += xx * yy;

			overlap++;
			i++;
			j++;
		}

		// in B&P's t-score algorithm, they aborted here with t=0 if z3<0.
		// i'm just an r-value now, so returning negative numbers is fine.
		// if z3<0, then r<0, so the t-score can abort with t=0 if r<0.
		// it's ever-so-slightly less efficient, but equally correct.

		// DOCUMENTME: why i'm computing r so strangely (it's the accuracy)
		float sigx = (float) Math.sqrt(z1 / overlap);
		float sigy = (float) Math.sqrt(z2 / overlap);
		float r = z3 / (overlap * sigx * sigy);

		// if overlap=1 or some other silliness, make it zero -- BUT WHY?
		if (Float.isNaN(r))
			r = 0;

		return r;
	}

	/** Preamble: copy all data to (mutable) float arrays, normalize
	 the data, and compute means of the series. */
	// FIXME: preamble() in Cross is dumb; make it just a lazy-evaluation in RValue's compute()
	@Override
	protected void preamble() {
		// normalize (while copying to mutable arrays)
		fixedData = normalize(getFixed().getRingWidthData());
		movingData = normalize(getMoving().getRingWidthData());

		// compute means (used later by compute())
		fixedMean = mean(fixedData);
		movingMean = mean(movingData);
	}
}
