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
import corina.prefs.Prefs;

import java.util.List;
import java.util.ArrayList;

/**
   Run a set of Indexes on a single Sample.  This runs a set of
   frequently-used indexes:

   <ul>
       <li>Horizontal (polynomial degree 0)
       <li>Linear (polynomial degree 1)
       <li>Polynomial degree 2
       <li>Polynomial degree 3
       <li>Polynomial degree 4
       <li>Polynomial degree 5
       <li>Polynomial degree 6
       <li>Negative exponential
       <li>Floating average (11-year window)
       <li>High-pass filter (configurable, default 1-2-4-2-1)
       <li>Cubic spline (configurable s-value, default 1e-16)
   </ul>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class IndexSet implements Runnable {
    /** The indexes. */
    public List indexes = new ArrayList(12);

    /** Construct a new IndexSet.
    @param s the Sample to index */
    public IndexSet(Sample s) {
        // horizontal
        indexes.add(new Horizontal(s));

        // polynomial fits
        int polydegs[] = Prefs.extractInts(System.getProperty("corina.index.polydegs", "1 2 3 4 5 6"));
        for (int i=0; i<polydegs.length; i++)
            indexes.add(new Polynomial(s, polydegs[i]));

        // exponential, floating, highpass, and cubicspline
        indexes.add(new Exponential(s));
        indexes.add(new Floating(s));
        indexes.add(new HighPass(s));
        indexes.add(new CubicSpline(s));
    }

    /** Construct a new IndexSet, using a proxy dataset.
        @param s the Sample to index */
    public IndexSet(Sample s, Sample proxy) {
        this(s);
        for (int i=0; i<indexes.size(); i++)
            ((Index) indexes.get(i)).setProxy(proxy);
    }

    /** Run all the indexes. */
    public void run() {
        // i put this in run(), so it could be threaded, but is it worth it?
        // naw, i'm TOO fast: for 100yr sample x 10 indexes, 900mhz athlon: 30-40ms
        for (int i=0; i<indexes.size(); i++)
            ((Index) indexes.get(i)).run();
    }
}
