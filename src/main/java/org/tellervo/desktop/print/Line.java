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
import java.awt.Font;
import java.awt.print.PageFormat;

/**
   A line which can be added to a printable text document.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public interface Line {
    /** A good size for titles: 18 points. */
    public static final int TITLE_SIZE = 18;

    /** A good size for section headings: 14 points. */
    public static final int SECTION_SIZE = 14;

    /** A good size for normal text: 10 points. */
    public static final int NORMAL_SIZE = 10;

    // standard font -- best way to store? -- with the size consts above, this now seems
    // somewhere between 'redundant' and 'silly'.
    /** Normal font for printing. */
    public static final Font NORMAL =
	new Font("serif", Font.PLAIN, NORMAL_SIZE);

    /** Print the line.  You're given a Graphics object for drawing, a
	y-position to print it at, and the PageFormat which you can
	use to figure out how wide the page is.  (You will not be
	asked to draw it at a position that would go off the top or
	bottom of the page.)
	@param g the Graphics object to use for drawing
	@param pf the PageFormat of this page
	@param y the position from the top of the page to start
	drawing this line */
    public void print(Graphics g, PageFormat pf, float y);

    /** How tall is this line, in points?
	@param g Graphics used to measure the line
	@return the height of this line, in points */
    public int height(Graphics g);
}
