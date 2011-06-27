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
package edu.cornell.dendro.corina_indexing;

import java.util.Arrays;

/**
   A floating (previously misnamed "floating-point") index; each
   point on the index is the mean of some number of data points (the
   window) around that data point.

   <p>The "window" attribute is the number of years around this point
   to average.  The default is 11, meaning each value x[i] will be
   replaced by the mean of x[i-5], x[i-4], ..., x[i-1], x[i], x[i+1],
   ..., x[i+4], x[i+5].</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: Floating.java 888 2007-11-10 07:22:51Z lucasm $
*/
public class Floating extends IndexFunction {

	// the number of points to average to determine the indexed value;
	// set in the constructor.
	private int window;

	// default
	private final static int DEFAULT_WINDOW = 11;

	/**
	   Construct a new floating index, using the default window of 11.

	   @param input the sample to index
	 */
	public Floating(Indexable input) {
		this(input, DEFAULT_WINDOW);
	}

	/**
	   Construct a new floating index, using the specified window.

	   @param input the sample to index
	   @param window the number of points in the window to use
	 */
	public Floating(Indexable input, int window) {
		super(input);
		this.window = window;
	}

	@Override
	public String getI18nTag() {
		return "index.floating";
	}
	
	@Override
	public String getI18nTagTrailer() {
		return new Integer(window).toString();
	}

	/** Calculate the index, by computing the average at each
	point. */
	@Override
	public void index() {
		// make array weights={1,1,1,...}
		int weights[] = new int[window];
		Arrays.fill(weights, 1);

		// high-pass filter
		output = HighPass.filter(input.getData(), weights);
	}

	/* this doesn't give exactly the same results as mecki's, but i
	   think it's correct (and have the unit tests to prove it!).  he
	   might be offsetting the weights, either accidentally or to give
	   a more accurate tree-growth estimate.  but all the docs say to
	   do what i'm doing, so until they tell me to stop... */

	@Override
	public int getLegacyID() {
		return 8;
	}

	public String getDatabaseRepresentation() {
		return "FloatingAverage";
	}
}
