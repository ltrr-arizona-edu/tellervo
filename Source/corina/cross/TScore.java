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

     <li>For each possible overlap, compute the T-score:</li>

     <blockquote>
     <i>s</i><sub>1</sub> = &Sigma;
             ( <i>x</i><sub>i</sub><i>y</i><sub>i</sub> -
	       <i>N</i> ( <i>x</i><sub>i</sub> - <i>x</i><sub>avg</sub> )
	                ( <i>y</i><sub>i</sub> - <i>y</i><sub>avg</sub> ) )
     </blockquote>

     <blockquote>
     <i>s</i><sub>2</sub> = &Sigma;
             ( <i>x</i><sub>i</sub><sup>2</sup> -
	       <i>N</i> ( <i>x</i><sub>i</sub> - <i>x</i><sub>avg</sub> )<sup>2</sup> )
     </blockquote>

     <blockquote>
     <i>s</i><sub>2</sub> = &Sigma;
             ( <i>y</i><sub>i</sub><sup>2</sup> -
	       <i>N</i> ( <i>y</i><sub>i</sub> - <i>y</i><sub>avg</sub> )<sup>2</sup> )
     </blockquote>

     <blockquote>
     <i>r</i> = <i>s</i><sub>1</sub> / &radic;( <i>s</i><sub>2</sub> <i>s</i><sub>3</sub> )
     </blockquote>

     <blockquote>
     <i>t</i> = <i>r</i> &radic;( (<i>N</i> - 2) / (1 - <i>r</i><sup>2</sup>) )
     </blockquote>

   </ol>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class TScore extends Cross {

    /** Size of window to use for the smoothing curve done by the
	normalization.
	@see #normalize */
    private final static int WINDOW=5;

    /** A mutable copy of the fixed data, in array form. */
    private double fixedData[];

    /** A mutable copy of the moving data, in array form. */
    private double movingData[];

    /** The mean of the fixed data. */
    private double fixedMean=0.0;

    /** The mean of the moving data. */
    private double movingMean=0.0;

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

    /** The minimum significant T-score, 2.55.
	@return the minimum significant score */
    public double getMinimumSignificant() {
	return 2.55;
    }

    /** Normalize the data.  This creates a new array of doubles from
	the List of Numbers.  The data is smoothed using a 5-point
	window, and then the natural logarithm is taken.
	@param data a List of Numbers holding the data to normalize
	@return an array of doubles containing the normalized data */
    private double[] normalize(List data) {
        // size of the data: used many times below
        int N = data.size();
        double smoothed[] = new double[N];

/*
 for (int i=0; i<N; i++) {
            int a = Math.max(i-WINDOW/2, 0);
            int z = Math.min(i+WINDOW/2, N-1);

            // what if sum stays 0.0?  rare, but it could happen.

            // this isn't very efficient.  sum[i] = sum[i-1] -
            // data[i-6] + data[i+5], with a few special cases --
            // that's 2 flops per iteration instead of 10.

            double sum=0.0;
            for (int j=a; j<=z; j++)
                sum += ((Number) data.get(j)).doubleValue();
            double avg = sum/(z-a+1);
            smoothed[i] = ((Number) data.get(i)).doubleValue() / avg; // avg; was: sum
        }

        // natural logarithm -- log(0) is bad, so log(max(s[i],eps))
        for (int i=0; i<smoothed.length; i++)
            smoothed[i] = Math.log(Math.max(100.0*smoothed[i], Double.MIN_VALUE));
        */

        // TESTING: make sure everything is positive (for GAZ37ABC.TRU)
        for (int i=0; i<N; i++)
            if (((Number) data.get(i)).intValue() < 0)
                data.set(i, new Integer(1));

        // make array weights={1,1,1,...}
        int weights[] = new int[WINDOW];
        Arrays.fill(weights, 1);

        // borrow a high-pass filter
        List tmp = HighPass.filter(data, weights); // eep, high-pass should return an array!  (and maybe take an array, too!)

        // natural logarithm -- log(0) is bad news, so log(max(s[i],eps))
        for (int i=0; i<N; i++) {
            smoothed[i] = ((Number) data.get(i)).doubleValue() / ((Number) tmp.get(i)).doubleValue(); // ratio
            smoothed[i] = Math.log(Math.max(100.0*smoothed[i], Double.MIN_VALUE));
        }

        return smoothed;
    }

    /** Compute the mean of an array of doubles.
	@param array the array of doubles to average
	@return the average (arithmetic mean) of the given array */
    private double mean(double array[]) {
	// (/ (map '+ array) (length array))
	double s = 0.0;
	for (int i=0; i<array.length; i++)
	    s += array[i];
	return s / array.length;
    }

    /** Given offsets into the fixed and moving data, compute a single
	T-score for that position.
	@return the T-score for this possible cross */
    public double compute(int offsetFixed, int offsetMoving) {
	int i=offsetFixed, j=offsetMoving;
	int overlap=0;

	double z1 = 0.0;
	double z2 = 0.0;
	double z3 = 0.0;

	while (i<fixedData.length && j<movingData.length) {
	    double xx = fixedData[i] - fixedMean;
	    double yy = movingData[j] - movingMean;

	    z1 += xx*xx;
	    z2 += yy*yy;
	    z3 += xx*yy;

	    overlap++;
	    i++;  j++;
	}

	if (z3 < 0.0)
	    return 0.0;

	double sigx = Math.sqrt(z1/overlap);
	double sigy = Math.sqrt(z2/overlap);
	double cal1 = z3/(overlap*sigx*sigy);
	double cal2 = Math.sqrt(overlap-2);
	double cal3 = Math.sqrt(1-cal1*cal1);

	return cal1*cal2/cal3;
    }

    /** Preamble: copy all data to (mutable) double arrays, normalize
        the data, and compute means of the series. */
    protected void preamble() {
	// normalize (while copying to mutable arrays)
	fixedData = normalize(fixed.data);
	movingData = normalize(moving.data);

	// compute means (used later by compute())
	fixedMean = mean(fixedData);
	movingMean = mean(movingData);
    }

    // histo was: 0.00, 0.25, 5.00
}
