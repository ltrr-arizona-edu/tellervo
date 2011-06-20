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

import java.util.Collections;
import java.util.List;

/**
   Horizontal-line index type.

   <p>The absolute simplest index possible: everything becomes a
   percentage of the mean.  Of course, there's no reason to ever use
   this index in practice, because crossdating normalizes datasets
   first (T-score) or doesn't care about their magnitudes (trend).</p>

   <p>There's also no real reason for this class to exist, because
   it's merely a special case of the polynomial index, but it's a good
   template for writing other algorithms because it's so simple.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: Horizontal.java 888 2007-11-10 07:22:51Z lucasm $
*/
public class Horizontal extends IndexFunction {
	/**
	   Constructs a new Horizontal index with the given sample.

	   @param s sample to index
	 */
	public Horizontal(Indexable input) {
		super(input);
	}

	/** Run the index. */
	@Override
	public void index() {
		// compute mean; make a flyweight for the list
		Double mean = new Double(getDatasetMean(input.getData()));

		// the curve: well, it's flat...
		int n = input.getData().size();
		List<Double> output = Collections.nCopies(n, mean);
		this.output = output;
	}
	
	private double getDatasetMean(List<? extends Number> dataset) {
		int n = dataset.size();
		int sum = 0;
		for (int i = 0; i < n; i++)
			sum += dataset.get(i).intValue();
		return (double) sum / (double) n;
	}
	
	@Override
	public String getI18nTag() {
		return "index.horizontal";
	}

	@Override
	public int getLegacyID() {
		return 0;
	}
	
	public String getDatabaseRepresentation() {
		return "Horizontal";
	}
}
