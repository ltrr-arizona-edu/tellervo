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
import corina.index.HighPass;

import java.util.List;
import java.util.Arrays;

/**
   <p>A (Student's) T-Score crossdate.</p>

   <p>This algorithm is based on Mecki Pohl's algorithm, and gives
   very similar results (reason for deviations unknown, possibly
   rounding error).  I'm not completely certain this implementation is
   correct, but it gives usable results.  Corrections welcome.</p>

   <p>There are apparently many (dozens of?) T-score algorithms in
   existance.  This one is taken from Baillie and Pilcher's "A Simple
   Crossdating Program", pp. 7-14, Tree-Ring Bulletin, Vol. 33, 1973.
   That version was "written in FORTRAN IV and uses a card reader and
   line printer".</p>

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

   <p>This class is dedicated to poor Mr. Potter out in Van Nuys.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class TScore extends Cross {

    /** Size of window to use for the smoothing curve done by the
	normalization.
	@see #normalize */
    private final static int WINDOW=5;

    /** A mutable copy of the fixed data, in array form. */
    private float fixedData[];

    /** A mutable copy of the moving data, in array form. */
    private float movingData[];

    /** The mean of the fixed data. */
    private float fixedMean=0.0f;

    /** The mean of the moving data. */
    private float movingMean=0.0f;

    // don't use me
    protected TScore() { }

    /** Construct a new T-score from two samples.
	@param s1 the fixed sample
	@param s2 the moving sample */
    public TScore(Sample s1, Sample s2) {
	super(s1, s2);
    }

    /** Return a prettier name for this cross: "T-Score".
	@return the name of this cross, "T-Score" */
    public String getName() {
	return msg.getString("tscore");
    }

    /** A format string for T-scores.
	@return a format string for T-scores */
    public String getFormat() {
	return System.getProperty("corina.cross.tscore.format", "0.00");
    }

    // for 99.5% confidence, from PIK's table
    private static float table[] = {
        63.657f,
        9.925f,
        5.841f,
        4.604f,
        4.032f,
        3.707f,
        3.499f,
        3.355f,
        3.250f,
        3.169f,
        3.106f,
        3.055f,
        3.012f,
        2.977f,
        2.947f,
        2.921f,
        2.898f,
        2.878f,
        2.861f,
        2.845f,
        2.831f,
        2.819f,
        2.807f,
        2.797f,
        2.787f,
        2.779f,
        2.771f,
        2.763f,
        2.756f,
        2.750f,
    };
    public boolean isSignificant(double score, int overlap) {
	if (overlap == 0) // it happens...
	    return false;

	// 2.704 is for overlap=40; 60 is 2.660, inf=2.576
        float threshold = ((overlap <= 30) ? table[overlap-1] : 2.576f);

        return (score >= threshold);
    }

    public double getMinimumSignificant() {
        return 2.55;
    }
    
    /** Normalize the data.  This creates a new array of floats from
	the List of Numbers.  The data is smoothed using a 5-point
	window, and then the natural logarithm is taken.
	@param data a List of Numbers holding the data to normalize
	@return an array of floats containing the normalized data */
    private float[] normalize(List data) {
        // size of the data: used many times below
        int N = data.size();
        float smoothed[] = new float[N];

        // make sure everything is positive (for GAZ37ABC.TRU)
	Integer one = new Integer(1); // (singleton)
        for (int i=0; i<N; i++)
            if (((Number) data.get(i)).intValue() < 0)
                data.set(i, one);

        // make array weights={1,1,1,...}
        int weights[] = new int[WINDOW];
        Arrays.fill(weights, 1);
	// REFACTOR: if WINDOW is fixed, this should be static

        // borrow a high-pass filter
        List tmp = HighPass.filter(data, weights);
	// eep, high-pass should return an array!  (and maybe take an array, too!)

        // natural logarithm -- log(0) is bad news, so log(max(s[i],eps))
        for (int i=0; i<N; i++) {
            smoothed[i] = ((Number) data.get(i)).floatValue() / ((Number) tmp.get(i)).floatValue(); // ratio
            smoothed[i] = (float) Math.log(Math.max(100.0f*smoothed[i], Float.MIN_VALUE));
        }

        return smoothed;
    }

    /** Compute the mean of an array of floats.
	@param array the array of floats to average
	@return the average (arithmetic mean) of the given array */
    private float mean(float array[]) {
        // (/ (reduce '+ array) (length array))
        float s = 0.0f;
        for (int i=0; i<array.length; i++)
            s += array[i];
        return s / array.length;

	// what if array.length==0? -- only happens if one sample has no data (possible?)
    }

    /** Given offsets into the fixed and moving data, compute a single
	T-score for that position.
	@return the T-score for this possible cross */
    public double compute(int offsetFixed, int offsetMoving) {
        int i=offsetFixed, j=offsetMoving;
        int overlap=0;

	float z1 = 0.0f;
	float z2 = 0.0f;
        float z3 = 0.0f;

        float xx, yy;

        while (i<fixedData.length && j<movingData.length) {
            xx = fixedData[i] - fixedMean;
            yy = movingData[j] - movingMean;

	    // NOTE: looking at this today (22.8.2002), i'm not sure this shouldn't be
	    // the mean of the overlapping sections only (instead of the mean of the
	    // entire samples), but this appears to be what baillie did, i'm pretty
	    // sure it's what mecki did, and it gives reasonable results (changing
	    // which mean it uses probably wouldn't have much effect), so i'm going
	    // to leave it for now.

            z1 += xx*xx;
            z2 += yy*yy;
            z3 += xx*yy;

            overlap++;
            i++;  j++;
        }

	// fail ... i don't completely understand this, but it's Bad (trust me)
        if (z3 < 0.0f)
            return 0.0f;

	// DOCUMENTME: what this computes, and why i'm scratching my right ear with my left hand
        float sigx = (float) Math.sqrt(z1/overlap);
        float sigy = (float) Math.sqrt(z2/overlap);
        float cal1 = z3/(overlap*sigx*sigy);
        float cal2 = (float) Math.sqrt(overlap-2);
        float cal3 = (float) Math.sqrt(1-cal1*cal1);

        float t = cal1*cal2/cal3;

        // if overlap=1 or some other silliness, make it zero
        if (Float.isNaN(t))
            t = 0.0f;

        return t;
    }

    /** Preamble: copy all data to (mutable) float arrays, normalize
        the data, and compute means of the series. */
    protected void preamble() {
	// normalize (while copying to mutable arrays)
	fixedData = normalize(fixed.data);
	movingData = normalize(moving.data);

	// compute means (used later by compute())
	fixedMean = mean(fixedData);
	movingMean = mean(movingData);
    }
}
