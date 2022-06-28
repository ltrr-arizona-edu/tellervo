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

package org.tellervo.desktop.print;

import java.awt.Graphics;
import java.awt.print.PageFormat;

/**
   An empty line, 18 points (1/4") high.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class EmptyLine implements Line {
    // height: 1/4"
    private static final int height=18;

    /** Create a new empty line. */
    public EmptyLine() {
	// do nothing
    }

    /**
       Print ... nothing.

       @param g the Graphics object to do nothing to
       @param pf the PageFormat to know what page to do nothing on
       @param y where on the page to do nothing
    */
    public void print(Graphics g, PageFormat pf, float y) {
        // do nothing.
    }

    /**
       Return the height of this line: 18 points.

       @param g the Graphics object
       @return the height: 18 points
    */
    public int height(Graphics g) {
        return height;
    }
}
