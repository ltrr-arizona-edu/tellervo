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

package corina.manip;

import corina.Sample;

import java.util.List;

/**
   Compute mean sensitivity.

   <p>According to some <a href="http://www.ltrr.arizona.edu/pub/dpl/A-NEWS.TXT">release notes</a>
	for a version of Arizona's
	<a href="http://www.ltrr.arizona.edu/archive/dpl/DPL.html">Dendrochronology Program Library</a>,
	mean sensitivity is defined as:</p>

	<blockquote>
	ms = 1 / (<i>N</i>-1) &Sigma; ( 2 | <i>y</i><sub>i</sub> - <i>y</i><sub>i-1</sub> | ) /
	                                  ( | <i>y</i><sub>i</sub> | + | <i>y</i><sub>i-1</sub> | )
	</blockquote>

   <p>If a divide-by-zero (or any other invalid operation) ever
   occurs, NaN is returned.  This can occur, for example, if two
   successive years contain zeros.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class MeanSensitivity {

    public static double ms(Sample sample) {
        List y = sample.data;
        int N = y.size();

        // a special case
        if (N == 0)
            return Double.NaN;

        try {
            double meanSens = 0.0;
            double yi, yi1; // y[i], y[i-1]
            for (int i=1; i<N; i++) {
                // (it's slightly more efficient but less readable to
                // dump yi into yi1 each iteration.)

                yi = ((Number) y.get(i)).doubleValue();
                yi1 = ((Number) y.get(i-1)).doubleValue();

                meanSens += Math.abs(yi - yi1) / (Math.abs(yi) + Math.abs(yi1));

                // abs(y[i]) should always be nonnegative -- but may
                // not be if the user applied a bad index, and it's
                // only a minimal expense
            }
            return meanSens * 2 / (N-1);
        } catch (ArithmeticException ae) {
            return Double.NaN; // divide-by-zero, probably
        } catch (ClassCastException cce) {
            return Double.NaN; // user is editing, it's a String, so let it go
        }
    }
}
