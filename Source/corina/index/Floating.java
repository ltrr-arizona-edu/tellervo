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
import corina.ui.I18n;

import java.util.Arrays;

import java.text.MessageFormat;

/**
   A floating (previously misnamed "floating-point") index; each
   point on the index is the mean of some number of data points (the
   window) around that data point.

   <p>The "window" attribute is the number of years around this point
   to average.  The default is 11, meaning each value x[i] will be
   replaced by the mean of x[i-5], x[i-4], ..., x[i-1], x[i], x[i+1],
   ..., x[i+4], x[i+5].</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class Floating extends Index {

    // the number of points to average to determine the indexed value;
    // set in the constructor.
    private int window;

    // default
    private final static int DEFAULT_WINDOW = 11;

    /**
       Construct a new floating index, using the default window of 11.

       @param s the sample to index
    */
    public Floating(Sample s) {
        this(s, DEFAULT_WINDOW);
    }

    /**
       Construct a new floating index, using the specified window.

       @param s the sample to index
       @param window the number of points in the window to use
    */
    public Floating(Sample s, int window) {
	super(s);
	this.window = window;
    }

    /**
       Return the human-readable name of this Index, which is
       "Floating (n-pt)", for an n-point window.

       @return "Floating (n-pt)", for window size n
    */
    public String getName() {
        return MessageFormat.format(I18n.getText("floating"),
				    new Object[] { new Integer(window) } );
    }

    /** Calculate the index, by computing the average at each
	point. */
    public void index() {
        // make array weights={1,1,1,...}
        int weights[] = new int[window];
        Arrays.fill(weights, 1);

        // high-pass filter
        data = HighPass.filter(source.data, weights);
    }

    /* this doesn't give exactly the same results as mecki's, but i
       think it's correct (and have the unit tests to prove it!).  he
       might be offsetting the weights, either accidentally or to give
       a more accurate tree-growth estimate.  but all the docs say to
       do what i'm doing, so until they tell me to stop... */

    public int getID() {
        return 8;
    }
}
