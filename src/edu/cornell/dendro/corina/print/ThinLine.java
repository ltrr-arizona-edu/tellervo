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

package corina.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.print.PageFormat;

/**
   A thin horizontal line, similar to HTML's &lt;HR&gt;.
   It defaults to the middle 1/2 of the page, but anything's possible.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class ThinLine implements Line {
    // left and right edges of the line, in "fraction of the way
    // across the visible page"
    private float start=0.25f, finish=0.75f;

    /** Create a new default line, which covers the middle 1/2 of the
	page. */
    public ThinLine() {
	// use defaults
    }

    /**
       Create a new line which covers only part of the width of the
       page.  The parameters are given in "fraction of the way across
       the visible page".

       @param start the left end of the line
       @param finish the right end of the line
    */
    public ThinLine(float start, float finish) {
	this.start = start;
	this.finish = finish;
    }

    public void print(Graphics g, PageFormat pf, float y) {
	Graphics2D g2 = (Graphics2D) g;
	g2.setStroke(new BasicStroke(THICKNESS));

	// margins of printable page
	float left = (float) pf.getImageableX();
	float right = (float) (pf.getImageableX() + pf.getImageableWidth());

	// fraction i'm going to draw
	float realLeft = left + (right-left) * start;
	float realRight = left + (right-left) * finish;

	// draw it
	g2.drawLine((int) realLeft, (int) (y+1), (int) realRight, (int) (y+1));
    }

    public int height(Graphics g) {
	return 2;
    }

    private static final float THICKNESS = 0.1f;
}
