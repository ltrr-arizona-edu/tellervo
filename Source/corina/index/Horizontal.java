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

package corina.index;

import corina.Sample;

/**
   Horizontal-line index type.

   <p>The absolute simplest index possible: everything becomes a
   percentage of the mean.  Of course, there's no reason to ever use
   this index in practice, because crossdating normalizes datasets
   first (T-score) or doesn't care about their magnitudes (trend).</p>

   <p>There's also no real reason for this class to exist, because
   it's merely a special case of the polynomial index, but it's a good
   template for writing other algorithms because it's so simple.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class Horizontal extends Index {

    /** Constructs a new Horizontal index with the given sample.
	@param s sample to index */
    public Horizontal(Sample s) {
	super(s);
    }

    /** Run the index. */
    public void index() {
	// compute mean; make a flyweight for the list
	Double mean = new Double((double) source.computeRadius() /
				 (double) source.data.size());

	// the curve: it's flat...
	int n = source.data.size();
	for (int i=0; i<n; i++)
	    data.add(mean);
    }

    /** Return the name of this index.
	@return the name of this index */
    public String getName() {
	return msg.getString("horizontal");
    }

    public int getID() {
        return 0;
    }
}
