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

package edu.cornell.dendro.corina_indexing;

import java.util.ArrayList;
import java.util.List;

/**
   A simple high-pass filter.

   <p>The filter is a simple weighted filter; a graphics jock might
   call this "Gaussian smoothing" or a "convolution matrix".</p>

   <p>The weights are taken from the property
   <code>corina.index.lowpass</code>; if it isn't set, the filter "1 2
   4 2 1" is used.  The weights must be integers, separated by spaces.
   If there are an even number of weights, the last one is ignored.
   The divisor is the sum of all the weights.</p>

   <p>Cook makes reference to low-pass filtering as applied to
   "growth-trend estimation" in <i>Methods of Dendrochronology</i>,
   Cook and Kairiukstis, pp. 110-111.  (At
   <a href="http://www.amazon.com/exec/obidos/ASIN/0792305868/o/qid=993490628/sr=2-1/ref=aps_sr_b_1_1/104-5614136-5731905">Amazon.com</a>,
   it costs a small fortune.)</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: HighPass.java 888 2007-11-10 07:22:51Z lucasm $
*/
public class HighPass extends IndexFunction {
	/**
	   Create a new high-pass filter for the given sample.

	   @param s the Sample to index
	 */
	public HighPass(Indexable input) {
		this(input, new int[] { 1, 2, 4, 2, 1});
	}
	
	public HighPass(Indexable input, int[] weights) {
		super(input);
		this.weights = weights;
	}

	private int weights[];

	/** Compute the index. */
	@Override
	public void index() {
		output = filter(input.getData(), weights);
	}

	// a discrete high-pass filter.
	// used by: highpass.run(), floating.run(), tscore.preamble()
	// -- array/list dichotomy problem?  no, seems ok.
	// -- implement special case for w[i]==1?  (floating and t-score use it this way.)
	public static List filter(List input, int[] w) {
		int n = input.size();
		List output = new ArrayList(n);
		int nw = (w.length - 1) / 2;

		// sum the weights -- (extract method?)
		int sum = 0; // -- (apply '+ w)
		for (int i = 0; i < w.length; i++)
			sum += w[i];

		for (int i = 0; i < n; i++) {
			double x = 0, adj = 0;
			int j = -nw;
			do {
				if (i + j >= 0 && i + j < n)
					x += ((Number) input.get(i + j)).doubleValue() * w[nw + j]; // add value, weighted, or ...
				else
					adj += w[nw + j]; // ... discount weights by that amount
				j++;
			} while (j <= nw);

			double value;
			if (sum - adj == 0) { // was: ... || x==0
				value = 0.0;
			} else {
				value = x / (sum - adj);
			}
			output.add(new Double(value));
		}
		return output;
	}
	
	@Override
	public String getI18nTag() {
		return "high_pass";
	}
	
	@Override
	public String getI18nTagTrailer() {
		// this is basically the opposite of StringUtils.extractInts() -- consolidate?
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < weights.length; i++) {
			buf.append(weights[i]);
			if (i < weights.length - 1)
				buf.append('-');
		}

		return buf.toString();
	}

	@Override
	public int getLegacyID() {
		return 9;
	}
	
	public String getDatabaseRepresentation() {
		return "Highpass";
	}
}
