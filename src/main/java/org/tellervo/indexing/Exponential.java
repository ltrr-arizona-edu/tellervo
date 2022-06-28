/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
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
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.indexing;

import java.util.ArrayList;
import java.util.List;

/**
   An exponential curve fit.

   <p>Since it's fitting to the curve

   <blockquote><i>y = a<sub>1</sub> + a<sub>2</sub>e<sup>-px</sup></i></blockquote>

   by solving for <i>[a<sub>1</sub> a<sub>2</sub>]</i>, we compute
   <i>p</i> by 2 passes of a linear search, based on where it will
   probably be.  This isn't as slow as it sounds, because each
   iteration only involves solving a 2x2 matrix equation, which
   <code>Solver.solve2x2()</code> does very quickly.</p>

   <p>The first pass runs <i>p</i> from 0.01 to 0.41 in steps of 0.01;
   the second pass runs for 0.01 on either side of the best value
   found in steps of 0.001.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: Exponential.java 888 2007-11-10 07:22:51Z lucasm $
*/
public class Exponential extends IndexFunction implements SolverFunction {

	// this class took 11 minutes to write (not counting
	// documentation).  porting mecki's exponential-fit (which i can't
	// even distribute because it's under NR's license) took hours, if
	// not days.  plus, this one is simpler.

	// multiplier in exponent
	private double p;

	/**
	   Compute the basis vector, which is
	   <blockquote><i>[ 1 e<sup>-px</sup> ]</i></blockquote>.
	 */
	public double[] f(double x) {
		return new double[] { 1., Math.exp(-p * x) };
	}

	/**
	   Create an exponential fit from a given indexable.

	   @param s the Indexable to index
	 */
	public Exponential(Indexable s) {
		super(s);
	}

	@Override
	public String getI18nTag() {
		return "index.exponential";
	}
	
	@Override
	public String getI18nTagTrailer() {
		return new Double(p).toString();
	}
	
	// if forReal==false, compute chi2, only.
	// if forReal==true, compute chi2 and put results in data
	private double compute(boolean forReal) {
		final List<? extends Number> indata = input.getRingWidthData();
		List<Double> output = null;
		// init x, y
		
		int n = indata.size();
		double x[] = new double[n]; // don't worry, this is only called once
		double y[] = new double[n];
		for (int i = 0; i < n; i++) {
			x[i] = i;
			y[i] = ((Number)indata.get(i)).doubleValue();
		}

		// compute coeffs
		double c[] = null;
		try {
			c = Solver.leastSquares(this, x, y);
		} catch (SingularMatrixException sme) {
			// how to deal with errors?  return a really big chi2!
			return Double.MAX_VALUE;
		}

		// compute curve, chi2.  (this is a special chi^2-computer: it
		// doesn't require a complete list, or O(n) memory.  otherwise
		// i'd re-use Index's implementation.)
		double chi2 = 0.;
		if (forReal)
			output = new ArrayList<Double>(n);
		for (int i = 0; i < n; i++) {
			double f[] = f(x[i]);
			double yp = 0.;
			for (int j = 0; j < 2; j++)
				// degree+1
				yp += c[j] * f[j];
			chi2 += (y[i] - yp) * (y[i] - yp);
			if (forReal)
				output.add(new Double(yp));
		}

		// (if your compiler does loop unrolling and CSE, that last
		// part will be beautiful.  if not...)

		if(forReal)
			this.output = output;
		
		// return chi2
		return chi2;
	}

	// do a linear search from BIG_START to BIG_STOP, in steps of BIG_STEP
	private static final double BIG_START = 0.01;
	private static final double BIG_STOP = 0.41;
	private static final double BIG_STEP = 0.01;

	// then do a linear search around the best value, in steps of BIG_STEP/10

	// the best p-value found so far
	private double bestExp;

	// the chi2 of the best p-value
	private double bestChi2 = Double.MAX_VALUE;

	// search from |start| to |stop| every |incr|, looking for the
	// lowest chi^2.  store results in bestChi2, bestExp
	private void search(double start, double stop, double incr) {
		for (p = start; p < stop; p += incr) {
			double chi2 = compute(false);
			if (chi2 < bestChi2) {
				bestChi2 = chi2;
				bestExp = p;
			}
		}
	}

	/**
	   Run the index; do a search in two passes to find a good
	   &Chi;<sup>2</sup>.
	 */
	@Override
	public void index() {
		// big steps
		search(BIG_START, BIG_STOP, BIG_STEP);

		// refine that best value
		search(bestExp - BIG_STEP, bestExp + BIG_STEP, BIG_STEP / 10); // mecki: .003

		// for real, now
		p = bestExp;
		compute(true);
	}

	@Override
	public int getLegacyID() {
		return 7;
	}
	
	public String getDatabaseRepresentation() {
		return "NegativeExponential";
	}
}
