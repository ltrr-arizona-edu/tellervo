/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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
 *     Peter Brewer
 ******************************************************************************/

package org.tellervo.desktop.manip;

import java.util.List;

/**
   Compute mean sensitivity.

   <p>According to some
   <a href="http://www.ltrr.arizona.edu/pub/dpl/A-NEWS.TXT">release notes</a>
   for a version of Arizona's
   <a href="http://www.ltrr.arizona.edu/archive/dpl/DPL.html">Dendrochronology
   Program Library</a>, mean sensitivity is defined as:</p>

   <blockquote>
   1 / (<i>N</i>-1) &Sigma; ( 2 | <i>y</i><sub>i</sub> - <i>y</i><sub>i-1</sub> | ) /
       ( | <i>y</i><sub>i</sub> | + | <i>y</i><sub>i-1</sub> | )
   </blockquote>

   <p>If a divide-by-zero (or any other invalid operation) ever
   occurs, NaN is returned.  (This can occur, for example, if two
   successive years contain zeros.)  If the sample has no data in it,
   NaN is returned.</p>

   <p><a href="http://www.wsl.ch/land/dynamics/dendro/FHS_d.html">Schweingruber</a>
   says the mean sensitivity is

   <blockquote>
   1 / (N-1) &Sigma; ( 2 | (y<sub>i-1</sub>-y<sub>i</sub>) /
                           (y<sub>i-1</sub>+y<sub>i</sub>) |)
   </blockquote>

   (see Tree Rings, p.82 [English translation]).  If all values are
   positive, that's the same value.  If
   y<sub>i</sub>=-y<sub>i-1</sub>, his version blows up (mine only
   blows up when y<sub>i</sub>=y<sub>i-1</sub>=0).  I'll call these
   the "Arizona" (DPL, Corina) and "German" (Schweingruber)
   variations.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class MeanSensitivity {
    private MeanSensitivity() {
	// don't instantiate me
    }

    /**
       Compute the mean sensitivity of a list of numbers.

       @param data list of numbers
       @return the mean sensitivity of the list, or NaN if it can't be
       computed
    */
    @SuppressWarnings("unchecked")
	public static float meanSensitivity(List data) {
        List y = data;
        int N = y.size();

        // a special case
        if (N == 0)
            return Float.NaN;

        try {
            float meanSens = 0.0f;
            float yi, yi1; // y[i], y[i-1]
            for (int i=1; i<N; i++) {
                // (it's slightly more efficient but less readable to
                // dump yi into yi1 each iteration.)

                yi = ((Number) y.get(i)).floatValue();
                yi1 = ((Number) y.get(i-1)).floatValue();

                meanSens += Math.abs(yi - yi1) /
		           (Math.abs(yi) + Math.abs(yi1));

                // note: i know that abs(y[i]) should always be
                // nonnegative -- but may not be if the user applied a
                // bad index.
            }
            return meanSens * 2 / (N-1);
        } catch (ArithmeticException ae) {
            return Float.NaN; // divide-by-zero, probably
        } catch (ClassCastException cce) {
            return Float.NaN; // user is editing, it's a String, so let it go
        }
    }
}
